package com.example.demandmanagement.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import org.w3c.dom.Text


class RaiseDemandSuccess : Fragment() {

    private lateinit var lottieCheck: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_raise_demand_success, container, false)

        val tvDemandSuccess = view.findViewById<TextView>(R.id.tvDemandSuccess)
        val tvDemandUpdate = view.findViewById<TextView>(R.id.tvDemandUpdate)

        val bundle = this.arguments
        val demandId = bundle?.getString("demandID").toString()
        if (demandId != "null") {
            tvDemandSuccess.visibility = View.GONE
            tvDemandUpdate.visibility = View.VISIBLE
        }

        lottieCheck = view.findViewById(R.id.lottieCheck)

        lottieCheck.animate()

        (activity as MainActivity).disableTouch()
        (activity as MainActivity).apiCall()

        return view
    }

}