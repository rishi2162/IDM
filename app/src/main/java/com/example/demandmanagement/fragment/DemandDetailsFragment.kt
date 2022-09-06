package com.example.demandmanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.demandmanagement.R
import com.example.demandmanagement.fragment.demandchildfragment.DemandRaisedFragment


class DemandDetailsFragment : Fragment() {

    private var fCount = 0
    private var rCount = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand_detail, container, false)

        val txtBack = view.findViewById<TextView>(R.id.txtBack)
        txtBack.setOnClickListener {
            val transition = fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, DemandRaisedFragment())?.commit()
        }

        var tvText = view.findViewById<TextView>(R.id.tvFulfilledNumber)
        var btnLeft = view.findViewById<TextView>(R.id.btnLeft)
        var btnRight = view.findViewById<TextView>(R.id.btnRight)

        tvText.text = fCount.toString()

        btnLeft.setOnClickListener {
            if (fCount > 0) {
                fCount--
                Toast.makeText(
                    requireActivity(),
                    "$fCount out of $rCount demands are fulfilled!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            tvText.text = fCount.toString()
        }

        btnRight.setOnClickListener {

            when (fCount) {
                rCount -> {
                    Toast.makeText(
                        requireActivity(),
                        "All demands are fulfilled!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                rCount - 1 -> {
                    fCount++
                    tvText.text = fCount.toString()
                    Toast.makeText(
                        requireActivity(),
                        "All demands are fulfilled!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                else -> {
                    fCount++
                    tvText.text = fCount.toString()
                    Toast.makeText(
                        requireActivity(),
                        "$fCount out of $rCount demands are fulfilled!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        return view
    }

}