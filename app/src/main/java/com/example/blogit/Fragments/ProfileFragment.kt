package com.example.blogit.Fragments

import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        displayUserInformation()

        user_edit_info.setOnClickListener {
            editUserInformation()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun displayUserInformation() {
        val userID = auth.currentUser?.uid
        val currentUser = auth.currentUser
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if(currentUser != null) {
                    Glide.with(context!!).load(currentUser.photoUrl).error(R.drawable.user)
                        .into(user_profile_photo)
                }
                user_profile_name.text = value?.getString("fullName")
                user_email.text = value?.getString("emailId")
                user_age.text = value?.getString("age")
                user_phone.text = value?.getString("phoneNumber")
                user_bio.text = value?.getString("status")
            }
        })
    }
    //------------------------------------------------------------------------------------------------------------------------- --
    private fun editUserInformation() {
        val dialogPlus = DialogPlus.newDialog(context)
            .setContentHolder(ViewHolder(R.layout.edit_user_info_dialog))
            .setGravity(Gravity.CENTER).create()

        val myView: View = dialogPlus.holderView
        val fullName: EditText = myView.findViewById(R.id.editEnterFullName)
        val age: EditText = myView.findViewById(R.id.editEnterAge)
        val phoneNumber: EditText = myView.findViewById(R.id.editEnterPhonenumber)
        val status: EditText = myView.findViewById(R.id.editEnterStatus)
        val save: Button = myView.findViewById(R.id.btn_save)
        val close: Button = myView.findViewById(R.id.btn_close)

        dialogPlus.show()

        val userID = auth.currentUser?.uid
        val documentReference: DocumentReference = fStore.collection("User Profiles").document(userID!!)

        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                fullName.setText(value?.getString("fullName"))
                age.setText( value?.getString("age"))
                phoneNumber.setText(value?.getString("phoneNumber"))
                status.setText(value?.getString("status"))
            }
        })

        close.setOnClickListener {
            dialogPlus.dismiss()
        }

        save.setOnClickListener {
            val map: MutableMap<String, Any> = HashMap()
            map["fullName"] = fullName.text.toString()
            map["age"] = age.text.toString()
            map["phoneNumber"] = phoneNumber.text.toString()
            map["status"] = status.text.toString()

            fStore.collection("User Profiles").document(userID).update(map)
            Toast.makeText(it.context, "Profile info updated!", Toast.LENGTH_SHORT).show()
            dialogPlus.dismiss()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
}