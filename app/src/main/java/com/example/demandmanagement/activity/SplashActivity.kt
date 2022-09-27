package com.example.demandmanagement.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONArray

class SplashActivity : AppCompatActivity() {

    private var deviceId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        apiCall()


//        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 2000)
    }

    private fun apiCall() {
        val queue = Volley.newRequestQueue(this)
        //val url = "https://demandmgmt.azurewebsites.net/getDetails/va@gmail.com"
        val url = "https://mocki.io/v1/81c3c27f-a4c1-44bd-81c2-d9898b101b45"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                //Log.i("successRequest", response.toString())

                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val token = task.result
                            deviceId = token
                        }
                    }

                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putStringArrayListExtra("response", convertToStringArray(response))
                    intent.putExtra("deviceId", deviceId)
                    //Log.i("response", convertToStringArray(response).toString())

                    startActivity(intent)
                    finish()
                }, 2000)

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    private fun convertToStringArray(jsonArray: JSONArray): ArrayList<String> {
        val stringArray = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            stringArray.add(jsonArray.get(i).toString())
        }
        return stringArray
    }


}