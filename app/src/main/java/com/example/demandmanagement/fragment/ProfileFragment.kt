package com.example.demandmanagement.fragment

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.LoginActivity


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
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

}