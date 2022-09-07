package com.example.demandmanagement.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.demandmanagement.R
import com.example.demandmanagement.databinding.ActivityMainBinding
import com.example.demandmanagement.fragment.*
import com.example.demandmanagement.fragment.DemandDetailsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //iniRefreshListener()
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
}