package com.example.demandmanagement.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.demandmanagement.R

class RecipientsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipients, container, false)

        val txtBack = view.findViewById<TextView>(R.id.tvNewDemand)
        txtBack.setOnClickListener {
            val transition = fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, NewFragment())?.commit()
        }

        return view
    }

}