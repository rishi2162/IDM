package com.example.demandmanagement.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DemandDetailsFragment : Fragment() {

    private var fCount = 0
    private var rCount = 0

    lateinit var tvDemandId: TextView
    lateinit var tvDate: TextView
    lateinit var tvDueDate: TextView
    lateinit var tvRequester: TextView
    lateinit var tvPriority: TextView
    lateinit var tvShift: TextView
    lateinit var tvDmDesgn: TextView
    lateinit var tvYoe: TextView
    lateinit var tvSkills: TextView
    lateinit var tvDesc: TextView
    lateinit var tvRequiredQty: TextView
    lateinit var tvFulfilledQty: TextView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand_detail, container, false)

        var tvFulfilledQty: TextView = view.findViewById(R.id.tvFulfilledQty)

        val bundle = this.arguments
        if (bundle != null) {
            val demandJSONObject = JSONObject()
            demandJSONObject.put("demandId", bundle.getString("demandId"))
            demandJSONObject.put("date", bundle.getString("date"))
            demandJSONObject.put("dueDate", bundle.getString("dueDate"))
            demandJSONObject.put("requesterUserId", bundle.getString("userId"))
            demandJSONObject.put("priority", bundle.getString("priority"))
            demandJSONObject.put("shift", bundle.getString("shift"))
            demandJSONObject.put("dmDesgn", bundle.getString("dmDesgn"))
            demandJSONObject.put("yoe", bundle.getString("yoe"))
            demandJSONObject.put("skills", bundle.getString("skills"))
            demandJSONObject.put("desc", bundle.getString("desc"))
            demandJSONObject.put("requiredQty", bundle.getInt("requiredQty"))
            demandJSONObject.put("fulfilledQty", bundle.getInt("fulfilledQty"))

            rCount = bundle.getInt("requiredQty")


            setView(demandJSONObject, view)
        }

        val txtBack = view.findViewById<TextView>(R.id.txtBack)
        txtBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }


        val btnLeft = view.findViewById<TextView>(R.id.btnLeft)
        val btnRight = view.findViewById<TextView>(R.id.btnRight)

        tvFulfilledQty.text = fCount.toString()

        btnLeft.setOnClickListener {
            if (fCount > 0) {
                fCount--
                Toast.makeText(
                    requireActivity(),
                    "$fCount out of $rCount demands are fulfilled!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            tvFulfilledQty.text = fCount.toString()
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
                    tvFulfilledQty.text = fCount.toString()
                    Toast.makeText(
                        requireActivity(),
                        "All demands are fulfilled!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                else -> {
                    fCount++
                    tvFulfilledQty.text = fCount.toString()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setView(data: JSONObject, view: View) {
        tvDemandId = view.findViewById(R.id.tvDemandId)
        tvDemandId.text = data.getString("demandId")

        tvDate = view.findViewById(R.id.tvDate)
        tvDate.text = convertDate(data.getString("date"))

        tvDueDate = view.findViewById(R.id.tvDueDate)
        tvDueDate.text = convertDate(data.getString("dueDate"))

        tvRequester = view.findViewById(R.id.tvRequester)
        tvRequester.text = data.getString("requesterUserId")

        tvPriority = view.findViewById(R.id.tvPriority)
        tvPriority.text = data.getString("priority")

        tvShift = view.findViewById(R.id.tvShift)
        tvShift.text = data.getString("shift")

        tvDmDesgn = view.findViewById(R.id.tvDmDesgn)
        tvDmDesgn.text = data.getString("dmDesgn")

        tvYoe = view.findViewById(R.id.tvYoe)
        tvYoe.text = data.getString("yoe")

        tvSkills = view.findViewById(R.id.tvSkils)
        tvSkills.text = data.getString("skills")

        tvDesc = view.findViewById(R.id.tvDesc)
        tvDesc.text = data.getString("desc")

        tvRequiredQty = view.findViewById(R.id.tvRequiredQty)
        tvRequiredQty.text = data.getInt("requiredQty").toString()

        tvFulfilledQty = view.findViewById(R.id.tvFulfilledQty)
        tvFulfilledQty.text = data.getInt("fulfilledQty").toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDate(date: String): String? {
        val datetime: LocalDateTime =
            LocalDateTime.parse(
                date.subSequence(0, date.length - 8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S")
            )
        val formattedDate: String = datetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))

        return formattedDate
    }


}