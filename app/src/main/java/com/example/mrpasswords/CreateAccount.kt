package com.example.mrpasswords

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CreateAccount : AppCompatActivity() {

    private lateinit var nuname: EditText
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var cnpass: EditText
    private lateinit var create: Button
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fdata: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        //for setting a title as "create" account at action bar
        supportActionBar?.title = "Create Account"

        nuname = findViewById(R.id.nuname)
        email = findViewById(R.id.email)
        pass = findViewById(R.id.npass)
        cnpass = findViewById(R.id.cnpass)

        val nUsername = nuname.text
        val eMail = email.text
        val password = pass.text
        val confirmPass = cnpass.text

        fAuth = Firebase.auth

        create = findViewById(R.id.create)
        create.setOnClickListener{
            if (validateAll(nUsername,eMail,password,confirmPass)){
                if(pass.text.toString() == cnpass.text.toString()) {

                    //To store the username and email
                    val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
//                    editor.putString("username", nUsername.toString())
                    editor.putString("email", eMail.toString())
                    editor.apply()

                    //register the user in firebase
                    fireBaseSignUp(nuname.text.toString(), email.text.toString())

                }
                else{
                    cnpass.error = "Enter the same password"
                }
            }
            else{
                Snackbar.make(it,"Please fill all details", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateAll(
        nUsername: Editable,
        eMail: Editable,
        password: Editable,
        conformPass: Editable

    ) :Boolean {

        if (nUsername.isEmpty()) {
            nuname.error = "Field can't be empty"
            return false
        }
        else if (eMail.isEmpty()) {
            email.error = "Field can't be empty"
            return false
        }
        else if (password.isEmpty()) {
            pass.error = "Field can't be empty"
            return false
        }
        else if (conformPass.isEmpty()) {
            cnpass.error = "Field can't be empty"
            return false
        }
        else{
            return true
        }
    }

    private fun fireBaseSignUp(name: String, email: String){
        create.isEnabled = false
        create.alpha = 0.5f
        fAuth.createUserWithEmailAndPassword(email,pass.text.toString()).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                addUserToDatabase(name, email, fAuth.currentUser?.uid!!)
                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                create.isEnabled = true
                create.alpha = 1.0f
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        FirebaseApp.initializeApp(this)
    }

    private fun addUserToDatabase(name: String, email: String, uid: String){
        fdata = FirebaseDatabase.getInstance("https://passwords-839fe-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
        fdata.child("users").child(uid).setValue(Users(name, email, uid))
    }
}