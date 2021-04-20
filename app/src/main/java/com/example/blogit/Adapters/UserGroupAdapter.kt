package com.example.blogit.Adapters

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.parseUri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Model.Groups
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class UserGroupAdapter(options: FirestoreRecyclerOptions<UserInfo>, groupid : String):
    FirestoreRecyclerAdapter<UserInfo, UserGroupAdapter.MyViewHolder>(options)   {

    private var groupStringId : String = groupid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.all_users_group_item_view,parent,false)
        return UserGroupAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UserInfo) {
        holder.userName.text = model.fullName
        holder.userBio.text = model.status

        holder.addUserToGroup.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            val addedUserID = model.userID
            val addedUserName = model.fullName
            val addedUserTest = null

            val group = Groups(addedUserID,addedUserName,addedUserTest)

            db.collection("Groups").document(groupStringId).collection("Members").document(addedUserID!!)
                .set(group).addOnSuccessListener {
                    Log.d("Groups Add", "Added: success")
                }
                .addOnFailureListener {
                    Log.d("Groups Add", "NotAdded: failure")
                }

            Toast.makeText(it.context, "Added to Group!", Toast.LENGTH_SHORT).show()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var user_circular_image_view : CircleImageView = itemView.findViewById(R.id.user_circular_image_view)
        var userName : TextView = itemView.findViewById(R.id.userNameGroup)
        var userBio : TextView = itemView.findViewById(R.id.userBioGroup)
        var addUserToGroup : Button = itemView.findViewById(R.id.addUserToGroup)
    }
}