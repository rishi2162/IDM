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
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray


class ProfileFragment : Fragment() {

    var responseData = JSONArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        apiCall(view)
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
        return view
    }

    private fun apiCall(view: View): JSONArray {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://json.extendsclass.com/bin/322d051a3560"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                // Log.i("successRequest", response.toString())
                responseData = response
                //Log.d("successRequest", responseData.toString())
                setViewContent(responseData, view)

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)

        return responseData
    }

    @SuppressLint("SetTextI18n")
    private fun setViewContent(response: JSONArray, view: View) {
        userName.text = response.getJSONObject(0).getJSONObject("user")
            .getString("fname") + " " + response.getJSONObject(0).getJSONObject("user")
            .getString("lname")

        userEmail.text =
            response.getJSONObject(0).getJSONObject("user")
                .getString("email")
        pushBtn.isChecked = response.getJSONObject(0).getJSONObject("user")
            .getBoolean("mailactive")

    }


}