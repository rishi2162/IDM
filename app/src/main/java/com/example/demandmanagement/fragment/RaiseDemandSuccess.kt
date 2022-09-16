package com.example.demandmanagement.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.demandmanagement.R


class RaiseDemandSuccess : Fragment() {

    private lateinit var lottieCheck : LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_raise_demand_success, container, false)

        lottieCheck = view.findViewById(R.id.lottieCheck)

        lottieCheck.animate()
        return view
    }

}