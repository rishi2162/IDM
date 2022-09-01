package com.example.demandmanagement.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.demandmanagement.R
import com.example.demandmanagement.databinding.ActivityMainBinding
import com.example.demandmanagement.fragment.HomeFragment
import com.example.demandmanagement.fragment.NewFragment
import com.example.demandmanagement.fragment.ProfileFragment
import com.example.demandmanagement.fragment.TaskFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragments(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragments(HomeFragment())
                R.id.newDemand -> replaceFragments(NewFragment())
                R.id.tasks -> replaceFragments((TaskFragment()))
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
}