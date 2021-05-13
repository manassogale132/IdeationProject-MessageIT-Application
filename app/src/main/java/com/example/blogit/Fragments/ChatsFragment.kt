package com.example.blogit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.UsersAdapter
import com.example.blogit.BuildConfig
import com.example.blogit.Model.Chat
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ChatsFragment : Fragment(){

    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewChatsList : RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var mUsers: MutableList<UserInfo>
    lateinit var manager : LinearLayoutManager
    lateinit var collectionReference : CollectionReference

    lateinit var usersList: MutableList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid

        recyclerViewChatsList = view.findViewById(R.id.recyclerViewChatsList)
        manager = LinearLayoutManager(context)
        recyclerViewChatsList.setHasFixedSize(true);
        recyclerViewChatsList.layoutManager = manager

        usersList = ArrayList()

        val db = FirebaseFirestore.getInstance()
        collectionReference = db.collection("Chats")

        collectionReference.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                usersList.clear()

                for (documentsnapshot: DocumentSnapshot in value!!.documents) {
                    val chat: Chat? = documentsnapshot.toObject(Chat::class.java)

                    if(chat?.sender.equals(userID)){
                        if(!usersList.contains(chat?.receiver)) {
                            usersList.add(chat?.receiver.toString())
                        }
                    }
                    if(chat?.receiver.equals(userID)){
                        if(!usersList.contains(chat?.sender)) {
                            usersList.add(chat?.sender.toString())
                        }
                    }
                }
                readChats()
            }
        })
        return view
    }

    private fun readChats() {
        mUsers = ArrayList()

        val db = FirebaseFirestore.getInstance()
        collectionReference = db.collection("User Profiles")

        collectionReference.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
               mUsers.clear()

                for (documentsnapshot: DocumentSnapshot in value!!.documents) {
                    val userInfo: UserInfo? = documentsnapshot.toObject(UserInfo::class.java)

                    for(id : String in usersList) {
                        if (userInfo?.userID.equals(id)) {
                            if (mUsers.size != 0) {
                                for (userl: UserInfo in mUsers) {
                                    if(!userInfo?.userID.equals(userl.userID)) {
                                        mUsers.add(userInfo!!)
                                        break
                                    }
                                }
                            } else {
                                mUsers.add(userInfo!!)
                                break
                            }
                        }
                    }
                }

                usersAdapter = UsersAdapter(context!!,mUsers,true)
                recyclerViewChatsList.adapter = usersAdapter
            }
        })
    }
}