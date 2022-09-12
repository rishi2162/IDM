package com.example.demandmanagement.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray


class HomeFragment : Fragment() {

    var responseData = JSONArray()

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

//        val bundle = this.arguments
//        if (bundle != null) {
//            responseData = bundle.getString("responseData").toString()
//        }
//        Log.d("homeData", responseData)


        responseData = apiCall(view)
        
        postToList()

        val view_pager2 = view?.findViewById<ViewPager2>(R.id.view_pager2)
        Log.i("ViewPager", view_pager2.toString())
        view_pager2?.adapter = ViewPagerAdapter(notificationsList, this, requireContext())
        view_pager2?.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val indicator = view?.findViewById<CircleIndicator3>(R.id.indicator)
        indicator?.setViewPager(view_pager2)

        return view
    }


    @SuppressLint("SetTextI18n")
    private fun setViewContent(response: JSONArray, view: View) {
        tvWelcome.text = "Welcome ${
            response.getJSONObject(0).getJSONObject("user").getString("fname")
        }"
        drToday.text =
            response.getJSONObject(2).getJSONObject("home").getString("drToday").toString()
        drThisMonth.text =
            response.getJSONObject(2).getJSONObject("home").getString("drThisMonth").toString()
        drTotal.text =
            response.getJSONObject(2).getJSONObject("home").getString("drTotal").toString()
        dfToday.text =
            response.getJSONObject(2).getJSONObject("home").getString("dfToday").toString()
        dfThisMonth.text =
            response.getJSONObject(2).getJSONObject("home").getString("dfThisMonth").toString()
        dfTotal.text =
            response.getJSONObject(2).getJSONObject("home").getString("dfTotal").toString()
    }

    private fun apiCall(view: View): JSONArray {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://json.extendsclass.com/bin/322d051a3560"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                // Log.i("successRequest", response.toString())
                responseData = response
                //Log.d("successRequest", responseData.toString())
                setViewContent(responseData, view)

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)

        return responseData
    }


}