package com.example.blogit.Activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.blogit.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loadRegisterPage()
        showPasswordCheckBox()

        loginForgetPassword.setOnClickListener {
            forgotPassword()
        }

        loginSignInButton.setOnClickListener {
            loginUserValidation()
        }

        loginGoogleSignIn.setOnClickListener {
            googleSignIn()
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    private fun loadRegisterPage() {
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
                loginPasswordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance();
            } else {
                loginPasswordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance();
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
            fun forgot(username: EditText) {
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    private fun loginUserValidation() {

        if (loginEmailEditText.text.toString().isEmpty()) {
            loginEmailEditText.error = "Please enter Email"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(loginEmailEditText.text.toString()).matches()) {
            loginEmailEditText.error = "Please enter Valid Email"
            loginEmailEditText.requestFocus()
            return
        }
        if (loginPasswordEditText.text.toString().isEmpty()) {
            loginPasswordEditText.error = "Please enter Password"
            loginPasswordEditText.requestFocus()
            return
        }
        if (loginPasswordEditText.length() < 6) {
            loginPasswordEditText.error = "Password must be > 6 characters"
            return
        }

        loginProgressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(
            loginEmailEditText.text.toString(),
            loginPasswordEditText.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Wrong email or password.", Toast.LENGTH_SHORT)
                        .show()
                    loginProgressBar.visibility = View.INVISIBLE
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {                //if user is already logged in then we get user details in 'currentUser' otherwise 'null'
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser:FirebaseUser? = auth.currentUser
        // updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            Toast.makeText(baseContext, "Login Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, TabbedActivity::class.java))
            finish()
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    loginProgressBar.visibility = View.VISIBLE
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    Toast.makeText(baseContext, "Login Successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, TabbedActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }
    //-------------------------------------------------------------------------------------------------------------------------
}
