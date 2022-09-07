package com.example.demandmanagement.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.demandmanagement.R
import com.example.demandmanagement.adapter.ViewPagerAdapter
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    private var notificationsList = mutableListOf<String>()

    private fun addToList(notification: String) {
        notificationsList.add(notification)
    }

    private fun postToList() {
        for (i in 1..5) {
            addToList(" A technical lead is required for VIATRIS account having ....")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        postToList()

        val view_pager2 = view?.findViewById<ViewPager2>(R.id.view_pager2)
        Log.i("ViewPager", view_pager2.toString())
        view_pager2?.adapter = ViewPagerAdapter(notificationsList, this, requireContext())
        view_pager2?.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val indicator = view?.findViewById<CircleIndicator3>(R.id.indicator)
        indicator?.setViewPager(view_pager2)

        //iniRefreshListener()
        return view
    }

//    private fun iniRefreshListener() {
//        val swipeRefreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
//        swipeRefreshLayout?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { // This method gets called when user pull for refresh,
//            // You can make your API call here,
//            Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                // Your Code
//                if (swipeRefreshLayout.isRefreshing) {
//                    swipeRefreshLayout.isRefreshing = false
//                    swipeRefreshLayout.visibility = View.GONE
//                }
//            }, 3000)
//        })
//    }
}