package com.example.blogit.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import androidx.work.*
import com.example.blogit.Activities.ui.main.SectionsPagerAdapter
import com.example.blogit.EventHandler
import com.example.blogit.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_tabbed.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


class TabbedActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)
        loadLocale()

        /*val constraints: Constraints = Constraints.Builder()
            .setRequiresStorageNotLow(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .build()*/

        /*val oneTimeWorkRequest = OneTimeWorkRequestBuilder<EventHandler>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)*/

        /*val periodicWorkRequest = PeriodicWorkRequestBuilder<EventHandler>( 15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("uniquePeriodicWork")
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("uniquePeriodicWork"
                ,ExistingPeriodicWorkPolicy.REPLACE,periodicWorkRequest)*/


        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.setIcon(R.drawable.ic_baseline_message_24)
        tabs.getTabAt(1)?.setIcon(R.drawable.white_baseline_person_24)
        tabs.getTabAt(2)?.setIcon(R.drawable.ic_baseline_groupstab_24)
        tabs.getTabAt(3)?.setIcon(R.drawable.ic_baseline_statustab_24)
        tabs.getTabAt(4)?.setIcon(R.drawable.ic_baseline_account_circle_24)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        if(viewPager.currentItem != 0) {
            viewPager.setCurrentItem(0)
        }
        else {
            finish()
        }
    }

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
            R.id.menuLanguage -> {
                showChangeLanguageDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun showChangeLanguageDialog() {
        val listItems : Array<String> = arrayOf("English","हिंदी")
        val mBuilder : AlertDialog.Builder = AlertDialog.Builder(this)
        mBuilder.setTitle("Choose Language...")
        mBuilder.setSingleChoiceItems(listItems, -1, DialogInterface.OnClickListener { dialog, i ->
            if(i == 0){
                setLocal("en")
                recreate()
            }
            if(i == 1){
                setLocal("hi")
                recreate()
            }

            dialog.dismiss()
        })
        val mDialog : AlertDialog = mBuilder.create()
        mDialog.show()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setLocal(lang : String) {
        var locale : Locale = Locale(lang)
        Locale.setDefault(locale)
        var config : Configuration = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        //saved data to shared preferences
        val editor : SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("My_Lang", lang)
        editor.apply()
    }

    private fun loadLocale() {
        val pref : SharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language : String? = pref.getString("My_Lang", "")
        setLocal(language!!)
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