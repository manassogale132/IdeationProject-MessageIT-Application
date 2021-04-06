package com.example.blogit.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.StatusAdapter
import com.example.blogit.Adapters.UsersAdapter
import com.example.blogit.Model.StatusInfo
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AllUserListActivity: AppCompatActivity()  {

    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewALlUsersList : RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alluserlist)
        auth = FirebaseAuth.getInstance()

        recyclerViewALlUsersList = findViewById(R.id.recyclerViewALlUsersList)
        manager = LinearLayoutManager(this)
        recyclerViewALlUsersList.layoutManager = manager


        loadDataIntoRecycler()
    }

    private fun loadDataIntoRecycler() {
        val db = FirebaseFirestore.getInstance()
        val query: Query = db.collection("User Profiles")

        val options: FirestoreRecyclerOptions<UserInfo> = FirestoreRecyclerOptions.Builder<UserInfo>()
            .setQuery(query, UserInfo::class.java).build()

        usersAdapter = UsersAdapter(options)
        recyclerViewALlUsersList.adapter = usersAdapter
    }

    override fun onStart() {
        super.onStart()
        usersAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        usersAdapter.stopListening()
    }
}