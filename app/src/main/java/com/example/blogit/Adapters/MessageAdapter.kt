package com.example.blogit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Model.Chat
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MessageAdapter(var context: Context,var mChat: MutableList<Chat>) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    private  var firebaseUser: FirebaseUser? = null
    val MSG_TYPE_LEFT : Int = 0
    val MSG_TYPE_RIGHT : Int = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        return if(viewType == MSG_TYPE_RIGHT) {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.chat_item_right, parent, false)
            MyViewHolder(view)
        } else {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.chat_item_left, parent, false)
            MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat : Chat = mChat.get(position)
        holder.show_message.setText(chat.message)
        holder.messageTimeStamp.setText(chat.timestamp)
        holder.senderUid.visibility = View.GONE
        
        if(position == mChat.size -1) {
            if(chat.isseen!!){
                holder.text_seen.setText("Seen")
            }
            else {
                holder.text_seen.setText("Delivered")
            }
        } else {
            holder.text_seen.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var show_message : TextView = itemView.findViewById(R.id.show_message)
        var messageTimeStamp : TextView = itemView.findViewById(R.id.messageTimeStamp)
        var text_seen : TextView = itemView.findViewById(R.id.text_seen)
        var senderUid : TextView = itemView.findViewById(R.id.senderUid)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(mChat.get(position).sender.equals(firebaseUser!!.uid)){
            return MSG_TYPE_RIGHT
        }
        else {
            return MSG_TYPE_LEFT
        }
    }
}


