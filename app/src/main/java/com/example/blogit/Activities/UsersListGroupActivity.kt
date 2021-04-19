package com.example.blogit.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.UserGroupAdapter
import com.example.blogit.Adapters.UsersAdapter
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UsersListGroupActivity : AppCompatActivity()  {

    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewUsersGroupList : RecyclerView
    lateinit var userGroupAdapter: UserGroupAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userslistgroupactivity)

        auth = FirebaseAuth.getInstance()

        recyclerViewUsersGroupList = findViewById(R.id.recyclerViewUsersGroupList)
        manager = LinearLayoutManager(this)
        recyclerViewUsersGroupList.setHasFixedSize(true);
        recyclerViewUsersGroupList.layoutManager = manager

        loadDataIntoRecycler()
    }

    private fun loadDataIntoRecycler() {

        val userID = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val query: Query = db.collection("User Profiles").whereNotEqualTo("userID",userID)

        val options: FirestoreRecyclerOptions<UserInfo> = FirestoreRecyclerOptions.Builder<UserInfo>()
            .setQuery(query, UserInfo::class.java).build()

        userGroupAdapter = UserGroupAdapter(options)
        recyclerViewUsersGroupList.adapter = userGroupAdapter
    }

    override fun onStart() {
        super.onStart()
        userGroupAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        userGroupAdapter.stopListening()
    }

}