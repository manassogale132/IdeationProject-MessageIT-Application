package com.example.blogit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.MessageAdapter
import com.example.blogit.Adapters.UsersAdapter
import com.example.blogit.Model.Chat
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MessageFragment  : Fragment()  {
    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewALlUsersList : RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var mUsers: MutableList<UserInfo>
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_allusers_chat,container,false)
        auth = FirebaseAuth.getInstance()

        recyclerViewALlUsersList = view.findViewById(R.id.recyclerViewALlUsersList)
        manager = LinearLayoutManager(context)
        recyclerViewALlUsersList.setHasFixedSize(true);
        recyclerViewALlUsersList.layoutManager = manager

        mUsers = ArrayList()

        loadDataIntoRecycler()

        return view
    }

    private fun loadDataIntoRecycler() {
        val userID = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val collectionReference: CollectionReference = db.collection("User Profiles")

        collectionReference.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                mUsers.clear()
                for (documentsnapshot: DocumentSnapshot in value!!.documents) {
                    val userInfo: UserInfo? = documentsnapshot.toObject(UserInfo::class.java)
                    if(!userInfo?.userID.equals(userID)) {
                        mUsers.add(userInfo!!)
                    }
                }
                usersAdapter = UsersAdapter(context!!,mUsers,true)
                recyclerViewALlUsersList.adapter = usersAdapter
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}