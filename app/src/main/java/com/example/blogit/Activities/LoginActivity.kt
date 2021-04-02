package com.example.blogit.Activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.blogit.R
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadRegisterPage()
        showPasswordCheckBox()

        loginForgetPassword.setOnClickListener {
            forgotPassword()
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun loadRegisterPage(){
        loginLoadRegisterNewUserPage.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            Toast.makeText(this, "Register Page Opened!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun showPasswordCheckBox() {
        showPasswordCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                loginPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance();
            } else {
                loginPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------
    private fun forgotPassword() {
        val dialogPlus = DialogPlus.newDialog(this)
            .setContentHolder(ViewHolder(R.layout.forgot_password_dialog)).create()

        val myView: View = dialogPlus.holderView
        val reset: Button = myView.findViewById(R.id.btn_reset)
        val close: Button = myView.findViewById(R.id.btn_close)
        val enteremail: EditText = myView.findViewById(R.id.enterEmail)

        dialogPlus.show()

        close.setOnClickListener {
            dialogPlus.dismiss()
        }

        reset.setOnClickListener {
            fun forgot(username : EditText) {
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
}