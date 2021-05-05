
package com.example.blogit.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.GroupMessageAdapter
import com.example.blogit.Adapters.MessageAdapter
import com.example.blogit.Model.Chat
import com.example.blogit.Model.GroupChat
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_groupmessage.*
import kotlinx.android.synthetic.main.activity_message.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class GroupMessageActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = null
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var groupMessageAdapter: GroupMessageAdapter
    lateinit var gMChat : MutableList<GroupChat>
    lateinit var recyclerViewGroupMessageList : RecyclerView
    lateinit var manager : LinearLayoutManager

    private lateinit var groupIDString : String
    private lateinit var groupIDName : String
    private lateinit var adminCheck : String

    private lateinit var ref : Query

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groupmessage)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val intent: Intent = getIntent()
        groupIDString = intent.getStringExtra("groupID").toString()
        groupIDName = intent.getStringExtra("groupName").toString()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        adminCheck = firebaseUser!!.uid

        recyclerViewGroupMessageList = findViewById(R.id.recyclerViewGroupMessageList)
        recyclerViewGroupMessageList.setHasFixedSize(true)
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recyclerViewGroupMessageList.layoutManager = manager

        val documentReference: DocumentReference = fStore.collection("Groups").document(groupIDString)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                selectedGroupName.text = value?.getString("groupName")

                adminCheck = value?.getString("groupAdminUid").toString()

                if(adminCheck.equals(firebaseUser!!.uid)){
                    addUserTotheGroupImgView.setOnClickListener {
                        addUserTotheGroupImgViewMethod()
                    }
                }
                else{
                    addUserTotheGroupImgView.visibility = View.GONE
                }

                readMessages(firebaseUser!!.uid,groupIDString)
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

        send_button_group.setOnClickListener {
            val msg : String = message_input_group.text.toString()
            if(!msg.equals("")) {
                sendMessage(firebaseUser!!.uid, groupIDString , groupIDName, msg, creationtime = System.currentTimeMillis(),
                    timestamp = SimpleDateFormat("dd-MM-yyyy / hh:mm:ss").format(Date()))
            }
            else {
                Toast.makeText(this,"You cannot send empty message",Toast.LENGTH_SHORT).show()
            }
            message_input_group.setText("")
        }

    }

    private fun sendMessage(sender :String, groupIDReceiver: String, groupNameReceiver: String , message: String
                            ,creationtime : Long,timestamp : String) {

        val db = FirebaseFirestore.getInstance()
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap.put("sender",sender)
        hashMap.put("groupIDReceiver",groupIDReceiver)
        hashMap.put("groupNameReceiver",groupNameReceiver)
        hashMap.put("message",message)
        hashMap.put("creationtime",creationtime)
        hashMap.put("timestamp",timestamp)

        db.collection("Groups").document(groupIDString).collection("Group Chats")
            .document().set(hashMap)
    }

    private fun readMessages(myid : String, groupID : String) {
        gMChat = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val ref: DocumentReference =  db.collection("Groups").document(groupIDString)
        val queryTwo : Query = ref.collection("Group Chats")
            .orderBy("creationtime",Query.Direction.ASCENDING)

        queryTwo.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                gMChat.clear()
                for (documentsnapshot: DocumentSnapshot in value!!.documents) {
                    val groupChat: GroupChat? = documentsnapshot.toObject(GroupChat::class.java)

                        gMChat.add(groupChat!!)

                    groupMessageAdapter = GroupMessageAdapter(this@GroupMessageActivity,gMChat)
                    recyclerViewGroupMessageList.adapter = groupMessageAdapter
                }
            }
        })
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