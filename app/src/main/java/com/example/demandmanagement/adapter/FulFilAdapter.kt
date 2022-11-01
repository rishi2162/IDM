package com.example.demandmanagement.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.model.FulfilEntity

class FulFilAdapter(
    val context: Context,
    val fulfilList: ArrayList<FulfilEntity>,
    val state: String
) :
    RecyclerView.Adapter<FulFilAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.fulfil_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = fulfilList[position]
        holder.empName.text = currentItem.empName
        holder.empId.text = currentItem.empId

        when (currentItem.status) {
            "ACCEPT" -> {
                holder.icAccept.visibility = View.VISIBLE
            }

            "DECLINE" -> {
                holder.icDecline.visibility = View.VISIBLE
            }

            "PENDING" -> {
                holder.icPending.visibility = View.VISIBLE
            }
        }

        if (currentItem.status == "PENDING" && state == "APPROVED") {
            holder.llAction.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return fulfilList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val empName = itemView.findViewById<TextView>(R.id.tvEmpName)
        val empId = itemView.findViewById<TextView>(R.id.tvEmpId)

        val icAccept = itemView.findViewById<ImageView>(R.id.iconAccept)
        val icPending = itemView.findViewById<ImageView>(R.id.iconPending)
        val icDecline = itemView.findViewById<ImageView>(R.id.iconDecline)

        val llAction = itemView.findViewById<LinearLayout>(R.id.llAction)
        val btnApprove = itemView.findViewById<TextView>(R.id.tvAccept)
        val btnDecline = itemView.findViewById<TextView>(R.id.tvDecline)

    }


}