package com.example.demandmanagement.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.demandmanagement.R
import org.w3c.dom.Text

class LoadingActivity : AppCompatActivity() {

    lateinit var stringArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        if (intent.extras != null) {
            stringArray = intent.getStringArrayListExtra("response") as ArrayList<String>
        }

        var view = findViewById<View>(R.id.view)
        var lottieLoading = findViewById<LottieAnimationView>(R.id.lottieLoading)
        var tvLoading = findViewById<TextView>(R.id.tvLoading)

        lottieLoading.animate().translationY(-1600F).setDuration(2000).setStartDelay(2000)
        tvLoading.animate().translationY(-1600F).setDuration(2000).setStartDelay(2000)

        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putStringArrayListExtra("response", stringArray)
//            startActivity(intent)
//            finish()
        }, 2000)


    }
}