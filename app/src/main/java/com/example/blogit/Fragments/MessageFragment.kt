package com.example.blogit.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.UsersAdapter
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_allusers_chat.*

class MessageFragment  : Fragment()  {
    private lateinit var auth: FirebaseAuth
    lateinit var recyclerViewALlUsersList : RecyclerView
    lateinit var usersAdapter: UsersAdapter
    lateinit var mUsers: MutableList<UserInfo>
    lateinit var manager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_allusers_chat,container,false)
        auth = FirebaseAuth.getInstance()
        mUsers = ArrayList()

        recyclerViewALlUsersList = view.findViewById(R.id.recyclerViewALlUsersList)
        manager = LinearLayoutManager(context)
        recyclerViewALlUsersList.setHasFixedSize(true);
        recyclerViewALlUsersList.layoutManager = manager
        usersAdapter = UsersAdapter(context!!,mUsers,false)
        recyclerViewALlUsersList.adapter = usersAdapter



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
                usersAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchUserET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterMethod(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun filterMethod(text: String) {
        val filteredList: ArrayList<UserInfo> = ArrayList()

        for(user : UserInfo in mUsers) {
            if(user.fullName?.toLowerCase()?.contains(text.toLowerCase())!!) {
                filteredList.add(user)
            }
        }

        usersAdapter.filterList(filteredList)
    }
}