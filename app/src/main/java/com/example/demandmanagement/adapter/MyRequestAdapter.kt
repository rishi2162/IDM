package com.example.demandmanagement.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.fragment.DemandDetailsFragment
import com.example.demandmanagement.fragment.NewFragment
import com.example.demandmanagement.fragment.demandchildfragment.NoDemandFoundFragment
import com.example.demandmanagement.model.DemandEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MyRequestAdapter(
    val context: Context,
    private val tasks: ArrayList<DemandEntity>,
    val fragment: Fragment
) :
    RecyclerView.Adapter<MyRequestAdapter.ViewHolder>(), Filterable {

    var filterList = ArrayList<DemandEntity>()

    init {
        filterList = tasks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (filterList[position] == null) {
            val transition = fragment.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameDemand, NoDemandFoundFragment())?.commit()
        } else {
            val currentItem = filterList[position]
            holder.designationView.text = currentItem.dmDesgn
            holder.descView.text = currentItem.desc
            holder.authorView.text = currentItem.name
            holder.dateView.text = convertDate(currentItem.date)

            when (currentItem.status) {
                "APPROVED" -> {
                    holder.colorItemView.setBackgroundColor(Color.parseColor("#7FB77E"))
                }
                "PENDING" -> {
                    holder.colorItemView.setBackgroundColor(Color.parseColor("#FBDF07"))
                }
                "REJECTED" -> {
                    holder.colorItemView.setBackgroundColor(Color.parseColor("#EC9BAA"))
                }
            }

            holder.editIconView.setOnClickListener {
                val editBundle = Bundle()
                if(!currentItem.skills.contains(",")){
                    editBundle.putString("allSkills", currentItem.skills+",")
                }else{
                    editBundle.putString("allSkills", currentItem.skills)
                }
                editBundle.putString("demandId", currentItem.demandId)
                editBundle.putString("date", currentItem.date)
                editBundle.putString("DueDate", convertDateSlash(currentItem.dueDate))
                editBundle.putString("userId", currentItem.userId)
                editBundle.putString("name", currentItem.name)
                editBundle.putString("Priority", currentItem.priority)
                editBundle.putString("Location", currentItem.location)
                editBundle.putString("recipients", currentItem.recipients)
                editBundle.putString("Shift", currentItem.shift)
                editBundle.putString("Designation", currentItem.dmDesgn)
                editBundle.putString("Experience", currentItem.yoe)
                editBundle.putString("Description", currentItem.desc)
                editBundle.putString("NumReq", currentItem.requiredQty.toString())
                editBundle.putString("fulfilledQty", currentItem.fulfilledQty.toString())

                val transition = fragment.fragmentManager?.beginTransaction()
                val fragment = NewFragment()
                fragment.arguments = editBundle

                transition?.replace(R.id.frameLayout, fragment)
                    ?.addToBackStack(fragment.javaClass.name)?.commit()
            }


            holder.itemView.setOnClickListener {
                //Toast.makeText(context, "Task Clicked", Toast.LENGTH_SHORT).show()
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

                    bundle.putString("state", "send")

                    fragment.arguments = bundle

                    transition?.replace(R.id.frameLayout, fragment)
                        ?.addToBackStack(fragment.javaClass.name)?.commit()
                } catch (e: Exception) {
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show()
                }
            }
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
                    val resultList = ArrayList<DemandEntity>()
                    for (row in tasks) {
                        if (row.dmDesgn.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT)) or row.desc.lowercase(
                                Locale.ROOT
                            )
                                .contains(charSearch.lowercase(Locale.ROOT)) or row.userId.lowercase(
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
                filterList = results?.values as ArrayList<DemandEntity>
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
        val editIconView: View = itemView.findViewById(R.id.iconEdit)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDateSlash(date: String): String? {
        val datetime: LocalDateTime =
            LocalDateTime.parse(
                date.subSequence(0, date.length - 8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S")
            )
        val formattedDate: String = datetime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

        return formattedDate
    }


}
