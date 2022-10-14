package com.example.demandmanagement.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.demandmanagement.R
import com.example.demandmanagement.activity.MainActivity
import com.example.demandmanagement.adapter.MessageAdapter
import com.example.demandmanagement.model.CommentEntity
import com.example.demandmanagement.model.Message
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MessagesFragment : BottomSheetDialogFragment() {

    private var userData = JSONObject()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    var commentList = arrayListOf<CommentEntity>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messages, container, false)

        (activity as MainActivity).disableSwipe()
        userData = (activity as MainActivity).getUserData()

        val lottieCheckNoComment: LottieAnimationView =
            view.findViewById(R.id.lottieCheckNoComment)
        val tvNoComment: TextView = view.findViewById(R.id.tvNoComment)
        val MessRecyclerView: RecyclerView = view.findViewById(R.id.MessRecyclerView)

        var commentArray = ArrayList<String>()
        var demandId = ""
        val bundle = this.arguments
        if (bundle != null) {
            demandId = bundle.getString("demandId").toString()
            commentArray = bundle.getStringArrayList("commentStringArray") as ArrayList<String>

            for (i in commentArray.indices) {
                val myComment = Gson().fromJson(commentArray[i], CommentEntity::class.java)
                commentList.add(myComment)
            }

//            if (commentList.isEmpty()) {
//                val MessRecyclerView: RecyclerView = view.findViewById(R.id.MessRecyclerView)
//                MessRecyclerView.visibility = View.GONE
//            } else {
//                val lottieCheckNoComment: LottieAnimationView =
//                    view.findViewById(R.id.lottieCheckNoComment)
//                val tvNoComment: TextView = view.findViewById(R.id.tvNoComment)
//
//                lottieCheckNoComment.visibility = View.GONE
//                tvNoComment.visibility = View.GONE
//
//                setView(commentList, view)
//            }

//            setView(commentList, view)
        }


        if (commentList.isEmpty()) {
            MessRecyclerView.visibility = View.GONE
        } else {
            lottieCheckNoComment.visibility = View.GONE
            tvNoComment.visibility = View.GONE

            setView(commentList, view)
        }

        val txtDemandBack = view.findViewById<TextView>(R.id.txtDemandBack)
        txtDemandBack.text = "Demand ID - ${demandId}"
//        txtDemandBack.setOnClickListener {
//            (activity as MainActivity).onBackKeyPressed()
//        }


        // implement the message box
        val messageBox = view.findViewById<EditText>(R.id.messageBox)
        val btnMessage = view.findViewById<Button>(R.id.btnMessage)

        btnMessage.setOnClickListener {
            val mess = messageBox.text.toString()
//            if (mess.isNotEmpty()) {
//                messageList.add(
//                    CommentEntity(
//                        "Rishi Mishra",
//                        "09/11/2022",
//                        mess
//                    )
//                )
//                messageBox.setText("")
//           }

            if (mess.isNotEmpty()) {

                lottieCheckNoComment.visibility = View.GONE
                tvNoComment.visibility = View.GONE
                MessRecyclerView.visibility = View.VISIBLE

                val messPayload = JSONObject()
                messPayload.put("demandId", demandId)
                messPayload.put("requserId", userData.getString("loggedInUserId"))
                messPayload.put("comment", mess)
                messPayload.put("date", LocalDateTime.now())

                apiCall(messPayload)
                //Log.i("mess", messPayload.toString())
                messageBox.setText("")

                commentList.add(
                    CommentEntity(
                        "COOO",
                        mess,
                        "INC0000",
                        userData.getString("loggedInUser"),
                        LocalDateTime.now().toString() + "+00:00"
                    )
                )
                setView(commentList, view)
            }
            else{
                Toast.makeText(requireContext(), "Please enter message", Toast.LENGTH_SHORT).show()
            }
            messageBox.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        return view
    }

    private fun setView(commentList: ArrayList<CommentEntity>, view: View) {

        val messRecyclerView = view.findViewById<RecyclerView>(R.id.MessRecyclerView)
        val myLinearLayoutManager = LinearLayoutManager(activity)
        messRecyclerView.layoutManager = myLinearLayoutManager

        messageAdapter =
            MessageAdapter(requireActivity(), commentList, userData.getString("loggedInUser"))
        messRecyclerView.adapter = messageAdapter
    }

    private fun apiCall(jsonObject: JSONObject) {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "http://20.219.231.57:8080/createComment"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            { response ->
                //Log.i("successRequest", response.toString())

            },
            {
                Log.d("error", it.localizedMessage as String)
            }) {

        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

}