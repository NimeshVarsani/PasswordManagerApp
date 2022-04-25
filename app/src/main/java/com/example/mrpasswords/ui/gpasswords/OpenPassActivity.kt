package com.example.mrpasswords.ui.gpasswords

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.mrpasswords.R

class OpenPassActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_pass)

        val passwordNameText: TextView = findViewById(R.id.pName)
        val usernameText: TextView = findViewById(R.id.uName)
        val passwordText: TextView = findViewById(R.id.yPass)

        val bundle: Bundle? = intent.extras
        val passwordName = bundle?.getString("passwordName")
        val username = bundle?.getString("userName")
        val password = bundle?.getString("password")

        passwordNameText.text = passwordName
        usernameText.text = username
        passwordText.text = password

        val imageButton: ImageButton = findViewById(R.id.copyPass)
        imageButton.setOnClickListener{
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clipData =  ClipData.newPlainText("pass", password)
            clipboard?.setPrimaryClip(clipData)
            Toast.makeText(this,"Copied", Toast.LENGTH_SHORT).show()
        }
    }
}