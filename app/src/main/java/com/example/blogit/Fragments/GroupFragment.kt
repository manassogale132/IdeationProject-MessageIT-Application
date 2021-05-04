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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.create_group_dialog.*
import kotlinx.android.synthetic.main.fragment_groups.*

class GroupFragment: Fragment()   {

    lateinit var recyclerViewALlGroupsList : RecyclerView
    lateinit var groupAdapter: GroupAdapter
    lateinit var manager : LinearLayoutManager

    private  var firebaseUser: FirebaseUser? = null
    lateinit var groupID : String

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
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val groupAdminUid = firebaseUser!!.uid
        val query: Query = db.collection("Groups")
            .whereArrayContains("memberIds",firebaseUser!!.uid)

        //.collection("Groups")
        //.where("memberIds", "array-contains", "")

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

        createNewGroupMethod()
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun createNewGroupMethod() {
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

                firebaseUser = FirebaseAuth.getInstance().currentUser

                val db = FirebaseFirestore.getInstance()
                groupID = java.util.UUID.randomUUID().toString()

                val idList : List<String> = firebaseUser!!.uid.split("\\s*,\\s*")

                val hashMap : HashMap<String, Any> = HashMap()
                hashMap.put("groupID",groupID)
                hashMap.put("groupName",groupNameEditText.editableText.toString())
                hashMap.put("groupAdminUid",firebaseUser!!.uid)
                hashMap.put("creationtime",System.currentTimeMillis())
                hashMap.put("memberIds",idList)

                db.collection("Groups").document(groupID).set(hashMap)

                groupNameEditText.setText("")
                Toast.makeText(context as Activity?, "Group Created!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------

}
