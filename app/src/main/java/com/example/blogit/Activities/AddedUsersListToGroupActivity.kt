package com.example.blogit.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.AddedUsersGroupAdapter
import com.example.blogit.Adapters.UserGroupAdapter
import com.example.blogit.Model.Groups
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_addeduserslisttogroup.*
import kotlinx.android.synthetic.main.activity_userslistgroupactivity.*

class AddedUsersListToGroupActivity  : AppCompatActivity()   {

    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewAddedUsersGroupList : RecyclerView
    lateinit var addedUserGroupAdapter: AddedUsersGroupAdapter
    lateinit var manager : LinearLayoutManager

    private lateinit var groupIDString : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addeduserslisttogroup)

        auth = FirebaseAuth.getInstance()

        val intent: Intent = getIntent()
        groupIDString = intent.getStringExtra("groupID").toString()

        recyclerViewAddedUsersGroupList = findViewById(R.id.recyclerViewAddedUsersGroupList)
        manager = LinearLayoutManager(this)
        recyclerViewAddedUsersGroupList.setHasFixedSize(true);
        recyclerViewAddedUsersGroupList.layoutManager = manager

        loadDataIntoRecycler()

        backTwo.setOnClickListener {
            finish()
        }
    }
    private fun loadDataIntoRecycler() {

        val userID = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val ref: DocumentReference =  db.collection("Groups").document(groupIDString)
        val queryTwo : Query = ref.collection("Members").orderBy("creationTime")


        val options: FirestoreRecyclerOptions<Groups> = FirestoreRecyclerOptions.Builder<Groups>()
            .setQuery(queryTwo, Groups::class.java).build()

        addedUserGroupAdapter = AddedUsersGroupAdapter(options)
        recyclerViewAddedUsersGroupList.adapter = addedUserGroupAdapter
    }

    override fun onStart() {
        super.onStart()
        addedUserGroupAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        addedUserGroupAdapter.stopListening()
    }
}