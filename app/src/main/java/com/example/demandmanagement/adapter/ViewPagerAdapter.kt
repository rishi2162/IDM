package com.example.demandmanagement.adapter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.fragment.DemandDetailsFragment
import com.example.demandmanagement.model.DemandEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewPagerAdapter(
    private var notifications: ArrayList<DemandEntity>,
    val fragment: Fragment,
    val context: Context
) :
    RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewModel>() {

    inner class Pager2ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDesignation: TextView = itemView.findViewById(R.id.tvDesignation)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthorName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewModel {
        return Pager2ViewModel(
            LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Pager2ViewModel, position: Int) {
        val currentItem = notifications[position]

        holder.tvDesignation.text = currentItem.dmDesgn
        holder.tvAuthor.text = currentItem.name

        var descStr :String = currentItem.desc
        //Log.i("len", descStr.length.toString())
        if(descStr.length <= 120){
            holder.tvDesc.text = descStr
        }
        else{
            descStr = descStr.substring(0, 120)
            descStr += ".."
            holder.tvDesc.text = descStr
        }

        holder.tvDate.text = convertDate(currentItem.date)

        holder.itemView.setOnClickListener {
//            val transition = fragment.fragmentManager?.beginTransaction()
//            transition?.replace(R.id.frameLayout, TaskDetailFragment())?.commit()
            if (context is MainActivity) {
                (context as MainActivity).moveToTaskIcon()
            }

            try {
                //apiCall()
                val transition = fragment.fragmentManager?.beginTransaction()
                val fragment = DemandDetailsFragment()
                val bundle = Bundle()
                bundle.putString("demandId", currentItem.demandId)
                bundle.putString("date", currentItem.date)
                bundle.putString("dueDate", currentItem.dueDate)
                bundle.putString("name", currentItem.name)
                bundle.putString("priority", currentItem.priority)
                bundle.putString("location", currentItem.location)
                bundle.putString("recipients", currentItem.recipients)
                bundle.putString("shift", currentItem.shift)
                bundle.putString("dmDesgn", currentItem.dmDesgn)
                bundle.putString("yoe", currentItem.yoe)
                bundle.putString("skills", currentItem.skills)
                bundle.putString("desc", currentItem.desc)
                bundle.putInt("requiredQty", currentItem.requiredQty)
                bundle.putInt("fulfilledQty", currentItem.fulfilledQty)

                bundle.putString("state", "received")

                fragment.arguments = bundle

                transition?.replace(R.id.frameLayout, fragment)
                    ?.addToBackStack(fragment.javaClass.name)?.commit()
            } catch (e: Exception) {
                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDate(date: String): String? {
        val datetime: LocalDateTime =
            LocalDateTime.parse(
                date.subSequence(0, date.length - 8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S")
            )
        val formattedDate: String = datetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))

        return formattedDate
    }

}