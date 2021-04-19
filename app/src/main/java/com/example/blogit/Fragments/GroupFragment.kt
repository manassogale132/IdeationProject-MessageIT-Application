package com.example.blogit.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Activities.MessageActivity
import com.example.blogit.Adapters.GroupAdapter
import com.example.blogit.Adapters.UserGroupAdapter
import com.example.blogit.Model.Groups
import com.example.blogit.Model.StatusInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.fragment_groups.*

class GroupFragment: Fragment()   {

    lateinit var recyclerViewALlGroupsList : RecyclerView
    lateinit var groupAdapter: GroupAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_groups,container,false)

        recyclerViewALlGroupsList = view.findViewById(R.id.recyclerViewALlGroupsList)
        manager = LinearLayoutManager(context)
        recyclerViewALlGroupsList.layoutManager = manager

        loadDataIntoRecycler()

        return view
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun loadDataIntoRecycler() {


        val db = FirebaseFirestore.getInstance()
        val query: Query = db.collection("Groups").orderBy("creationTime")

        val options: FirestoreRecyclerOptions<Groups> = FirestoreRecyclerOptions.Builder<Groups>()
            .setQuery(query, Groups::class.java).build()

        groupAdapter = GroupAdapter(options)
        recyclerViewALlGroupsList.adapter = groupAdapter
    }
    override fun onStart() {
        super.onStart()
        groupAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        groupAdapter.stopListening()
    }
    //-------------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createGroupBtn.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(ViewHolder(R.layout.create_group_dialog))
                .setGravity(Gravity.CENTER).create()

            val myViewTwo : View = dialogPlus.holderView
            val groupNameEditText: EditText = myViewTwo.findViewById(R.id.groupNameEditText)
            val btn_create_group: Button = myViewTwo.findViewById(R.id.btn_create_group)
            val btn_close_group: Button = myViewTwo.findViewById(R.id.btn_close_group)

            dialogPlus.show()

            btn_close_group.setOnClickListener {
                dialogPlus.dismiss()
            }

            btn_create_group.setOnClickListener {

                val db = FirebaseFirestore.getInstance()

                val groupID = java.util.UUID.randomUUID().toString()
                val groupName = groupNameEditText.editableText.toString()

                val group = Groups(groupID,groupName)
                db.collection("Groups").document(groupID)
                    .set(group).addOnSuccessListener {
                        Log.d("Groups Info Check", "Groups: success")
                    }
                    .addOnFailureListener {
                        Log.d("Groups Info Check", "Groups: failure")
                    }

                groupNameEditText.setText("")
                Toast.makeText(context as Activity?, "Group Created!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------

}
