package com.example.demandmanagement.adapter

import android.R.id
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.fragment.TaskDetailFragment
import com.example.demandmanagement.model.TaskEntity
import java.util.*
import kotlin.collections.ArrayList


class TaskAdapter(
    val context: Context,
    private val tasks: ArrayList<TaskEntity>,
    val fragment: Fragment
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>(), Filterable {

    var filterList = ArrayList<TaskEntity>()

    init {
        filterList = tasks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filterList[position]
        holder.designationView.text = currentItem.designation
        holder.descView.text = currentItem.desc
        holder.authorView.text = currentItem.author
        holder.dateView.text = currentItem.date
        holder.colorItemView.setBackgroundColor(Color.parseColor(currentItem.color))

        holder.itemView.setOnClickListener {
            //Toast.makeText(context, "Task Clicked", Toast.LENGTH_SHORT).show()
            val transition = fragment.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, TaskDetailFragment())?.commit()

        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = tasks
                } else {
                    val resultList = ArrayList<TaskEntity>()
                    for (row in tasks) {
                        if (row.designation.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT)) or row.desc.lowercase(
                                Locale.ROOT
                            )
                                .contains(charSearch.lowercase(Locale.ROOT)) or row.author.lowercase(
                                Locale.ROOT
                            )
                                .contains(charSearch.lowercase(Locale.ROOT)) or row.date.lowercase(
                                Locale.ROOT
                            ).contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<TaskEntity>
                notifyDataSetChanged()
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val designationView: TextView = itemView.findViewById(R.id.tvDesignation)
        val descView: TextView = itemView.findViewById(R.id.tvDesc)
        val authorView: TextView = itemView.findViewById(R.id.tvAuthor)
        val dateView: TextView = itemView.findViewById(R.id.tvDate)
        val colorItemView: View = itemView.findViewById(R.id.label)

    }
}
