package com.example.blogit.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Model.StatusInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class StatusAdapter (options: FirestoreRecyclerOptions<StatusInfo>):
    FirestoreRecyclerAdapter<StatusInfo, StatusAdapter.MyViewHolder>(options) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.status_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: StatusInfo) {
        Glide.with(holder.circular_image_view.context).load(model.pimage).into(holder.circular_image_view)
        holder.statusText.text = model.statustext
        holder.createTime.text = model.timestamp
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var circular_image_view : CircleImageView = itemView.findViewById(R.id.circular_image_view)
        var statusText : TextView = itemView.findViewById(R.id.statusText)
        var createTime : TextView = itemView.findViewById(R.id.createTime)

    }

}