package com.example.demandmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.model.TaskEntity

class TaskAdapter(private val tasks : ArrayList<TaskEntity>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = tasks[position]
        holder.designationView.text = currentItem.designation
        holder.descView.text = currentItem.desc
        holder.authorView.text = currentItem.author
        holder.dateView.text = currentItem.date
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val designationView: TextView = itemView.findViewById(R.id.tvDesignation)
    val descView: TextView = itemView.findViewById(R.id.tvDesc)
    val authorView: TextView = itemView.findViewById(R.id.tvAuthor)
    val dateView: TextView = itemView.findViewById(R.id.tvDate)
}