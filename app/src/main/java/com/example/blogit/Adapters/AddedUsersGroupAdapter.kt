package com.example.blogit.Adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Model.Groups
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class AddedUsersGroupAdapter(options: FirestoreRecyclerOptions<Groups>):
    FirestoreRecyclerAdapter<Groups, AddedUsersGroupAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.added_userstogroup_item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Groups) {
        holder.userNameGroupAdded.text = model.addedUserName

        holder.removeUserFromGroup.setOnClickListener {
            val builder : AlertDialog.Builder = AlertDialog.Builder(holder.userNameGroupAdded.context)
            builder.setTitle("Remove user dialog ")
            builder.setMessage("Remove this user from this group?")

            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                deleteItem(holder.adapterPosition)
                Toast.makeText(it.context, "User removed!", Toast.LENGTH_SHORT).show()

            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->
            })
            builder.show()
        }
    }

    public fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var user_circular_image_view_Added : CircleImageView = itemView.findViewById(R.id.user_circular_image_view_Added)
        var userNameGroupAdded : TextView = itemView.findViewById(R.id.userNameGroupAdded)
        var removeUserFromGroup : ImageView = itemView.findViewById(R.id.removeUserFromGroup)

    }
}