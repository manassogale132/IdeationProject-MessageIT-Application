package com.example.blogit.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Adapters.MessageAdapter
import com.example.blogit.Model.Chat
import com.example.blogit.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MessageActivity : AppCompatActivity()  {

    private  var firebaseUser: FirebaseUser? = null
    private lateinit var fStore: FirebaseFirestore

    lateinit var messageAdapter: MessageAdapter
    lateinit var mchat : MutableList<Chat>
    lateinit var recyclerViewMessageList : RecyclerView
    lateinit var manager : LinearLayoutManager

    private lateinit var auth: FirebaseAuth

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        auth = FirebaseAuth.getInstance()

        val intent : Intent = getIntent()
        val userid = intent.getStringExtra("userID")

        firebaseUser = FirebaseAuth.getInstance().currentUser

        recyclerViewMessageList = findViewById(R.id.recyclerViewMessageList)
        recyclerViewMessageList.setHasFixedSize(true)
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recyclerViewMessageList.layoutManager = manager

        fStore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userid!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                //Glide.with(applicationContext).load(currentUser?.photoUrl).error(R.drawable.user).into(selectedUserPhoto)
                selectedUserName.text = value?.getString("fullName")

                readMessages(firebaseUser!!.uid,userid)
            }
        })

        send_button.setOnClickListener {
            val msg : String = message_input.text.toString()
            if(!msg.equals("")) {
                sendMessage(firebaseUser!!.uid, userid, msg, creationtime = System.currentTimeMillis(),
                    timestamp = SimpleDateFormat("dd-MM-yyyy / hh:mm:ss").format(Date()))
            }
            else {
                Toast.makeText(this,"You cannot send empty message",Toast.LENGTH_SHORT).show()
            }
            message_input.setText("")
        }

        backMessageActivity.setOnClickListener {
            finish()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun sendMessage(sender :String, receiver: String, message: String ,creationtime : Long,timestamp : String) {

        val db = FirebaseFirestore.getInstance()
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap.put("sender",sender)
        hashMap.put("receiver",receiver)
        hashMap.put("message",message)
        hashMap.put("creationtime",creationtime)
        hashMap.put("timestamp",timestamp)

        db.collection("Chats").document().set(hashMap)
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun readMessages(myid : String, userid : String) {
        mchat = ArrayList()

        val ref : Query = fStore.collection("Chats").orderBy("creationtime",Query.Direction.ASCENDING)
        ref.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                mchat.clear()
                for (documentsnapshot: DocumentSnapshot in value!!.documents) {
                    val chat: Chat? = documentsnapshot.toObject(Chat::class.java)
                    if (chat?.receiver.equals(myid) && chat?.sender.equals(userid) ||
                        chat?.receiver.equals(userid) && chat?.sender.equals(myid)) {
                        mchat.add(chat!!)
                    }

                    messageAdapter = MessageAdapter(this@MessageActivity,mchat)
                    recyclerViewMessageList.adapter = messageAdapter
                }
            }
        })
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun status(onlineOfflineStatus : String) {

        val userID = auth.currentUser?.uid
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID!!)

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap.put("onlineOfflineStatus",onlineOfflineStatus)

        documentReference.update(hashMap)
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }
}