package com.example.demandmanagement.fragment.demandchildfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.adapter.DemandRaisedAdapter
import com.example.demandmanagement.adapter.MyApprovalAdapter
import com.example.demandmanagement.model.DemandEntity
import com.example.demandmanagement.model.TaskEntity
import com.example.demandmanagement.util.SwipeToDeleteCallback
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_demand_raised.*

/**
 * A simple [Fragment] subclass.
 * Use the [Tasks.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyApprovalFragment : Fragment() {

    lateinit var mAdapter: MyApprovalAdapter
    lateinit var searchView: SearchView

    var stringArray = ArrayList<String>()
    var demandList = arrayListOf<DemandEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_approval, container, false)
        searchView = view.findViewById(R.id.searchView)

        val bundle = this.arguments
        if (bundle != null) {
            stringArray = bundle.getStringArrayList("stringArray") as ArrayList<String>

            if (stringArray.isNotEmpty()) {
                retrieveData(stringArray)
            }
        }

        return view
    }

    private fun setData() {

        recyclerTasks.layoutManager = LinearLayoutManager(activity)
        recyclerTasks.addItemDecoration(
            DividerItemDecoration(
                activity as Context,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter = MyApprovalAdapter(requireActivity(), demandList, this)
        recyclerTasks.adapter = mAdapter

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                demandList.removeAt(position)
                recyclerTasks.adapter?.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerTasks)

    }

    private fun filterData() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onResume() {
        setData()
        filterData()
        super.onResume()
    }

    private fun retrieveData(stringArray: ArrayList<String>) {

        try {
            //removing the key part checking first occurence of ':'
            val myDemandObject = stringArray[1].subSequence(1, stringArray[1].length - 1)
            val myDemandJsonString =
                myDemandObject.subSequence(myDemandObject.indexOf(":") + 1, myDemandObject.length)

            //converting string to array
            val demandStringArray = myDemandJsonString.split("]},").toTypedArray()

            if (demandStringArray.size == 1) {
                val demand =
                    demandStringArray[0].subSequence(1, demandStringArray[0].length - 1)
                        .toString()

                val myDemand = Gson().fromJson(demand, DemandEntity::class.java)
                demandList.add(myDemand)
            } else {
                for (i in demandStringArray.indices) {

                    when (i) {
                        0 -> {
                            val demand =
                                demandStringArray[i].subSequence(1, demandStringArray[i].length)
                                    .toString() + "]}"
                            val myDemand = Gson().fromJson(demand, DemandEntity::class.java)
                            demandList.add(myDemand)
                        }
                        demandStringArray.size - 1 -> {
                            val demand =
                                demandStringArray[i].subSequence(0, demandStringArray[i].length - 1)
                                    .toString()
                            val myDemand = Gson().fromJson(demand, DemandEntity::class.java)
                            demandList.add(myDemand)
                        }
                        else -> {
                            val demand = demandStringArray[i] + "]}"
                            val myDemand = Gson().fromJson(demand, DemandEntity::class.java)
                            demandList.add(myDemand)
                        }

                    }
                }
            }

        } catch (e: Exception) {
            val transition = this.fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameDemand, NoDemandFoundFragment())?.commit()

        }

    }


}