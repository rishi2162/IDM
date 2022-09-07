package com.example.demandmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.Skill
import com.example.demandmanagement.model.Recipient
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_recipients.*
import kotlinx.android.synthetic.main.skill_grid_item.view.*
import java.util.ArrayList

class RecipientsFragment : Fragment() {

    private lateinit var autoCompleteRecipients: AutoCompleteTextView
    private lateinit var chipGroupRecipients: ChipGroup

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

        val txtBack = view.findViewById<TextView>(R.id.tvNewDemand)
        txtBack.setOnClickListener {
            val transition = fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, NewFragment())?.commit()
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


        return view
    }

    private fun addChip(text: String) {
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

