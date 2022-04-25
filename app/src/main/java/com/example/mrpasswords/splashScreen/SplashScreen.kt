package com.example.mrpasswords.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mrpasswords.HomeActivity
import com.example.mrpasswords.LoginActivity
import com.example.mrpasswords.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        firebaseAuth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            val user = firebaseAuth.currentUser
            if (user!= null){
                val intent= Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },800)
    }
}