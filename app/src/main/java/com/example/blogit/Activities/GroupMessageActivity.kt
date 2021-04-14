package com.example.blogit.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_groupmessage.*
import kotlinx.android.synthetic.main.activity_message.*

class GroupMessageActivity  : AppCompatActivity()  {

    private  var firebaseUser: FirebaseUser? = null
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groupmessage)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val intent : Intent = getIntent()
        val groupid = intent.getStringExtra("groupID")

        val documentReference: DocumentReference = fStore.collection("Groups").document(groupid!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                selectedGroupName.text = value?.getString("groupName")
            }
        })

        backGroupMessageActivity.setOnClickListener {
            finish()
        }
    }
}