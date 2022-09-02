package com.example.demandmanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity


class TaskDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_detail, container, false)

        val txtBack = view.findViewById<TextView>(R.id.txtBack)
        txtBack.setOnClickListener {
            val transition = fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, TaskFragment())?.commit()
        }

        return view
    }

}