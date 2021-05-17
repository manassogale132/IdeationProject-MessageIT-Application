package com.example.blogit.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Activities.MessageActivity
import com.example.blogit.Activities.RegisterActivity
import com.example.blogit.Model.Chat
import com.example.blogit.Model.GroupChat
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*

class UsersAdapter(var context: Context, var mUsers: MutableList<UserInfo>?,var isChat: Boolean) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.users_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val userInfo : UserInfo = mUsers!![position]
        holder.userName.text = userInfo.fullName
        holder.userBio.text = userInfo.status
        Glide.with(holder.user_circular_image_view.context).load(userInfo.profileimage).error(R.drawable.blank_profile_picture).into(holder.user_circular_image_view)

        if (isChat) {
            if(userInfo.onlineOfflineStatus.equals("online")){
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
            intent.putExtra("userID",userInfo.userID)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.e("sizecheck", "getItemCount: ${mUsers!!.size} ")
        return mUsers!!.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var user_circular_image_view : CircleImageView = itemView.findViewById(R.id.user_circular_image_view)
        var userName : TextView = itemView.findViewById(R.id.userName)
        var userBio : TextView = itemView.findViewById(R.id.userBio)
        var sendMessage : Button = itemView.findViewById(R.id.sendMessage)
        var img_on : CircleImageView = itemView.findViewById(R.id.img_on)
        var img_off : CircleImageView = itemView.findViewById(R.id.img_off)
    }

    fun filterList(filteredList : ArrayList<UserInfo>) {
        Log.e("sizecheck", "filterlist: ${filteredList}  ")
        mUsers = filteredList
        notifyDataSetChanged()
    }
}