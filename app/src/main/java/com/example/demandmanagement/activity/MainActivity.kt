package com.example.demandmanagement.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.databinding.ActivityMainBinding
import com.example.demandmanagement.fragment.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var responseData = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //iniRefreshListener()

        responseData = apiCall()
        replaceFragments(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragments(HomeFragment())
                R.id.newDemand -> replaceFragments(NewFragment())
                R.id.tasks -> replaceFragments((DemandFragment()))
                R.id.profile -> replaceFragments(ProfileFragment())

                else -> {

                }
            }
            true
        }

    }


    fun moveToTaskIcon() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.selectedItemId = R.id.tasks

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, DemandDetailsFragment()).commit()

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragments(HomeFragment())
                R.id.newDemand -> replaceFragments(NewFragment())
                R.id.tasks -> replaceFragments((DemandFragment()))
                R.id.profile -> replaceFragments(ProfileFragment())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragments(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("responseData", responseData.toString())
        //Log.d("responseMainActivity", responseData.toString())
        fragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun iniRefreshListener() {
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        swipeRefreshLayout.setOnRefreshListener { // This method gets called when user pull for refresh,
            // You can make your API call here,
            val handler = Handler()
            handler.postDelayed(
                {
                    if (swipeRefreshLayout.isRefreshing) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                },
                3000,
            )
        }
    }

    private fun apiCall(): JSONArray {
        val queue = Volley.newRequestQueue(this)
        val url = "https://json.extendsclass.com/bin/322d051a3560"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                // Log.i("successRequest", response.toString())
                responseData = response
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