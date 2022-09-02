package com.example.demandmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity

class ViewPagerAdapter(
    private var notifications: List<String>,
    val fragment: Fragment,
    val context: Context
) :
    RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewModel>() {

    inner class Pager2ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNotifications: TextView = itemView.findViewById(R.id.tvNotifications)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewModel {
        return Pager2ViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewModel, position: Int) {
        holder.tvNotifications.text = notifications[position]

        holder.itemView.setOnClickListener {
//            val transition = fragment.fragmentManager?.beginTransaction()
//            transition?.replace(R.id.frameLayout, TaskDetailFragment())?.commit()
            if (context is MainActivity) {
                (context as MainActivity).moveToTaskIcon()
            }
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

}