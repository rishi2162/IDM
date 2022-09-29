package com.example.demandmanagement.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.allViews
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_new.*
import java.text.SimpleDateFormat
import java.util.*

class NewFragment : Fragment() {

    @SuppressLint("NewApi")
    var formatDate = SimpleDateFormat("dd MMMM YYYY", Locale.US)

    // Chip Group
    private lateinit var inputSkill: AutoCompleteTextView
    private lateinit var skillsChipGroup: ChipGroup


    // Implement save Data
    private lateinit var inputDesignation: EditText
    private var design: String = ""

    private var exp: String = ""

    private lateinit var inputDescription: EditText
    private var desc: String = ""

    private var dueDate: String = ""

    private lateinit var etNumReq: EditText
    private var nreq: String = ""

    private var allSkills: String = ""

    private var loc: String = ""
    private var prior: String = ""
    private var shift: String = ""

    var skills: List<String> = listOf(
        "HTML",
        "CSS",
        "Python",
        "Java",
        "JavaScript",
        "Swift",
        "C++",
        "C#",
        "R",
        "Golang (Go)"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new, container, false)

        (activity as MainActivity).disableSwipe()

        val btnCalendarView = view.findViewById<TextView>(R.id.btnCalendar)
        val inputYOE = view.findViewById<AutoCompleteTextView>(R.id.dropDownYoe)
        val inputLOC = view.findViewById<AutoCompleteTextView>(R.id.dropDownLocation)
        val inputShift = view.findViewById<AutoCompleteTextView>(R.id.dropDownShift)
        val inputPriority = view.findViewById<AutoCompleteTextView>(R.id.dropDownPriority)

        val yoeList = resources.getStringArray(R.array.yearOfExp)
        val locList = resources.getStringArray(R.array.location)
        val shiftList = resources.getStringArray(R.array.shifts)
        val priorityList = resources.getStringArray(R.array.priority)

