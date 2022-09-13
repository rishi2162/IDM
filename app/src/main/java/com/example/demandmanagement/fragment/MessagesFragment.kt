package com.example.demandmanagement.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.adapter.MessageAdapter
import com.example.demandmanagement.model.Message


class MessagesFragment : Fragment() {

    private var loggedInUser = ""
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messages, container, false)

        //activity?.title =

        val txtDemandBack = view.findViewById<TextView>(R.id.txtDemandBack)
        txtDemandBack.setOnClickListener {
            val transition = fragmentManager?.beginTransaction()
            transition?.replace(R.id.frameLayout, DemandDetailsFragment())?.commit()
        }

        setData(view)


        // implement the message box
        val messageBox = view.findViewById<EditText>(R.id.messageBox)
        val btnMessage = view.findViewById<Button>(R.id.btnMessage)

        btnMessage.setOnClickListener{
            val mess = messageBox.text.toString()
            if(mess.isNotEmpty()){
                messageList.add(
                    Message(
                        "Rishi Mishra",
                        "09/11/2022",
                        mess
                    )
                )
                messageBox.setText("")
            }
            messageBox.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        return view
    }

    private fun setData(view: View) {

        messageList = arrayListOf<Message>(
            Message(
                "Ayush Das",
                "09/11/2022",
                "Could you give me some additional details?"
            ),
            Message(
                "Rishi Mishra",
                "09/11/2022",
                "The candidate must be good at understanding the flow/lifecycle of the project."

            ),
        )

        val messRecyclerView = view.findViewById<RecyclerView>(R.id.MessRecyclerView)
        val myLinearLayoutManager = LinearLayoutManager(activity)
        messRecyclerView.layoutManager = myLinearLayoutManager

        messageAdapter = MessageAdapter(requireActivity(), messageList, "Rishi Mishra")
        messRecyclerView.adapter = messageAdapter
    }
}