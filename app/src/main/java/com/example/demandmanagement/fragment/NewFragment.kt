package com.example.demandmanagement.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import com.example.demandmanagement.R

class NewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_new, container, false)
        val gridSkill = view.findViewById<GridView>(R.id.gridSkill)
        gridSkill.visibility = View.GONE
        val btnNextPage = view.findViewById<Button>(R.id.btnNextPage)
        btnNextPage.setOnClickListener {
            val transition = this.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, RecipientsFragment())?.commit()
        }
        return view
    }

}