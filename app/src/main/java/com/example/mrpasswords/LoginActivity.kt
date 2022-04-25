package com.example.mrpasswords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity(){

    private lateinit var email: EditText
    private lateinit var password : EditText
    private lateinit var signIn : Button
    private lateinit var createAccount : Button
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fgPass: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        email = findViewById(R.id.oemail)
        val uname= email.text
        password = findViewById(R.id.Pass)
        val pass = password.text

        //declare fAuth
        fAuth = Firebase.auth

        signIn = findViewById(R.id.login_button)
        signIn.setOnClickListener{
            if (uname.isEmpty() || pass.isEmpty()){
                Snackbar.make(it,"Please fill all details",Snackbar.LENGTH_SHORT).show()
            }
            else{
                fireBaseSignIn()
            }
        }

        try {
            createAccount = findViewById(R.id.creatAc)
            createAccount.setOnClickListener{
                val intent = Intent(this,CreateAccount::class.java)
                startActivity(intent)
            }
        }
        catch (e: Exception){
            Log.d("opopo", "error $e ")

        }

        fgPass = findViewById(R.id.fg_pass)
        fgPass.setOnClickListener{
            if (uname.isEmpty()){
                Snackbar.make(it,"Enter your Email to Reset your password",Snackbar.LENGTH_LONG).show()
            }
            else{
                forgetPassword()
            }
        }
    }

    private fun fireBaseSignIn(){
        signIn.isEnabled = false
        signIn.alpha = 0.5f
        fAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Sign in Successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                signIn.isEnabled = true
                signIn.alpha = 1.0f
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun forgetPassword(){
        fAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                Toast.makeText(this,"We have sent you an Email to reset your password!",Toast.LENGTH_LONG).show()
            }
            else{
//                Toast.makeText(this,"Failed to send reset Email!",Toast.LENGTH_LONG).show()
                Snackbar.make(window.decorView.rootView,"Make Sure Entered Email is Correct",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}