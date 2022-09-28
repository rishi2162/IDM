package com.example.demandmanagement.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.databinding.ActivityMainBinding
import com.example.demandmanagement.fragment.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var stringArray: ArrayList<String>
    lateinit var deviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onPullToRefresh()

        if (intent.extras != null) {
            stringArray = intent.getStringArrayListExtra("response") as ArrayList<String>
            deviceId = intent.getStringExtra("deviceId").toString()

        }
        Log.i("deviceId", deviceId)
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

    private fun onPullToRefresh() {
        var swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout.setOnRefreshListener {

            val handler = Handler()
            handler.postDelayed(
                {
                    if (swipeRefreshLayout.isRefreshing) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                },
                2000,
            )
            stringArray = apiCall()


        }
    }

    private fun apiCall(): ArrayList<String> {
        val queue = Volley.newRequestQueue(this)
        //val url = "https://demandmgmt.azurewebsites.net/getDetails/va@gmail.com"
        val url = "https://mocki.io/v1/036d0ae9-0b3b-4bb4-a413-8b95b6137717"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                //Log.i("successRequest", response.toString())
                stringArray = convertToStringArray(response)

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)

        return stringArray
    }

    private fun convertToStringArray(jsonArray: JSONArray): ArrayList<String> {
        val stringArray = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            stringArray.add(jsonArray.get(i).toString())
        }
        return stringArray
    }

    fun onBackKeyPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    fun disableSwipe() {
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout.isEnabled = false
    }

    fun enableSwipe() {
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout.isEnabled = true
    }

    fun switchBackToDemand() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.selectedItemId = R.id.tasks

        replaceFragments((DemandFragment()), stringArray)

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

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}