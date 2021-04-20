package com.example.blogit.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_groupmessage.*
import kotlinx.android.synthetic.main.activity_message.*

class GroupMessageActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = null
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var groupIDString : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groupmessage)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val intent: Intent = getIntent()
        groupIDString = intent.getStringExtra("groupID").toString()

        val documentReference: DocumentReference = fStore.collection("Groups").document(groupIDString)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                selectedGroupName.text = value?.getString("groupName")
            }
        })

        backGroupMessageActivity.setOnClickListener {
            finish()
        }

        addUserTotheGroupImgView.setOnClickListener {
            addUserTotheGroupImgViewMethod()
        }

        checkAddedUsersToGrpBtn.setOnClickListener {
            checkAddedUsersToGrpMethod()
        }
    }

    private fun addUserTotheGroupImgViewMethod() {
        val intent = Intent(this, UsersListGroupActivity::class.java)
        Toast.makeText(this, "Showing all users!", Toast.LENGTH_SHORT).show()
        intent.putExtra("groupID",groupIDString)
        startActivity(intent)
    }

    private fun checkAddedUsersToGrpMethod() {
        val intent = Intent(this, AddedUsersListToGroupActivity::class.java)
        Toast.makeText(this, "Showing all Group members!", Toast.LENGTH_SHORT).show()
        intent.putExtra("groupID",groupIDString)
        startActivity(intent)
    }
}