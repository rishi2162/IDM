package com.example.demandmanagement.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

        when (currentItem.statusOfFulfilledQty) {
            "APPROVED" -> {
                holder.icAccept.visibility = View.VISIBLE
            }

            "REJECTED" -> {
                holder.icDecline.visibility = View.VISIBLE
            }

            "PENDING" -> {
                holder.icPending.visibility = View.VISIBLE
            }
        }

        if (currentItem.statusOfFulfilledQty == "PENDING" && state == "APPROVED") {
            holder.llAction.visibility = View.VISIBLE
        }

        holder.btnApprove.setOnClickListener {
            val sId = currentItem.serialId
            changeStatusApiCall(sId, "APPROVED")
        }

        holder.btnDecline.setOnClickListener {
            val sId = currentItem.serialId
            changeStatusApiCall(sId, "REJECTED")
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

    private fun changeStatusApiCall(sId: String, status: String) {
        val queue = Volley.newRequestQueue(context)
        val url = "http://20.204.235.62:8080/changeFulfilledQtyCmtStatus/${sId}/${status}"
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(
                    context,
                    "$status",
                    Toast.LENGTH_SHORT
                ).show()
            },
            {
                Log.d("error", it.localizedMessage)
            })
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

}