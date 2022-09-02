package com.example.demandmanagement.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.util.MSGraphRequestWrapper
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.microsoft.identity.client.exception.MsalUiRequiredException
import kotlinx.android.synthetic.main.fragment_single_account_mode.*
import org.json.JSONObject
import java.util.*

class SingleAccountModeFragment : Fragment() {
    private val TAG = SingleAccountModeFragment::class.java.simpleName

    /* Azure AD v2 Configs */
    private val AUTHORITY = "https://login.microsoftonline.com/common"

    /* Azure AD Variables */
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_account_mode, container, false)

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(
            context as Context,
            R.raw.auth_config_single_account,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    /**
                     * This test app assumes that the app is only going to support one account.
                     * This requires "account_mode" : "SINGLE" in the config json file.
                     *
                     */
                    mSingleAccountApp = application

                    loadAccount()
                }

                override fun onError(exception: MsalException) {
                    txt_log.text = exception.toString()
                }
            })


        return view
    }

    /**
     * Initializes UI variables and callbacks.
     */
    private fun initializeUI() {

        btn_signIn.setOnClickListener(View.OnClickListener {
            if (mSingleAccountApp == null) {
                return@OnClickListener
            }

            mSingleAccountApp!!.signIn(
                activity as Activity,
                "",
                getScopes(),
                getAuthInteractiveCallback()
            )

        })

//        btn_removeAccount.setOnClickListener(View.OnClickListener {
//            if (mSingleAccountApp == null) {
//                return@OnClickListener
//            }
//
//            /**
//             * Removes the signed-in account and cached tokens from this app.
//             */
//            mSingleAccountApp!!.signOut(object :
//                ISingleAccountPublicClientApplication.SignOutCallback {
//                override fun onSignOut() {
//                    updateUI(null)
//                    performOperationOnSignOut()
//                }
//
//                override fun onError(exception: MsalException) {
//                    displayError(exception)
//                }
//            })
//        })

//        btn_callGraphInteractively.setOnClickListener(View.OnClickListener {
//            if (mSingleAccountApp == null) {
//                return@OnClickListener
//            }
//
//            /**
//             * If acquireTokenSilent() returns an error that requires an interaction,
//             * invoke acquireToken() to have the user resolve the interrupt interactively.
//             *
//             * Some example scenarios are
//             * - password change
//             * - the resource you're acquiring a token for has a stricter set of requirement than your SSO refresh token.
//             * - you're introducing a new scope which the user has never consented for.
//             */
//
//            /**
//             * If acquireTokenSilent() returns an error that requires an interaction,
//             * invoke acquireToken() to have the user resolve the interrupt interactively.
//             *
//             * Some example scenarios are
//             * - password change
//             * - the resource you're acquiring a token for has a stricter set of requirement than your SSO refresh token.
//             * - you're introducing a new scope which the user has never consented for.
//             */
//            mSingleAccountApp!!.acquireToken(
//                requireActivity(),
//                getScopes(),
//                getAuthInteractiveCallback()
//            )
//        })
//
//        btn_callGraphSilently.setOnClickListener(View.OnClickListener {
//            if (mSingleAccountApp == null) {
//                return@OnClickListener
//            }
//
//            /**
//             * Once you've signed the user in,
//             * you can perform acquireTokenSilent to obtain resources without interrupting the user.
//             */
//            mSingleAccountApp!!.acquireTokenSilentAsync(
//                getScopes(),
//                AUTHORITY,
//                getAuthSilentCallback()
//            )
//        })

    }

    override fun onResume() {
        super.onResume()

        initializeUI()
        /**
         * The account may have been removed from the device (if broker is in use).
         * Therefore, we want to update the account state by invoking loadAccount() here.
         */
        loadAccount()
    }

    /**
     * Extracts a scope array from a text field,
     * i.e. from "User.Read User.ReadWrite" to ["user.read", "user.readwrite"]
     */
    private fun getScopes(): Array<String> {

        val userRead = "user.read"
        return userRead.lowercase(Locale.ROOT).split(" ".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

    }

    /**
     * Load the currently signed-in account, if there's any.
     * If the account is removed the device, the app can also perform the clean-up work in onAccountChanged().
     */
    private fun loadAccount() {
        if (mSingleAccountApp == null) {
            return
        }

        mSingleAccountApp!!.getCurrentAccountAsync(object :
            ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                updateUI(activeAccount)
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    performOperationOnSignOut()
                }
            }

            override fun onError(exception: MsalException) {
                txt_log.text = exception.toString()
            }
        })

        refreshApp()

    }

    /**
     * Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private fun getAuthSilentCallback(): AuthenticationCallback {
        return object : AuthenticationCallback {

            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                Log.d(TAG, "Successfully authenticated")

                /* Successfully got a token, use it to call a protected resource - MSGraph */
                callGraphAPI(authenticationResult)
            }

            override fun onError(exception: MsalException) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: $exception")
                displayError(exception)

                when (exception) {
                    is MsalClientException -> {
                        /* Exception inside MSAL, more info inside MsalError.java */
                    }
                    is MsalServiceException -> {
                        /* Exception when communicating with the STS, likely config issue */
                    }
                    is MsalUiRequiredException -> {
                        /* Tokens expired or no session, retry with interactive */
                    }
                }
            }

            override fun onCancel() {
                /* User cancelled the authentication */
                Log.d(TAG, "User cancelled login.")
            }
        }
    }

    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     */
    private fun getAuthInteractiveCallback(): AuthenticationCallback {
        return object : AuthenticationCallback {

            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated")
                Log.d(TAG, "ID Token: " + authenticationResult.account.claims!!["id_token"])

                /* Update account */
                updateUI(authenticationResult.account)

                /* call graph */
                callGraphAPI(authenticationResult)
            }

            override fun onError(exception: MsalException) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: $exception")
                displayError(exception)

                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            override fun onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.")
            }
        }
    }

    /**
     * Make an HTTP request to obtain MSGraph data
     */
    private fun callGraphAPI(authenticationResult: IAuthenticationResult) {
        val graphUrl = "https://graph.microsoft.com/v1.0/me"
        MSGraphRequestWrapper.callGraphAPIWithVolley(
            context as Context, graphUrl,
            authenticationResult.accessToken,
            { response ->
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: $response")
                displayGraphResult(response)
                filterMsalData(response)
            },
            { error ->
                Log.d(TAG, "Error: $error")
                displayError(error)
            })
    }

    //
    // Helper methods manage UI updates
    // ================================
    // displayGraphResult() - Display the graph response
    // displayError() - Display the graph response
    // updateSignedInUI() - Updates UI when the user is signed in
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    /**
     * Display the graph response
     */
    private fun displayGraphResult(graphResponse: JSONObject) {
        txt_log.text = graphResponse.toString()
    }

    /**
     * Display the error message
     */
    private fun displayError(exception: Exception) {
        txt_log.text = exception.toString()
    }

    /**
     * Pass the account details
     */
    private fun filterMsalData(graphResponse: JSONObject) {
        val jsonData = JSONObject()
        jsonData.put("name", graphResponse.get("displayName"))
        jsonData.put("email", graphResponse.get("mail"))
        jsonData.put("userId", graphResponse.get("id"))

       // postData(jsonData)
        Log.i("jsonData", jsonData.toString())

    }

    /**
     * Updates UI based on the current account.
     */
    private fun updateUI(account: IAccount?) {

        if (account != null) {
            btn_signIn.isEnabled = false
            btn_signIn.visibility = View.INVISIBLE
            txt_loading.visibility = View.VISIBLE
            progressBar1.visibility = View.VISIBLE
//            btn_removeAccount.isEnabled = true
//            btn_callGraphInteractively.isEnabled = true
//            btn_callGraphSilently.isEnabled = true
            current_user.text = account.username
        } else {
            btn_signIn.isEnabled = true
            txt_loading.visibility = View.INVISIBLE
            progressBar1.visibility = View.INVISIBLE
//            btn_removeAccount.isEnabled = false
//            btn_callGraphInteractively.isEnabled = false
//            btn_callGraphSilently.isEnabled = false
            current_user.text = ""
        }
    }

    /**
     * Updates UI when app sign out succeeds
     */
    private fun performOperationOnSignOut() {
        val signOutText = "Signed Out."
        current_user.text = ""
        Toast.makeText(context, signOutText, Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * POST Api call
     */
    private fun postData(requestBody: JSONObject) {

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://foodmgmtservice.practicei.xyz/admin/item"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            { response ->
                Log.d("successRequest", response.getString("data").toString())
                val intent = Intent(requireActivity() as Context, MainActivity::class.java)
               // intent.putExtra("name",response.getString("name"))
                requireActivity().startActivity(intent)
                requireActivity().finish()

            },
            {
                Log.d("error", it.localizedMessage)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                return headers
            }
        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }


    private fun refreshApp() {
        if (btn_signIn.isEnabled) {

            mSingleAccountApp!!.acquireToken(
                requireActivity(),
                getScopes(),
                getAuthInteractiveCallback()
            )
        } else {

        }
    }

    private fun signOut(){
//        if (mSingleAccountApp == null) {
//                return@OnClickListener
//            }

            /**
             * Removes the signed-in account and cached tokens from this app.
             */
            mSingleAccountApp!!.signOut(object :
                ISingleAccountPublicClientApplication.SignOutCallback {
                override fun onSignOut() {
                    updateUI(null)
                    performOperationOnSignOut()
                }

                override fun onError(exception: MsalException) {
                    displayError(exception)
                }
            })
    }
}