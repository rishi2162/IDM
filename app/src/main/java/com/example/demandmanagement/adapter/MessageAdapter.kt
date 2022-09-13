package com.example.demandmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demandmanagement.R
import com.example.demandmanagement.model.Message

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>, val loggedInUser: String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==1){
            // inflate receive
            val view:View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        }
        else{
            // inflate sent
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage = messageList[position]

        if(currMessage.username == loggedInUser){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            // Do the stuff for SentViewHolder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currMessage.message

        }
        else{
            // Do stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.ReceiveMessage.text = currMessage.message
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.tvSentMessage)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ReceiveMessage = itemView.findViewById<TextView>(R.id.tvReceiveMessage)
    }
}