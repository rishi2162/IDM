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
    lateinit var stringArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //iniRefreshListener()

        if (intent.extras != null) {
            stringArray = intent.getStringArrayListExtra("response") as ArrayList<String>
        }

        replaceFragments(HomeFragment(), stringArray)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragments(HomeFragment(), stringArray)
                R.id.newDemand -> replaceFragments(NewFragment(), stringArray)
                R.id.tasks -> replaceFragments((DemandFragment()), stringArray)
                R.id.profile -> replaceFragments(ProfileFragment(), stringArray)

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
                R.id.home -> replaceFragments(HomeFragment(), stringArray)
                R.id.newDemand -> replaceFragments(NewFragment(), stringArray)
                R.id.tasks -> replaceFragments((DemandFragment()), stringArray)
                R.id.profile -> replaceFragments(ProfileFragment(), stringArray)

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragments(fragment: Fragment, stringArray: ArrayList<String>) {
        val bundle = Bundle()
        //bundle.putString("responseData", responseData.toString())
        bundle.putStringArrayList("stringArray", stringArray)
        //Log.i("successRequest", stringArray.toString())
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }


}