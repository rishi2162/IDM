package com.example.demandmanagement.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.demandmanagement.R
import com.example.demandmanagement.model.CommentEntity
import com.example.demandmanagement.model.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessageAdapter(
    val context: Context,
    val messageList: ArrayList<CommentEntity>,
    val loggedInUser: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            // inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage = messageList[position]

        if (currMessage.name == loggedInUser) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            // Do the stuff for SentViewHolder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currMessage.comment
            holder.name.text = "Me"
            holder.date.text = convertDateTime(currMessage.date)

        } else {

            val viewHolder = holder as ReceiveViewHolder

            holder.ReceiveMessage.text = currMessage.comment
            holder.name.text = currMessage.name
            holder.date.text = convertDateTime(currMessage.date)

        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.tvSentMessage)
        val name = itemView.findViewById<TextView>(R.id.tvName)
        val date = itemView.findViewById<TextView>(R.id.tvDate)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ReceiveMessage = itemView.findViewById<TextView>(R.id.tvReceiveMessage)
        val name = itemView.findViewById<TextView>(R.id.tvName)
        val date = itemView.findViewById<TextView>(R.id.tvDate)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDateTime(date: String): String? {
        val datetime: LocalDateTime =
            LocalDateTime.parse(
                date.subSequence(0, date.length - 8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S")
            )
        val formattedDate: String = datetime.format(DateTimeFormatter.ofPattern("dd-MMM hh:mm a"))

        return formattedDate
    }
}