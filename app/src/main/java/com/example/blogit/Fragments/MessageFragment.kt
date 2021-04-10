package com.example.blogit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.UsersAdapter
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MessageFragment  : Fragment()  {
    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewALlUsersList : RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_allusers_chat,container,false)
        auth = FirebaseAuth.getInstance()

        recyclerViewALlUsersList = view.findViewById(R.id.recyclerViewALlUsersList)
        manager = LinearLayoutManager(context)
        recyclerViewALlUsersList.setHasFixedSize(true);
        recyclerViewALlUsersList.layoutManager = manager

        loadDataIntoRecycler()

        return view
    }

    private fun loadDataIntoRecycler() {
        val userID = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val query: Query = db.collection("User Profiles").whereNotEqualTo("userID",userID)

        val options: FirestoreRecyclerOptions<UserInfo> = FirestoreRecyclerOptions.Builder<UserInfo>()
            .setQuery(query, UserInfo::class.java).build()

        usersAdapter = UsersAdapter(options,true)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}