package com.example.demandmanagement.fragment.demandchildfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.demandmanagement.R

class NoDemandFoundFragment : Fragment() {

    private lateinit var lottieCheckNoDemand: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_no_demand_found, container, false)

        lottieCheckNoDemand = view.findViewById(R.id.lottieCheckNoDemand)
        lottieCheckNoDemand.animate()

        return view
    }

}