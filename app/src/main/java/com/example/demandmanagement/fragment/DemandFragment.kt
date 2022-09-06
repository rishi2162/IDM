package com.example.demandmanagement.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demandmanagement.R
import com.example.demandmanagement.databinding.FragmentDemandBinding
import com.example.demandmanagement.fragment.demandchildfragment.DemandRaisedFragment
import com.example.demandmanagement.fragment.demandchildfragment.MyApprovalFragment
import com.example.demandmanagement.fragment.demandchildfragment.MyRequestFragment

class DemandFragment : Fragment() {

    private lateinit var binding: FragmentDemandBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand, container, false)
        binding = FragmentDemandBinding.inflate(layoutInflater)
        inflater.inflate(R.layout.fragment_demand, container, false)
        replaceFragments(DemandRaisedFragment())

        binding.topNavBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.all -> replaceFragments(DemandRaisedFragment())
                R.id.approval -> replaceFragments((MyApprovalFragment()))
                R.id.request -> replaceFragments(MyRequestFragment())

                else -> {

                }
            }
            true
        }
        return binding.root
    }

    private fun replaceFragments(fragment: Fragment) {
        val transition = this.fragmentManager?.beginTransaction()
        transition?.replace(R.id.frameDemand, fragment)?.commit()
    }


}