package com.example.blogit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Model.GroupChat
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class GroupMessageAdapter(var context: Context, var gMChat: MutableList<GroupChat>) : RecyclerView.Adapter<GroupMessageAdapter.MyViewHolder>()  {

    private  var firebaseUser: FirebaseUser? = null
    val MSG_TYPE_LEFT : Int = 0
    val MSG_TYPE_RIGHT : Int = 1
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMessageAdapter.MyViewHolder {
        return if(viewType == MSG_TYPE_RIGHT) {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.chat_item_right, parent, false)
            GroupMessageAdapter.MyViewHolder(view)
        } else {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.chat_item_left, parent, false)
            GroupMessageAdapter.MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: GroupMessageAdapter.MyViewHolder, position: Int) {
        val groupChat : GroupChat = gMChat.get(position)
        holder.show_message.setText(groupChat.message)
        holder.messageTimeStamp.setText(groupChat.timestamp)
        holder.text_seen.visibility = View.GONE
        holder.senderUid.setText(groupChat?.sender)

        /*if(getItemViewType(position) == MSG_TYPE_LEFT){
            db.collection("User Profiles").document(groupChat.sender.toString())
                .get().addOnSuccessListener {documentSnapshot ->
                    val userInfo: UserInfo? = documentSnapshot.toObject(UserInfo::class.java)
                    holder.senderUid.setText(userInfo?.fullName)
                }
        }*/
    }

    override fun getItemCount(): Int {
        return gMChat.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var show_message : TextView = itemView.findViewById(R.id.show_message)
        var messageTimeStamp : TextView = itemView.findViewById(R.id.messageTimeStamp)
        var text_seen : TextView = itemView.findViewById(R.id.text_seen)
        var senderUid : TextView = itemView.findViewById(R.id.senderUid)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(gMChat.get(position).sender.equals(firebaseUser!!.uid)){
            return MSG_TYPE_RIGHT
        }
        else {
            return MSG_TYPE_LEFT
        }
    }

}