package com.example.mrpasswords

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mrpasswords.databinding.ActivityHomeBinding
import com.example.mrpasswords.ui.gpasswords.CreatePassword
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    //to use fire base , to check teh user is logged in or not
    //Declare an instance of FirebaseAuth.
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = Firebase.auth
        reference = FirebaseDatabase.getInstance("https://passwords-839fe-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        // Intent to create Password activity through fab
        binding.appBarHome.fab.setOnClickListener {
            val intent = Intent(this, CreatePassword::class.java)
            startActivity(intent)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_GPasswords
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        reference.child("users").child(firebaseAuth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userName = snapshot.child("name").getValue().toString()
                val userEmail = snapshot.child("email").getValue().toString()

                val navigationView: NavigationView = findViewById(R.id.nav_view)
                val headerView: View = navigationView.getHeaderView(0)
                val navUserName: TextView = headerView.findViewById(R.id.nav_name)
                val navUserEmail: TextView = headerView.findViewById(R.id.nav_email)
                navUserName.setText("Hi, $userName")
                navUserEmail.text = userEmail
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something Went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.action_settings){
            // execute when setting clicked
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            return true
        }
        if(item.itemId == R.id.action_logout){
           firebaseAuth.signOut()
            showLogoutPopUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        FirebaseApp.initializeApp(this)
    }

    private fun showLogoutPopUp(){
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setMessage("Are you Sure?").setPositiveButton("Logout",DialogInterface.OnClickListener{
            dialog, id-> logout()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        }).setNegativeButton("Cancel",null)

        val alertDialog: AlertDialog = alert.create()
        alertDialog.show()
    }

    private fun logout(){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}