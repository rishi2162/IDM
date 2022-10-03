package com.example.demandmanagement.fragment

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.LoginActivity
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.model.UserEntity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject


class ProfileFragment : Fragment() {

    lateinit var shortUserName: TextView
    lateinit var userName: TextView
    lateinit var userEmail: TextView
    lateinit var pushBtn: SwitchCompat

    var stringArray = ArrayList<String>()
    lateinit var user: UserEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        (activity as MainActivity).disableSwipe()

        val bundle = this.arguments
        if (bundle != null) {
            stringArray = bundle.getStringArrayList("stringArray") as ArrayList<String>
            if (!stringArray.isEmpty()) {
                setView(stringArray, view)
            }
        }

        val btnSignOut = view.findViewById<Button>(R.id.btnSignOut)

        btnSignOut.setOnClickListener(View.OnClickListener {
            if (VERSION_CODES.KITKAT <= VERSION.SDK_INT) {
                (requireContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                    .clearApplicationUserData() // note: it has a return value!
            }

            val intent = Intent(requireActivity() as Context, LoginActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        })

        pushBtn = view.findViewById(R.id.pushBtn)
        pushBtn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            apiCall()
        })
        return view
    }


    @SuppressLint("SetTextI18n")
    private fun setView(stringArray: ArrayList<String>, view: View) {

        //removing the key part checking first occurence of ':
        val userString = stringArray[0].subSequence(1, stringArray[0].length - 1)
        val userJsonString = userString.subSequence(userString.indexOf(":") + 1, userString.length)

        user = Gson().fromJson(userJsonString.toString(), UserEntity::class.java)

        shortUserName = view.findViewById(R.id.shortUserName)
        shortUserName.text = user.fname[0].toString() + user.lname[0].toString()

        userName = view.findViewById(R.id.userName)
        userName.text = user.fname + " " + user.lname

        userEmail = view.findViewById(R.id.userEmail)
        userEmail.text = user.email

        pushBtn = view.findViewById(R.id.pushBtn)
        pushBtn.isChecked = user.mailactive

    }

    private fun apiCall() {

        val queue = Volley.newRequestQueue(requireActivity())
        val url = "http://20.219.231.57:8080/changeEmailNotification/${user.email}"
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.d("successRequest", response)
                var status = "enabled"
                if(response.contains("inactive")){
                    status = "disabled"
                }
                Toast.makeText(
                    requireActivity(),
                    "Email service ${status}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            {
                Log.d("error", it.localizedMessage)
            })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }


}