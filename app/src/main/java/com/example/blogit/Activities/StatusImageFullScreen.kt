package com.example.blogit.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_statusimagefullscreen.*

class StatusImageFullScreen : AppCompatActivity()  {

    private lateinit var pimageString : String
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statusimagefullscreen)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        val intent: Intent = getIntent()
        pimageString = intent.getStringExtra("pimage").toString()

        Glide.with(statusFullScreenImage).load(pimageString).into(statusFullScreenImage)
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