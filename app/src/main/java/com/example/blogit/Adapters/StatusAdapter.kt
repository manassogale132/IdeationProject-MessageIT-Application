package com.example.blogit.Adapters

import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.example.blogit.Activities.GroupMessageActivity
import com.example.blogit.Activities.StatusImageFullScreen
import com.example.blogit.Model.StatusInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_statusimagefullscreen.*

class StatusAdapter (options: FirestoreRecyclerOptions<StatusInfo> ,private val context: Context):
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
        holder.statusSenderName.visibility = View.GONE
        holder.deleteStatusBtn.setOnClickListener {
            val builder : AlertDialog.Builder = AlertDialog.Builder(holder.statusText.context)
            builder.setTitle("Delete confirmation ")
            builder.setMessage("Do you want to delete this status?")

            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
               deleteItem(holder.adapterPosition)
                Toast.makeText(it.context, "Status removed!", Toast.LENGTH_SHORT).show()

            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
            })
            builder.show()
        }

        holder.circular_image_view.setOnClickListener {
            val intent = Intent(it.context, StatusImageFullScreen::class.java)
            intent.putExtra("pimage",model.pimage)
            it.context.startActivity(intent)
        }
    }

    public fun deleteItem(position: Int) {
        val reference = snapshots.getSnapshot(position).reference
        val id = reference.id
        reference.delete()
        WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(id)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var circular_image_view : CircleImageView = itemView.findViewById(R.id.circular_image_view)
        var statusText : TextView = itemView.findViewById(R.id.statusText)
        var createTime : TextView = itemView.findViewById(R.id.createTime)
        var statusSenderName : TextView = itemView.findViewById(R.id.statusSenderName)
        var deleteStatusBtn : ImageView = itemView.findViewById(R.id.deleteStatusBtn)
    }

}