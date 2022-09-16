package com.example.demandmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.adapter.DemandRaisedAdapter
import com.example.demandmanagement.adapter.MessageAdapter
import com.example.demandmanagement.fragment.demandchildfragment.DemandRaisedFragment
import com.example.demandmanagement.model.Message
import com.example.demandmanagement.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_demand.*
import kotlinx.android.synthetic.main.fragment_demand_detail.*
import kotlinx.android.synthetic.main.fragment_demand_raised.*


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

            transition?.replace(R.id.frameLayout, DemandFragment())?.commit()
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

        val btnMessages = view.findViewById<Button>(R.id.btnMessages)
        btnMessages.setOnClickListener {
            val transition = this.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, MessagesFragment())?.commit()
        }
        return view
    }

}