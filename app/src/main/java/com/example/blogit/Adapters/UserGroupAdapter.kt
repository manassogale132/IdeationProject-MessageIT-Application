package com.example.blogit.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import de.hdodenhof.circleimageview.CircleImageView

class UserGroupAdapter(options: FirestoreRecyclerOptions<UserInfo>, groupid : String):
    FirestoreRecyclerAdapter<UserInfo, UserGroupAdapter.MyViewHolder>(options)   {

    private var groupStringId : String = groupid
    private  var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.all_users_group_item_view,parent,false)
        return UserGroupAdapter.MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: UserInfo) {
        holder.setIsRecyclable(false)
        holder.userName.text = model.fullName
        holder.userBio.text = model.status
        Glide.with(holder.user_circular_image_view_group_list.context)
            .load(model.profileimage)
            .error(R.drawable.blank_profile_picture)
            .into(holder.user_circular_image_view_group_list)

        holder.addUserToGroup.setOnClickListener {

            val db = FirebaseFirestore.getInstance()

            firebaseUser = FirebaseAuth.getInstance().currentUser
            val addedUserID = model.userID
            //val addedUserName = model.fullName
           // val groupAdminUid = firebaseUser!!.uid
            val idList : List<String> = addedUserID!!.split("\\s*,\\s*")

            val hashMap : HashMap<String, Any> = HashMap()
            hashMap.put("memberIds",idList)

            db.collection("Groups").document(groupStringId)
                .update("memberIds",FieldValue.arrayUnion(firebaseUser!!.uid,addedUserID))


            /*db.collection("Groups").document(groupStringId).collection("Members").document(addedUserID!!)
                .set(group).addOnSuccessListener {
                    Log.d("Groups Add", "Added: success")
                }
                .addOnFailureListener {
                    Log.d("Groups Add", "NotAdded: failure")
                }*/

            holder.addUserToGroup.isEnabled = false
            holder.addUserToGroup.text = "Added"
            Toast.makeText(it.context, "Added to Group!", Toast.LENGTH_SHORT).show()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var user_circular_image_view_group_list : CircleImageView = itemView.findViewById(R.id.user_circular_image_view_group_list)
        var userName : TextView = itemView.findViewById(R.id.userNameGroup)
        var userBio : TextView = itemView.findViewById(R.id.userBioGroup)
        var addUserToGroup : Button = itemView.findViewById(R.id.addUserToGroup)
    }
}
