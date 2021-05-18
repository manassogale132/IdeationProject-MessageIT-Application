package com.example.blogit.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Activities.StatusImageFullScreen
import com.example.blogit.Model.StatusInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class StatusAdapterTwo (options: FirestoreRecyclerOptions<StatusInfo>):
    FirestoreRecyclerAdapter<StatusInfo, StatusAdapterTwo.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.status_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: StatusInfo) {
        Glide.with(holder.circular_image_view.context).load(model.pimage).into(holder.circular_image_view)
        holder.statusText.text = model.statustext
        holder.createTime.text = model.timestamp
        holder.statusSenderName.text = model.statusUploadedName
        holder.deleteStatusBtn.visibility = View.GONE

        holder.circular_image_view.setOnClickListener {
            val intent = Intent(it.context, StatusImageFullScreen::class.java)
            intent.putExtra("pimage",model.pimage)
            it.context.startActivity(intent)
        }
    }

    public fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var circular_image_view : CircleImageView = itemView.findViewById(R.id.circular_image_view)
        var statusText : TextView = itemView.findViewById(R.id.statusText)
        var createTime : TextView = itemView.findViewById(R.id.createTime)
        var deleteStatusBtn : ImageView = itemView.findViewById(R.id.deleteStatusBtn)
        var statusSenderName : TextView = itemView.findViewById(R.id.statusSenderName)
    }

}