package com.example.blogit.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Activities.MessageActivity
import com.example.blogit.Activities.RegisterActivity
import com.example.blogit.Model.Chat
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(options: FirestoreRecyclerOptions<UserInfo>,var isChat: Boolean):
    FirestoreRecyclerAdapter<UserInfo, UsersAdapter.MyViewHolder>(options)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.users_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UserInfo) {
        holder.userName.text = model.fullName
        holder.userBio.text = model.status

        if (isChat) {
            if(model.onlineOfflineStatus.equals("online")){
                holder.img_on.visibility = View.VISIBLE
                holder.img_off.visibility = View.GONE
            } else {
                holder.img_on.visibility = View.GONE
                holder.img_off.visibility = View.VISIBLE
            }
        } else {
            holder.img_on.visibility = View.GONE
            holder.img_off.visibility = View.GONE
        }

        holder.sendMessage.setOnClickListener {
            val intent = Intent(it.context, MessageActivity::class.java)
            intent.putExtra("userID",model.userID)
            it.context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var user_circular_image_view : CircleImageView = itemView.findViewById(R.id.user_circular_image_view)
        var userName : TextView = itemView.findViewById(R.id.userName)
        var userBio : TextView = itemView.findViewById(R.id.userBio)
        var sendMessage : Button = itemView.findViewById(R.id.sendMessage)
        var img_on : CircleImageView = itemView.findViewById(R.id.img_on)
        var img_off : CircleImageView = itemView.findViewById(R.id.img_off)
    }
}