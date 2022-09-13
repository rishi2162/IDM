package com.example.demandmanagement.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.demandmanagement.R

class DemandApprovalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand_approval, container, false)

        val btnApprovalMessages = view.findViewById<Button>(R.id.btnApprovalMessages)
        btnApprovalMessages.setOnClickListener {
            val transition = this.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, MessagesFragment())?.commit()
        }

        return view
    }

}