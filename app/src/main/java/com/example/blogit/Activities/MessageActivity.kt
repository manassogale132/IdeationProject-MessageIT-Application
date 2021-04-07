package com.example.blogit.Activities

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogit.Adapters.MessageAdapter
import com.example.blogit.Adapters.StatusAdapter
import com.example.blogit.Model.Chat
import com.example.blogit.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_message.*
import kotlin.collections.HashMap

class MessageActivity : AppCompatActivity()  {

    private  var firebaseUser: FirebaseUser? = null
    private lateinit var reference: DocumentReference
    private lateinit var fStore: FirebaseFirestore

    lateinit var messageAdapter: MessageAdapter
    lateinit var mchat : MutableList<Chat>
    lateinit var recyclerViewMessageList : RecyclerView
    lateinit var manager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        var intent : Intent = getIntent()
        var userid = intent.getStringExtra("userID")
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recyclerViewMessageList = findViewById(R.id.recyclerViewMessageList)
        manager = LinearLayoutManager(this)
        recyclerViewMessageList.layoutManager = manager

        send_button.setOnClickListener {
            val msg : String = message_input.text.toString()
            if(!msg.equals("")) {
                sendMessage(firebaseUser!!.uid,userid!!,msg)
            }
            else {
                Toast.makeText(this,"You cannot send empty message",Toast.LENGTH_SHORT).show()
            }
            message_input.setText("")
        }

        fStore = FirebaseFirestore.getInstance()
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userid!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                selectedUserName.text = value?.getString("fullName")

                readMessages(firebaseUser!!.uid,userid)
            }
        })


    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun sendMessage(sender :String, receiver: String, message: String ) {

        val db = FirebaseFirestore.getInstance()
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap.put("sender",sender)
        hashMap.put("receiver",receiver)
        hashMap.put("message",message)

        db.collection("Chats").document().set(hashMap)
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun readMessages(myid : String, userid : String) {
        mchat = mutableListOf()

        fStore.collection("Chats").get()
            .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
                override fun onSuccess(p0: QuerySnapshot?) {

                    for( documentsnapshot : DocumentSnapshot in p0!!.documents){
                        val chat : Chat? = documentsnapshot.toObject(Chat::class.java)
                        if(chat!!.receiver.equals(myid) && chat.sender.equals(userid) ||
                                chat.receiver.equals(userid) && chat.sender.equals(myid)) {
                            mchat.add(chat)
                        }

                        messageAdapter = MessageAdapter(this@MessageActivity,mchat)
                        recyclerViewMessageList.adapter = messageAdapter
                    }
                }
            })
    }
    //-------------------------------------------------------------------------------------------------------------------------
}