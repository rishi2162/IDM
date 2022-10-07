package com.example.demandmanagement.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.activity.Skill
import com.example.demandmanagement.model.NewDemand
import com.example.demandmanagement.model.Recipient
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_recipients.*
import kotlinx.android.synthetic.main.skill_grid_item.view.*
import org.json.JSONObject
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class RecipientsFragment : Fragment() {

    private lateinit var autoCompleteRecipients: AutoCompleteTextView
    private lateinit var chipGroupRecipients: ChipGroup

    private lateinit var btnRaiseDemand: Button

    // Implement Save data
    private lateinit var design: String
    private lateinit var desc: String
    private lateinit var dueDate: String
    private lateinit var nreq: String
    private lateinit var exp: String
    private lateinit var allskills: String
    private lateinit var loc: String
    private lateinit var prior: String
    private lateinit var shift: String

    private var allRecipients: String = ""
    private var demandID: String? = null
    private lateinit var emailList: ArrayList<String>

    private val newDemandObject = JSONObject()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recipients, container, false)

        (activity as MainActivity).disableSwipe()

        chipGroupRecipients = view.findViewById(R.id.chipGroupRecipients)

        // implement visibility of imageViews
        var ivAddOne = view.findViewById<ImageView>(R.id.ivAddOne)
        var ivAddTwo = view.findViewById<ImageView>(R.id.ivAddTwo)
        var ivAddThree = view.findViewById<ImageView>(R.id.ivAddThree)

        var ivRemoveOne = view.findViewById<ImageView>(R.id.ivRemoveOne)
        var ivRemoveTwo = view.findViewById<ImageView>(R.id.ivRemoveTwo)
        var ivRemoveThree = view.findViewById<ImageView>(R.id.ivRemoveThree)

        autoCompleteRecipients = view.findViewById(R.id.autoCompleteRecipients)
        chipGroupRecipients = view.findViewById(R.id.chipGroupRecipients)

        // Implement the save data
        val bundle = this.arguments
        if (bundle != null) {
            design = bundle.getString("Designation").toString()
            exp = bundle.getString("Experience").toString()
            allskills = bundle.getString("Skills").toString()
            desc = bundle.getString("Description").toString()
            loc = bundle.getString("Location").toString()
            shift = bundle.getString("Shift").toString()
            prior = bundle.getString("Priority").toString()
            dueDate = bundle.getString("DueDate").toString()
            nreq = bundle.getString("NumReq").toString()
            allRecipients = bundle.getString("recipients").toString()
            demandID = bundle.getString("demandID").toString()
            emailList = bundle.getStringArrayList("emailList") as ArrayList<String>

            if (allRecipients.isNotEmpty()) {
                val allRecipientsArray = allRecipients.split(",").toTypedArray()
                for (j in allRecipientsArray.indices) {
                    if (allRecipientsArray[j] != "null" && allRecipientsArray[j].isNotEmpty())
                        addChip(allRecipientsArray[j])
                    if (allRecipientsArray[j] == "All Managers") {
                        ivAddOne.visibility = View.GONE
                        ivRemoveOne.visibility = View.VISIBLE
                    }
                    if (allRecipientsArray[j] == "Talent Acquisition") {
                        ivAddTwo.visibility = View.GONE
                        ivRemoveTwo.visibility = View.VISIBLE
                    }
                    if (allRecipientsArray[j] == "Leadership Team") {
                        ivAddThree.visibility = View.GONE
                        ivRemoveThree.visibility = View.VISIBLE
                    }
                }
            }
        }

        Log.d("demandID", demandID!!)

        var dateString = ""
        var dateArray = dueDate.split(" ").toTypedArray()

        if (dateArray[1] == "January") {
            dateString = "01/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "February") {
            dateString = "02/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "March") {
            dateString = "03/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "April") {
            dateString = "04/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "May") {
            dateString = "05/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "June") {
            dateString = "06/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "July") {
            dateString = "07/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "August") {
            dateString = "08/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "September") {
            dateString = "09/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "October") {
            dateString = "10/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "November") {
            dateString = "11/" + dateArray[0] + "/" + dateArray[2]
        } else if (dateArray[1] == "December") {
            dateString = "12/" + dateArray[0] + "/" + dateArray[2]
        }


        dateString += " 00:00:00 AM"
        val secondFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu hh:mm:ss a", Locale.ENGLISH)
        val dueDateTime = LocalDateTime.parse(dateString, secondFormatter)


        // Get the current dateTime
        val currentDate = LocalDateTime.now()


        val txtBack = view.findViewById<TextView>(R.id.tvNewDemand)
        txtBack.setOnClickListener {

            val transition = this.fragmentManager?.beginTransaction()

            val bundle = Bundle()

            val n = chipGroupRecipients.childCount - 1
            allRecipients = ""
            for (i in 0..n) {
                val chip = chipGroupRecipients.getChildAt(i) as Chip
                if (allRecipients.isEmpty()) {
                    allRecipients += chip.text.toString()
                } else {
                    allRecipients += ",${chip.text.toString()}"
                }
            }

            bundle.putString("Designation", design)
            bundle.putString("Experience", exp)
            bundle.putString("allSkills", allskills)
            bundle.putString("Description", desc)
            bundle.putString("Location", loc)
            bundle.putString("Priority", prior)
            bundle.putString("Shift", shift)
            bundle.putString("DueDate", dueDate)
            bundle.putString("NumReq", nreq)
            bundle.putString("recipients", allRecipients)
            bundle.putString("demandID", demandID)

            val fragment = NewFragment()
            fragment.arguments = bundle

            transition?.replace(R.id.frameLayout, fragment)?.commit()

//            val transition = fragmentManager?.beginTransaction()
//            transition?.replace(R.id.frameLayout, NewFragment())?.commit()
        }

        // Implement Auto Suggestion with chipGroup

        var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, emailList)
        autoCompleteRecipients.setAdapter(adapter)

        autoCompleteRecipients.setOnKeyListener { _, keyCode, event ->
            if (autoCompleteRecipients.text.toString().isNotEmpty()
                && keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
            ) {

                val name = autoCompleteRecipients.text.toString()
                addChip(name)

                autoCompleteRecipients.text.clear()
                return@setOnKeyListener true
            }
            false
        }

        // 1
        ivAddOne.setOnClickListener {
            ivAddOne.visibility = View.GONE
            ivRemoveOne.visibility = View.VISIBLE

            val name = "All Managers"
            addChip(name)
        }

        // 2
        ivAddTwo.setOnClickListener {
            ivAddTwo.visibility = View.GONE
            ivRemoveTwo.visibility = View.VISIBLE

            val name = "Talent Acquisition"
            addChip(name)
        }

        // 3
        ivAddThree.setOnClickListener {
            ivAddThree.visibility = View.GONE
            ivRemoveThree.visibility = View.VISIBLE

            val name = "Leadership Team"
            addChip(name)
        }


        // get userId
        val userid = (activity as MainActivity).getUserData().getString("loggedInUserId")
        val emailId = (activity as MainActivity).getUserData().getString("loggedInEmail")

        btnRaiseDemand = view.findViewById(R.id.btnRaiseDemand)

        btnRaiseDemand.setOnClickListener {
            if (chipGroupRecipients.childCount >= 1) {

                val n = chipGroupRecipients.childCount - 1
                allRecipients = ""
                for (i in 0..n) {
                    val chip = chipGroupRecipients.getChildAt(i) as Chip
                    if (allRecipients.isEmpty()) {
                        allRecipients += chip.text.toString()
                    } else {
                        allRecipients += ",${chip.text.toString()}"
                    }
                }

                newDemandObject.put("userId", userid)
                newDemandObject.put("dmDesgn", design)
                newDemandObject.put("email", emailId)
                newDemandObject.put("yoe", exp)
                newDemandObject.put("requiredQty", nreq.toInt())
                newDemandObject.put("skills", allskills)
                newDemandObject.put("desc", desc)
                newDemandObject.put("location", loc)
                newDemandObject.put("recipients", allRecipients)
                newDemandObject.put("date", currentDate)
                newDemandObject.put("dueDate", dueDateTime)
                //newDemandObject.put("status", "PENDING")
                newDemandObject.put("fulfilledQty", 1)
                newDemandObject.put("shift", shift)
                newDemandObject.put("priority", prior)
                newDemandObject.put("active", true)


                apiCall(newDemandObject, demandID!!)

            } else {
                autoCompleteRecipients.setBackgroundResource(R.drawable.custom_input_check)

                Toast.makeText(
                    requireActivity(),
                    "Please add at least one recipients",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    private fun addChip(text: String) {

        var flag: Boolean = false
        val n = chipGroupRecipients.childCount - 1
        for (i in 0..n) {
            val chip = chipGroupRecipients.getChildAt(i) as Chip
            if (chip.text.toString() == text) {
                flag = true
                break
            }
        }
        if (!flag) {
            val chip = Chip(requireActivity())
            chip.text = text

            chip.isCloseIconVisible = true

            chip.setOnCloseIconClickListener {
                if (text == "All Managers" && ivRemoveOne.isVisible) {
                    ivAddOne.visibility = View.VISIBLE
                    ivRemoveOne.visibility = View.GONE
                }

                if (text == "Talent Acquisition" && ivRemoveTwo.isVisible) {
                    ivAddTwo.visibility = View.VISIBLE
                    ivRemoveTwo.visibility = View.GONE
                }

                if (text == "Leadership Team" && ivRemoveThree.isVisible) {
                    ivAddThree.visibility = View.VISIBLE
                    ivRemoveThree.visibility = View.GONE
                }
                chipGroupRecipients.removeView(chip)
            }

            chipGroupRecipients.addView(chip)
        }
    }

    private fun apiCall(newDemandObject: JSONObject, demandID: String) {
        val queue = Volley.newRequestQueue(requireActivity())
        var url = ""

        if(demandID == null){
            url = "http://20.219.231.57:8080/demand"
        }
        else{
            url = "http://20.219.231.57:8080/editDemand/${demandID}"
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, newDemandObject,
            { response ->
                Log.d("successRequest", response.toString())
                val transition = this.fragmentManager?.beginTransaction()
                val fragment = RaiseDemandSuccess()
                val bundle = Bundle()
                bundle.putString("demandID", demandID)
                fragment.arguments = bundle
                transition?.replace(R.id.frameLayout, fragment)?.commit()
            },
            {
                Log.d("error", it.localizedMessage as String)

            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
}

