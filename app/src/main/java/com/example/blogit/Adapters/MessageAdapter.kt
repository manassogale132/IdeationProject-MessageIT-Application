package com.example.blogit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Model.Chat
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MessageAdapter(private var context: Context,private var mChat: MutableList<Chat>) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    private  var firebaseUser: FirebaseUser? = null
    val msg_type_left : Int = 0
    val msg_type_right : Int = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        if(viewType == msg_type_right) {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.chat_item_right, parent, false)
            return MyViewHolder(view)
        }
        else {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.chat_item_left, parent, false)
            return MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat : Chat = mChat.get(position)
        holder.show_message.setText(chat.message)
    }

    override fun getItemCount(): Int {
        return mChat.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var show_message : TextView = itemView.findViewById(R.id.show_message)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(mChat.get(position).sender.equals(firebaseUser!!.uid)){
            return msg_type_right
        }
        else {
            return msg_type_left
        }
    }
}


