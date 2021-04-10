package com.example.blogit.Activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.blogit.Activities.ui.main.SectionsPagerAdapter
import com.example.blogit.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView


class TabbedActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    //-------------------------------------------------------------------------------------------------------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater : MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }
    //-------------------------------------------------------------------------------------------------------------------------

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.menuLogout -> {
                val userID = auth.currentUser?.uid
                val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID!!)
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap.put("onlineOfflineStatus","offline")

                documentReference.update(hashMap)

                auth.signOut()
                Toast.makeText(baseContext,"Logged Out!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun status(onlineOfflineStatus : String) {

        val userID = auth.currentUser?.uid ?: return
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID)

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