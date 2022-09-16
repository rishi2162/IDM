package com.example.demandmanagement.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.android.volley.Request
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
import java.util.ArrayList

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recipients, container, false)

        // Implement the save data
        val bundle = this.arguments
        if (bundle != null) {
            design = bundle.getString("Designation").toString()
            desc = bundle.getString("Description").toString()
            dueDate = bundle.getString("DueDate").toString()
            nreq = bundle.getString("NumReq").toString()
            exp = bundle.getString("Experience").toString()
            allskills = bundle.getString("Skills").toString()
            loc = bundle.getString("Location").toString()
            shift = bundle.getString("Shift").toString()
            prior = bundle.getString("Priority").toString()
        }

        Log.i("myTag", allskills)

        val txtBack = view.findViewById<TextView>(R.id.tvNewDemand)
        txtBack.setOnClickListener {

            val transition = this.fragmentManager?.beginTransaction()
            val bundle = Bundle()
            bundle.putString("Designation", design)
            bundle.putString("Description", desc)
            bundle.putString("DueDate", dueDate)
            bundle.putString("NumReq", nreq)

            bundle.putString("Experience", exp)

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

//            newDemandObject.put("dmDesgn", design)
//            newDemandObject.put("yoe", exp)
//            newDemandObject.put("skills", allskills)
//            newDemandObject.put("desc", desc)
//            newDemandObject.put("location", loc)
//            newDemandObject.put("priority", prior)
//            newDemandObject.put("shift", shift)
//            newDemandObject.put("dueDate", dueDate)
//            newDemandObject.put("requiredQty", nreq)
//            newDemandObject.put("recipients", allRecipients)
//
//            newDemandObject.put("userId", "INC02231")
//            newDemandObject.put("email", "anish.raj@incture.com")
//            //newDemandObject.put("date", )
//
//            apiCall(newDemandObject)

            val transition = this.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, RaiseDemandSuccess())?.commit()

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

