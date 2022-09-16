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
import com.example.demandmanagement.model.HomeEntity
import com.example.demandmanagement.model.UserEntity
import com.google.gson.Gson

class DemandFragment : Fragment() {

    var stringArray = ArrayList<String>()

    private lateinit var binding: FragmentDemandBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand, container, false)
        binding = FragmentDemandBinding.inflate(layoutInflater)
        inflater.inflate(R.layout.fragment_demand, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            stringArray = bundle.getStringArrayList("stringArray") as ArrayList<String>
        }

        replaceFragments(DemandRaisedFragment(), stringArray)

        binding.topNavBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.all -> replaceFragments(DemandRaisedFragment(), stringArray)
                R.id.approval -> replaceFragments(MyApprovalFragment(), stringArray)
                R.id.request -> replaceFragments(MyRequestFragment(), stringArray)

                else -> {

                }
            }
            true
        }
        return binding.root
    }

    private fun replaceFragments(fragment: Fragment, stringArray: ArrayList<String>) {
        val transition = this.fragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putStringArrayList("stringArray", stringArray)

        fragment.arguments = bundle
        transition?.replace(R.id.frameDemand, fragment)?.commit()
    }


}