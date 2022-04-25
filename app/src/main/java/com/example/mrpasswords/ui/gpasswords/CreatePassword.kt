package com.example.mrpasswords.ui.gpasswords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.mrpasswords.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class CreatePassword : AppCompatActivity() {

    private lateinit var passName: EditText
    private lateinit var usName: EditText
    private lateinit var yourPass: EditText
    private lateinit var saveButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_password)

        firebaseAuth = Firebase.auth

        //user id
        val userId = firebaseAuth.currentUser?.uid

        passName = findViewById(R.id.passName)
        usName = findViewById(R.id.userName)
        yourPass = findViewById(R.id.yourPass)

        //getting textPassword from intent
        val pass = intent.getStringExtra("pass")
        yourPass.setText(pass)

        val passwordName = passName.text
        val userName = usName.text
        val yourPassword = yourPass.text

//        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
//            .get(UserPdViewModel::class.java)

        saveButton = findViewById(R.id.save)
        saveButton.setOnClickListener{
            if (passwordName.isEmpty() || userName.isEmpty() || yourPassword.isEmpty()){
                Snackbar.make(it,"Please fill all details", Snackbar.LENGTH_SHORT).show()
            }
            else{
                if (userId != null) {
                    writeInDatabase(userId, passwordName.toString(), userName.toString(), yourPassword.toString())
                    finish()
                }
            }
        }
    }

    private fun writeInDatabase(userId: String, passwordName: String, userName: String, yourPassword: String){

        database = FirebaseDatabase.getInstance("https://passwords-839fe-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("userinfo")
        reference.child(userId).child(passwordName).child("userName").setValue(userName)
        reference.child(userId).child(passwordName).child("password").setValue(yourPassword)
        reference.child(userId).child(passwordName).child("passwordName").setValue(passwordName)

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }

}