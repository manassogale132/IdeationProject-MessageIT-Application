package com.example.blogit.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.blogit.Model.UserInfo
import com.example.blogit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        showPasswordCheckBox()

        registerSignUpNewUserBtn.setOnClickListener {
            signUpUserValidation()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------

    private fun showPasswordCheckBox() {
        registerPasswordCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                registerPassword.transformationMethod = PasswordTransformationMethod.getInstance();
            } else {
                registerPassword.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun signUpUserValidation() {
        if (registerFullName.text.toString().isEmpty()) {
            registerFullName.error = "Please enter Fullname"
            registerFullName.requestFocus()
            return
        }
        if (registerFullName.text.toString().matches("[0-9*\$%#&^()@!_+{}';]".toRegex())) {
            registerFullName.error = "Please enter proper Fullname"
            registerFullName.requestFocus()
            return
        }
        if (registerAge.text.toString().isEmpty()) {
            registerAge.error = "Please enter Age"
            registerAge.requestFocus()
            return
        }
        if (registerEmailId.text.toString().isEmpty()) {
            registerEmailId.error = "Please enter EmailID"
            registerEmailId.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(registerEmailId.text.toString()).matches()) {
            registerEmailId.error = "Please enter Valid EmailID"
            registerEmailId.requestFocus()
            return
        }
        if (registerPassword.text.toString().isEmpty()) {
            registerPassword.error = "Please enter Password"
            registerPassword.requestFocus()
            return
        }
        if (registerPassword.length() < 6) {
            registerPassword.setError("Password must be > 6 characters")
            return
        }
        if (registerPhoneNumber.text.toString().isEmpty()) {
            registerPhoneNumber.error = "Please enter PhoneNumber"
            registerPhoneNumber.requestFocus()
            return
        }

        registerProgressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(registerEmailId.text.toString(), registerPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val db = FirebaseFirestore.getInstance()

                    val userID = auth.currentUser?.uid
                    val status = ""
                    val fullName = registerFullName.editableText.toString()
                    val age = registerAge.editableText.toString()
                    val emailId = registerEmailId.editableText.toString()
                    val phoneNumber = registerPhoneNumber.editableText.toString()

                    val userInfo = UserInfo(userID,status,fullName,age,emailId,phoneNumber)

                    db.collection("User Profiles").document(userID!!)
                        .set(userInfo).addOnSuccessListener {
                            Log.d("registerAddToFirestore", "signUpUserValidation: success")
                        }
                        .addOnFailureListener {
                            Log.d("registerAddToFirestore", "signUpUserValidation: failure")
                        }

                    Toast.makeText(baseContext, "User registered successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.Try again", Toast.LENGTH_SHORT).show()
                    registerProgressBar.visibility = View.INVISIBLE
                }
            }
    }
    //-------------------------------------------------------------------------------------------------------------------------
}