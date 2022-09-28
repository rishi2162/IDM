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

class RecipientsFragment : Fragment() {

    private lateinit var autoCompleteRecipients: AutoCompleteTextView
    private lateinit var chipGroupRecipients: ChipGroup

    private lateinit var btnRaiseDemand : Button

    // Implement Save data
    private lateinit var design : String
    private lateinit var desc : String
    private lateinit var dueDate : String
    private lateinit var nreq : String
    private lateinit var exp : String
    private lateinit var allskills : String
    private lateinit var loc : String
    private lateinit var prior : String
    private lateinit var shift : String

    private var allRecipients : String = ""

    private val newDemandObject = JSONObject()

    // Create a Arraylist of value

    val names: List<String> = listOf(
        "Amelia",
        "Ava",
        "Alexander",
        "Avery",
        "Asher",
        "Aiden",
        "Abigail",
        "Anthony",
        "Aria",
        "Andrew",
        "Aurora",
        "Angel",
        "Adrian",
        "Aaron",
        "Addison",
        "Axel",
        "Austin",
        "Audrey",
        "Aubrey",
        "Adam"
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recipients, container, false)

        (activity as MainActivity).disableSwipe()

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
        }

        var dateString = ""
        var dateArray = dueDate.split(" ").toTypedArray()
        //Log.i("myTag", dateArray.toString())

        if(dateArray[1]=="January"){
            dateString = "01/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="February"){
            dateString = "02/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="March"){
            dateString = "03/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="April"){
            dateString = "04/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="May"){
            dateString = "05/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="June"){
            dateString = "06/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="July"){
            dateString = "07/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="August"){
            dateString = "08/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="September"){
            dateString = "09/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="October"){
            dateString = "10/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="November"){
            dateString = "11/" +  dateArray[0]  + "/"  + dateArray[2]
        }
        else if(dateArray[1]=="December"){
            dateString = "12/" +  dateArray[0]  + "/"  + dateArray[2]
        }


        dateString+=" 00:00:00 AM"
        val secondFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu hh:mm:ss a", Locale.ENGLISH)
        val dueDateTime = LocalDateTime.parse(dateString, secondFormatter)


        // Get the current dateTime
        val currentDate = LocalDateTime.now()


        val txtBack = view.findViewById<TextView>(R.id.tvNewDemand)
        txtBack.setOnClickListener {

            val transition = this.fragmentManager?.beginTransaction()
            val bundle = Bundle()
            bundle.putString("Designation", design)
            bundle.putString("Experience", exp)
            bundle.putString("allSkills", allskills)
            bundle.putString("Description", desc)
            bundle.putString("Location", loc)
            bundle.putString("Priority", prior)
            bundle.putString("Shift", shift)
            bundle.putString("DueDate", dueDate)
            bundle.putString("NumReq", nreq)

            val fragment = NewFragment()
            fragment.arguments = bundle

            transition?.replace(R.id.frameLayout, fragment)?.commit()

//            val transition = fragmentManager?.beginTransaction()
//            transition?.replace(R.id.frameLayout, NewFragment())?.commit()
        }

        // Implement Auto Suggestion with chipGroup

        autoCompleteRecipients = view.findViewById(R.id.autoCompleteRecipients)
        chipGroupRecipients = view.findViewById(R.id.chipGroupRecipients)

        var adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, names)
        autoCompleteRecipients.setAdapter(adapter)

        autoCompleteRecipients.setOnKeyListener { _, keyCode, event ->
            if (autoCompleteRecipients.text.toString().isNotEmpty()
                && keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
            ) {

                val name = autoCompleteRecipients.text.toString()
                addChip(name)
                if(allRecipients.isEmpty()){
                    allRecipients += "$name"
                }
                else{
                    allRecipients += ", $name"
                }
                autoCompleteRecipients.text.clear()
                return@setOnKeyListener true
            }
            false
        }


        // implement visibility of imageViews
        var ivAddOne = view.findViewById<ImageView>(R.id.ivAddOne)
        var ivAddTwo = view.findViewById<ImageView>(R.id.ivAddTwo)
        var ivAddThree = view.findViewById<ImageView>(R.id.ivAddThree)

        var ivRemoveOne = view.findViewById<ImageView>(R.id.ivRemoveOne)
        var ivRemoveTwo = view.findViewById<ImageView>(R.id.ivRemoveTwo)
        var ivRemoveThree = view.findViewById<ImageView>(R.id.ivRemoveThree)


        // 1
        ivAddOne.setOnClickListener{
            ivAddOne.visibility = View.GONE
            ivRemoveOne.visibility = View.VISIBLE

            val name = "All Managers"
            addChip(name)
        }

        // 2
        ivAddTwo.setOnClickListener{
            ivAddTwo.visibility = View.GONE
            ivRemoveTwo.visibility = View.VISIBLE

            val name = "Talent Acquisition"
            addChip(name)
        }

        // 3
        ivAddThree.setOnClickListener{
            ivAddThree.visibility = View.GONE
            ivRemoveThree.visibility = View.VISIBLE

            val name = "Leadership Team"
            addChip(name)
        }


        btnRaiseDemand = view.findViewById(R.id.btnRaiseDemand)

        btnRaiseDemand.setOnClickListener {
            if(chipGroupRecipients.childCount >=1 ){
                val n = chipGroupRecipients.childCount - 1
                for(i in 0..n){
                    val chip = chipGroupRecipients.getChildAt(i) as Chip
                    if(allRecipients.isEmpty()){
                        allRecipients += chip.text.toString()
                    }
                    else{
                        allRecipients += ", ${chip.text.toString()}"
                    }
                }

                newDemandObject.put("userId", "INC02231")
                newDemandObject.put("dmDesgn", design)
                newDemandObject.put("email", "anish.raj@incture.com")
                newDemandObject.put("yoe", exp)
                newDemandObject.put("requiredQty", nreq.toInt())
                newDemandObject.put("skills", allskills)
                newDemandObject.put("desc", desc)
                newDemandObject.put("location", loc)
                newDemandObject.put("recipients", allRecipients)
                newDemandObject.put("date", currentDate)
                newDemandObject.put("dueDate", dueDateTime)
                newDemandObject.put("status", "PENDING")
                newDemandObject.put("fulfilledQty", 1)
                newDemandObject.put("shift", shift)
                newDemandObject.put("priority", prior)
                newDemandObject.put("active", true)

                apiCall(newDemandObject)

                val transition = this.fragmentManager?.beginTransaction()
                transition?.replace(R.id.frameLayout, RaiseDemandSuccess())?.commit()
            }
            else{
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

        var flag : Boolean = false
        val n = chipGroupRecipients.childCount - 1
        for(i in 0..n){
            val chip = chipGroupRecipients.getChildAt(i) as Chip
            if(chip.text.toString() == text){
                flag = true
                break
            }
        }
        if(!flag){
            val chip = Chip(requireActivity())
            chip.text = text

            chip.isCloseIconVisible = true

            chip.setOnCloseIconClickListener{
                if(text == "All Managers" && ivRemoveOne.isVisible){
                    ivAddOne.visibility = View.VISIBLE
                    ivRemoveOne.visibility = View.GONE
                }

                if(text == "Talent Acquisition" && ivRemoveTwo.isVisible){
                    ivAddTwo.visibility = View.VISIBLE
                    ivRemoveTwo.visibility = View.GONE
                }

                if(text == "Leadership Team" && ivRemoveThree.isVisible){
                    ivAddThree.visibility = View.VISIBLE
                    ivRemoveThree.visibility = View.GONE
                }
                chipGroupRecipients.removeView(chip)
            }

            chipGroupRecipients.addView(chip)
        }
    }

    private fun apiCall(newDemandObject : JSONObject) {
        val queue = Volley.newRequestQueue(requireActivity())
        val url = "https://demandmgmt.azurewebsites.net/demand"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, newDemandObject,
            { response ->
                Log.i("successRequest", response.toString())

            },
            {
                Log.i("errorRequest", newDemandObject.toString())
                Log.d("error", it.localizedMessage as String)

            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

//        val jsonObjectRequest : JsonObjectRequest =
//            object : JsonObjectRequest(Method.POST, url, newDemandObject,
//                Response.Listener { response ->
//                    // response
//                    var strResp = response.toString()
//                    Log.d("API", strResp)
//                },
//                Response.ErrorListener { error ->
//                    Log.d("API", "error => $error")
//                }
//            ){
//                override fun getBody(): ByteArray {
//                    return newDemandObject.toByteArray(Charset.defaultCharset())
//                }
//            }
//        queue.add(jsonObjectRequest)
    }
}

