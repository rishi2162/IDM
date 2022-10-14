package com.example.demandmanagement.fragment

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray
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
    lateinit var tvLocation: TextView
    lateinit var tvDmDesgn: TextView
    lateinit var tvYoe: TextView
    lateinit var tvSkills: TextView
    lateinit var tvDesc: TextView
    lateinit var tvRequiredQty: TextView
    lateinit var tvFulfilledQty: TextView

    var state = ""
    var changeFlag = "false"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_demand_detail, container, false)

        (activity as MainActivity).disableSwipe()

        var tvFulfilledQty: TextView = view.findViewById(R.id.tvFulfilledQty)

        val bundle = this.arguments
        if (bundle != null) {
            val demandJSONObject = JSONObject()
            demandJSONObject.put("demandId", bundle.getString("demandId"))
            demandJSONObject.put("date", bundle.getString("date"))
            demandJSONObject.put("dueDate", bundle.getString("dueDate"))
            demandJSONObject.put("requesterName", bundle.getString("name"))
            demandJSONObject.put("priority", bundle.getString("priority"))
            demandJSONObject.put("shift", bundle.getString("shift"))
            demandJSONObject.put("location", bundle.getString("location"))
            demandJSONObject.put("dmDesgn", bundle.getString("dmDesgn"))
            demandJSONObject.put("yoe", bundle.getString("yoe"))
            demandJSONObject.put("skills", bundle.getString("skills"))
            demandJSONObject.put("desc", bundle.getString("desc"))
            demandJSONObject.put("requiredQty", bundle.getInt("requiredQty"))
            demandJSONObject.put("fulfilledQty", bundle.getInt("fulfilledQty"))

            rCount = bundle.getInt("requiredQty")
            fCount = bundle.getInt("fulfilledQty")

            state = bundle.getString("state").toString()


            setView(demandJSONObject, view)
        }

        val txtBack = view.findViewById<TextView>(R.id.txtBack)
        txtBack.setOnClickListener {
            if (changeFlag == "true") {
                (activity as MainActivity).disableTouch()
                (activity as MainActivity).loadRefreshView()

            } else {
                (activity as MainActivity).switchBackToDemand()
            }
        }


        val btnLeft = view.findViewById<TextView>(R.id.btnLeft)
        val btnRight = view.findViewById<TextView>(R.id.btnRight)

        if (state == "received") {
            btnLeft.visibility = View.GONE
            btnRight.visibility = View.GONE
        }


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

            fulfillQtyChAPI(bundle?.getString("demandId").toString(), "decrease")
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
            fulfillQtyChAPI(bundle?.getString("demandId").toString(), "increase")
        }

        val btnMessages = view.findViewById<Button>(R.id.btnMessages)
        btnMessages.setOnClickListener {
            if (bundle != null) {
                commentApiCall(bundle.getString("demandId").toString())
            } else {
                commentApiCall("D")
            }
        }


        return view
    }

    private fun commentApiCall(demandId: String) {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://20.219.231.57:8080/fetchCommentsByDemandId/${demandId}"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                //Log.i("successRequest", response.toString())
                val commentArray = convertToStringArray(response)
                val fragment = MessagesFragment()
                val fragmentManager = (activity as MainActivity).supportFragmentManager
                val commentBundle = Bundle()
                commentBundle.putStringArrayList("commentStringArray", commentArray)
                commentBundle.putString("demandId", demandId)
                fragment.arguments = commentBundle

                fragmentManager?.let { fragment.show(it, "MessagesFragment") }

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
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
        tvRequester.text = data.getString("requesterName")

        tvPriority = view.findViewById(R.id.tvPriority)
        tvPriority.text = data.getString("priority")

        tvShift = view.findViewById(R.id.tvShift)
        tvShift.text = data.getString("shift")

        tvLocation = view.findViewById(R.id.tvLocation)
        tvLocation.text = data.getString("location")

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

    private fun convertToStringArray(jsonArray: JSONArray): ArrayList<String> {
        val stringArray = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            stringArray.add(jsonArray.get(i).toString())
        }
        return stringArray
    }

    private fun fulfillQtyChAPI(demandId: String, operator: String) {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://20.219.231.57:8080/updateFulfilledQty/${demandId}/${operator}"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, null,
            { response ->
                changeFlag = "true"

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
}