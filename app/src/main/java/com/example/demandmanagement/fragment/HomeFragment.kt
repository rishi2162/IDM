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
import com.example.demandmanagement.model.HomeEntity
import com.example.demandmanagement.model.UserEntity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment() {

    lateinit var tvWelcome: TextView
    lateinit var drToday: TextView
    lateinit var drThisMonth: TextView
    lateinit var drTotal: TextView
    lateinit var dfToday: TextView
    lateinit var dfThisMonth: TextView
    lateinit var dfTotal: TextView

    var responseData = JSONArray()
    var stringArray = ArrayList<String>()

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

        val bundle = this.arguments
        if (bundle != null) {
            stringArray = bundle.getStringArrayList("stringArray") as ArrayList<String>
            if (!stringArray.isEmpty()) {
                setView(stringArray, view)
            }
        }
        // Log.d("stringarray", stringArray.toString())


        //responseData = apiCall(view)

        postToList()

        val view_pager2 = view?.findViewById<ViewPager2>(R.id.view_pager2)
        Log.i("ViewPager", view_pager2.toString())
        view_pager2?.adapter = ViewPagerAdapter(notificationsList, this, requireContext())
        view_pager2?.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val indicator = view?.findViewById<CircleIndicator3>(R.id.indicator)
        indicator?.setViewPager(view_pager2)

        return view
    }


//    @SuppressLint("SetTextI18n")
//    private fun setViewContent(response: JSONArray, view: View) {
//        tvWelcome.text = "Welcome ${
//            response.getJSONObject(0).getJSONObject("user").getString("fname")
//        }"
//        drToday.text =
//            response.getJSONObject(5).getJSONObject("home").getString("drToday").toString()
//        drThisMonth.text =
//            response.getJSONObject(5).getJSONObject("home").getString("drThisMonth").toString()
//        drTotal.text =
//            response.getJSONObject(5).getJSONObject("home").getString("drTotal").toString()
//        dfToday.text =
//            response.getJSONObject(5).getJSONObject("home").getString("dfToday").toString()
//        dfThisMonth.text =
//            response.getJSONObject(5).getJSONObject("home").getString("dfThisMonth").toString()
//        dfTotal.text =
//            response.getJSONObject(5).getJSONObject("home").getString("dfTotal").toString()
//    }

    private fun apiCall(view: View): JSONArray {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://demandmgmt.azurewebsites.net/getDetails/va@gmail.com"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                // Log.i("successRequest", response.toString())
                responseData = response
                //Log.d("successRequest", responseData.toString())
                //setViewContent(responseData, view)

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)

        return responseData
    }

    @SuppressLint("SetTextI18n")
    private fun setView(stringArray: ArrayList<String>, view: View) {

        //removing the key part checking first occurence of ':'
        val homeString = stringArray[5].subSequence(1, stringArray[5].length - 1)
        val homeJsonString = homeString.subSequence(homeString.indexOf(":") + 1, homeString.length)

        val userString = stringArray[0].subSequence(1, stringArray[0].length - 1)
        val userJsonString = userString.subSequence(userString.indexOf(":") + 1, userString.length)

        val home = Gson().fromJson(homeJsonString.toString(), HomeEntity::class.java)
        val user = Gson().fromJson(userJsonString.toString(), UserEntity::class.java)
        //Log.i("user", home.drToday.toString())

        tvWelcome = view.findViewById(R.id.tvWelcome)
        tvWelcome.text = "Welcome ${user.fname}"

        drToday = view.findViewById(R.id.drToday)
        drToday.text = home.drToday.toString()

        drThisMonth = view.findViewById(R.id.drThisMonth)
        drThisMonth.text = home.drThisMonth.toString()

        drTotal = view.findViewById(R.id.drTotal)
        drTotal.text = home.drTotal.toString()

        dfToday = view.findViewById(R.id.dfToday)
        dfToday.text = home.dfToday.toString()

        dfThisMonth = view.findViewById(R.id.dfThisMonth)
        dfThisMonth.text = home.dfThisMonth.toString()

        dfTotal = view.findViewById(R.id.dfTotal)
        dfTotal.text = home.dfTotal.toString()

    }


}

