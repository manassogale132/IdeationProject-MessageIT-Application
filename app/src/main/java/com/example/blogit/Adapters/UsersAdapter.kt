package com.example.blogit.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(options: FirestoreRecyclerOptions<UserInfo>):
    FirestoreRecyclerAdapter<UserInfo, UsersAdapter.MyViewHolder>(options)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.users_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UserInfo) {
        holder.userName.text = model.fullName
        holder.userBio.text = model.status
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var user_circular_image_view : CircleImageView = itemView.findViewById(R.id.user_circular_image_view)
        var userName : TextView = itemView.findViewById(R.id.userName)
        var userBio : TextView = itemView.findViewById(R.id.userBio)
    }
}