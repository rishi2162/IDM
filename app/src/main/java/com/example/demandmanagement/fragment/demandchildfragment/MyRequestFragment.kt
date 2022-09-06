package com.example.demandmanagement.fragment.demandchildfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.adapter.DemandRaisedAdapter
import com.example.demandmanagement.adapter.MyApprovalAdapter
import com.example.demandmanagement.adapter.MyRequestAdapter
import com.example.demandmanagement.model.TaskEntity
import com.example.demandmanagement.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_demand_raised.*

/**
 * A simple [Fragment] subclass.
 * Use the [Tasks.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyRequestFragment : Fragment() {

    lateinit var mAdapter: MyRequestAdapter
    lateinit var searchView: SearchView

    private val taskList = arrayListOf<TaskEntity>(
        TaskEntity(
            "UI Technical Lead",
            "A tech lead is required for VIATRIS account having expertise in Angular, ReactJs",
            "Ayush Das",
            "01/09/2022"
        ),
        TaskEntity(
            "JAVA Technical Lead",
            "A tech lead is required for INAM account having expertise in JAVA, SpringBoot",
            "Rishi Mishra",
            "01/09/2022",
            "#7FB77E"
        ),
        TaskEntity(
            "UI Technical Lead",
            "A tech lead is required for INFoM account having expertise in Angular,ReactJs",
            "Raunak Sinha",
            "01/09/2022"
        ),
        TaskEntity(
            "ioS Technical Lead",
            "A tech lead is required for VIATRIS account having expertise in ioS",
            "Anup Ghosh",
            "31/08/2022",
            "#FBDF07"
        ),TaskEntity(
            "UI Technical Lead",
            "A tech lead is required for VIATRIS account having expertise in Angular, ReactJs",
            "Ayush Das",
            "29/08/2022"
        )
    )

//    private val itemList =
//        arrayListOf<String>("item no. 1", "A tech lead is required for VIATRIS account having expertise in Angular, ReactJs no. 2", "item no. 3", "item no. 4")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_request, container, false)
        searchView = view.findViewById(R.id.searchView)
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
        mAdapter = MyRequestAdapter(requireActivity(), taskList, this)
        recyclerTasks.adapter = mAdapter

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                taskList.removeAt(position)
                recyclerTasks.adapter?.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerTasks)

    }

    private fun fetchData(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1 until 21) {
            list.add("test data.  $i")
        }
        return list
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


}