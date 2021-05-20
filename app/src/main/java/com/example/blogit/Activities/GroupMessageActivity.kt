
package com.example.blogit.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogit.Adapters.GroupMessageAdapter
import com.example.blogit.Model.GroupChat
import com.example.blogit.Model.Groups
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_groupmessage.*
import kotlinx.android.synthetic.main.add_group_bio_info_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
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

    lateinit var myViewTwo: View

    private lateinit var groupIDString : String
    private lateinit var groupIDName : String
    private lateinit var adminCheck : String
    private lateinit var senderName : String

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
            val calendar : Calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("dd-MMM / hh:mm a")
            val dateTime : String = simpleDateFormat.format(calendar.time)

            fStore.collection("User Profiles").document(firebaseUser!!.uid)
                .get().addOnSuccessListener {documentSnapshot ->
                    val userInfo: UserInfo? = documentSnapshot.toObject(UserInfo::class.java)
                    val msg : String = message_input_group.text.toString()
                    if(!msg.equals("")) {
                        sendMessage(firebaseUser!!.uid, senderName = userInfo?.fullName.toString(),
                            groupIDReceiver = groupIDString,
                            groupNameReceiver = groupIDName, message = msg, creationtime = System.currentTimeMillis(),
                            timestamp = dateTime)
                    }
                    else {
                        Toast.makeText(this,"You cannot send empty message",Toast.LENGTH_SHORT).show()
                    }
                    message_input_group.setText("")
                }


        }

        groupInfoBio.setOnClickListener {
            addGroupBioMethod()
        }

    }

    private fun sendMessage(sender :String ,senderName :String, groupIDReceiver: String, groupNameReceiver: String , message: String
                            ,creationtime : Long,timestamp : String) {

        val db = FirebaseFirestore.getInstance()
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap.put("sender",sender)
        hashMap.put("senderName",senderName)
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
                    recyclerViewGroupMessageList.scrollToPosition(recyclerViewGroupMessageList.adapter!!.itemCount - 1)
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

    private fun addGroupBioMethod() {
        val dialogPlus = DialogPlus.newDialog(this)
            .setContentHolder(ViewHolder(R.layout.add_group_bio_info_dialog))
            .setGravity(Gravity.CENTER).create()

        myViewTwo = dialogPlus.holderView
        val groupBioInfoTextView: TextView = myViewTwo.findViewById(R.id.groupBioInfoTextView)
        val groupBioInfoEditText: EditText = myViewTwo.findViewById(R.id.groupBioInfoEditText)
        val btn_save_groupbio: Button = myViewTwo.findViewById(R.id.btn_save_groupbio)
        val btn_close_groupbio: Button = myViewTwo.findViewById(R.id.btn_close_groupbio)

        dialogPlus.show()

        val documentReference: DocumentReference = fStore.collection("Groups").document(groupIDString)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                groupBioInfoTextView.setText(value?.getString("groupBioInfo"))

                adminCheck = value?.getString("groupAdminUid").toString()
                if(!adminCheck.equals(firebaseUser!!.uid)){
                    groupBioInfoEditText.visibility = View.GONE
                    btn_save_groupbio.visibility = View.GONE
                    btn_close_groupbio.visibility = View.GONE
                }

            }
        })

        btn_close_groupbio.setOnClickListener {
            dialogPlus.dismiss()
        }

        btn_save_groupbio.setOnClickListener {
            val hashMap: MutableMap<String, Any> = HashMap()
            hashMap.put("groupBioInfo",groupBioInfoEditText.text.toString())

            fStore.collection("Groups").document(groupIDString).update(hashMap)
            Toast.makeText(it.context, "Group bio updated!", Toast.LENGTH_SHORT).show()
        }
    }
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