        val locArrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item_yoe, locList)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item_yoe, yoeList)
        val shiftArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down_item_yoe, shiftList)
        val priorityArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down_item_yoe, priorityList)

        inputYOE.setAdapter(arrayAdapter)
        inputLOC.setAdapter(locArrayAdapter)
        inputShift.setAdapter(shiftArrayAdapter)
        inputPriority.setAdapter(priorityArrayAdapter)

        btnCalendarView.setOnClickListener() {
            val getDate: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val selectDate: Calendar = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, i)
                    selectDate.set(Calendar.MONTH, i2)
                    selectDate.set(Calendar.DAY_OF_MONTH, i3)
                    val date = formatDate.format(selectDate.time)
                    btnCalendarView.text = date
                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePicker.show()
        }


        // Skill Chip Group

        inputSkill = view.findViewById(R.id.inputSkill)
        skillsChipGroup = view.findViewById(R.id.skillsChipGroup)

        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, skills)
        inputSkill.setAdapter(adapter)

        inputSkill.setOnKeyListener { _, keyCode, event ->

            if (inputSkill.text.toString().isNotEmpty()
                && keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
            ) {

                val name = inputSkill.text.toString()
                addChip(name)
                inputSkill.text.clear()
                return@setOnKeyListener true
            }
            false
        }


        // Implementing the save data

        inputDesignation = view.findViewById(R.id.inputDesignation)
        inputDescription = view.findViewById(R.id.inputDescription)
        etNumReq = view.findViewById(R.id.etNumReq)

        val bundle = this.arguments
        if (bundle != null) {
            design = bundle.getString("Designation").toString()
            exp = bundle.getString("Experience").toString()
            allSkills = bundle.getString("allSkills").toString()
            desc = bundle.getString("Description").toString()
            loc = bundle.getString("Location").toString()
            prior = bundle.getString("Priority").toString()
            shift = bundle.getString("Shift").toString()
            dueDate = bundle.getString("DueDate").toString()
            nreq = bundle.getString("NumReq").toString()

            if (design != "null") {
                inputDesignation.setText(design)
                inputYOE.setText(exp)
                inputDescription.setText(desc)
                inputLOC.setText(loc)
                inputPriority.setText(prior)
                inputShift.setText(shift)
                btnCalendarView.text = dueDate
                etNumReq.setText(nreq)

                var allSkillsArray = allSkills.split(",").toTypedArray()
                for (i in 0..allSkillsArray.size - 1) {
                    if(allSkillsArray[i]!="null" && allSkillsArray[i].isNotEmpty())
                        addChip(allSkillsArray[i])
                }
            }
        }

        // Move to the next Fragment
        val btnNextPage = view.findViewById<Button>(R.id.btnNextPage)
        btnNextPage.setOnClickListener {

            if (inputDesignation.text.isNotEmpty() &&
                inputYOE.text.isNotEmpty() &&
                skillsChipGroup.childCount >= 1 &&
                inputDescription.text.isNotEmpty() &&
                dropDownLocation.text.isNotEmpty() &&
                dropDownPriority.text.isNotEmpty() &&
                dropDownShift.text.isNotEmpty() &&
                btnCalendar.text.isNotEmpty() &&
                etNumReq.text.isNotEmpty()
            ) {
                val transition = this.fragmentManager?.beginTransaction()
                val bundle = Bundle()

                val n = skillsChipGroup.childCount - 1
                for (i in 0..n) {
                    val chip = skillsChipGroup.getChildAt(i) as Chip
                    if (allSkills.isEmpty()) {
                        allSkills += chip.text.toString()
                    } else {
                        allSkills += ", ${chip.text.toString()}"
                    }
                }

                bundle.putString("Designation", inputDesignation.text.toString())
                bundle.putString("Experience", inputYOE.text.toString())
                bundle.putString("Skills", allSkills)
                bundle.putString("Description", inputDescription.text.toString())
                bundle.putString("Location", dropDownLocation.text.toString())
                bundle.putString("Priority", inputPriority.text.toString())
                bundle.putString("Shift", inputShift.text.toString())
                bundle.putString("DueDate", btnCalendarView.text.toString())
                bundle.putString("NumReq", etNumReq.text.toString())

                val fragment = RecipientsFragment()
                fragment.arguments = bundle
                transition?.replace(R.id.frameLayout, fragment)?.commit()
            } else {

                if (inputDesignation.text.isEmpty()) {
                    inputDesignation.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (inputDesignation.text.isNotEmpty()) {
                    inputDesignation.setBackgroundResource(R.drawable.custom_input)
                }


                if (inputYOE.text.isEmpty()) {
                    inputYOE.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (inputYOE.text.isNotEmpty()) {
                    inputYOE.setBackgroundResource(R.drawable.custom_input)
                }

                if (skillsChipGroup.childCount < 1) {
                    inputSkill.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (skillsChipGroup.childCount >= 1) {
                    inputSkill.setBackgroundResource(R.drawable.custom_input)
                }

                if (inputDescription.text.isEmpty()) {
                    inputDescription.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (inputDescription.text.isNotEmpty()) {
                    inputDescription.setBackgroundResource(R.drawable.custom_input)
                }

                if (dropDownLocation.text.isEmpty()) {
                    dropDownLocation.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (dropDownLocation.text.isNotEmpty()) {
                    dropDownLocation.setBackgroundResource(R.drawable.custom_input)
                }

                if (dropDownPriority.text.isEmpty()) {
                    dropDownPriority.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (dropDownPriority.text.isNotEmpty()) {
                    dropDownPriority.setBackgroundResource(R.drawable.custom_input)
                }

                if (dropDownShift.text.isEmpty()) {
                    dropDownShift.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (dropDownShift.text.isNotEmpty()) {
                    dropDownShift.setBackgroundResource(R.drawable.custom_input)
                }

                if (btnCalendar.text.isEmpty()) {
                    btnCalendar.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (btnCalendar.text.isNotEmpty()) {
                    btnCalendar.setBackgroundResource(R.drawable.custom_input)
                }

                if (etNumReq.text.isEmpty()) {
                    etNumReq.setBackgroundResource(R.drawable.custom_input_check)
                }
                if (etNumReq.text.isNotEmpty()) {
                    etNumReq.setBackgroundResource(R.drawable.custom_input)
                }

                Toast.makeText(
                    requireActivity(),
                    "Fill all the mandatory fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        return view
    }

    private fun addChip(text: String) {
        var flag: Boolean = false
        val n = skillsChipGroup.childCount - 1
        for (i in 0..n) {
            val chip = skillsChipGroup.getChildAt(i) as Chip
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
                skillsChipGroup.removeView(chip)
            }
            skillsChipGroup.addView(chip)
        }
    }


}