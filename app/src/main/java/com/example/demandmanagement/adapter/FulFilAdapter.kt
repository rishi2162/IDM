package com.example.demandmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.model.FulfilEntity

class FulFilAdapter(
    val context: Context,
    val fulfilList: ArrayList<FulfilEntity>
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

        if (currentItem.status == "ACCEPT") {

            holder.icAccept.visibility = View.VISIBLE
            holder.icPending.visibility = View.GONE
            holder.icDecline.visibility = View.GONE

        } else if (currentItem.status == "DECLINE") {

            holder.icAccept.visibility = View.GONE
            holder.icPending.visibility = View.GONE
            holder.icDecline.visibility = View.VISIBLE


        } else if (currentItem.status == "PENDING") {

            holder.icAccept.visibility = View.GONE
            holder.icPending.visibility = View.VISIBLE
            holder.icDecline.visibility = View.GONE

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

        val btnApprove = itemView.findViewById<TextView>(R.id.tvAccept)
        val btnDecline = itemView.findViewById<TextView>(R.id.tvDecline)

    }


}