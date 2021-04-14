package com.example.blogit.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Activities.GroupMessageActivity
import com.example.blogit.Activities.MessageActivity
import com.example.blogit.Model.Groups
import com.example.blogit.Model.StatusInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class GroupAdapter(options: FirestoreRecyclerOptions<Groups>):
    FirestoreRecyclerAdapter<Groups, GroupAdapter.MyViewHolder>(options)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.groups_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Groups ) {
        holder.groupName.text = model.groupName

        holder.sendGroupMessageBtn.setOnClickListener {
            val intent = Intent(it.context, GroupMessageActivity::class.java)
            intent.putExtra("groupID",model.groupID)
            it.context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var groupName : TextView = itemView.findViewById(R.id.groupName)
        var sendGroupMessageBtn : Button = itemView.findViewById(R.id.sendGroupMessageBtn)
    }

}