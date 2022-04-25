package com.example.mrpasswords.ui.passwords

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mrpasswords.R
import com.example.mrpasswords.UserInfo
import com.example.mrpasswords.databinding.FragmentPasswordsBinding
import com.example.mrpasswords.ui.gpasswords.OpenPassActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class PasswordsFragment : Fragment(), PasswordsAdapter.OnItemListener {

    private var _binding: FragmentPasswordsBinding? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var userArrayList: ArrayList<UserInfo>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_passwords, container, false)

        firebaseAuth = Firebase.auth
//        val userId = firebaseAuth.currentUser?.uid

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = Firebase.auth
        val userId = firebaseAuth.currentUser?.uid

        emptyView = view.findViewById(R.id.emptyView)
        userRecyclerView = view.findViewById(R.id.recycler)
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        userArrayList = arrayListOf<UserInfo>()

        if (userId != null) {
            getUserDataFromFireBase(userId)
        }
    }

    private fun getUserDataFromFireBase(userId: String) {

        reference = FirebaseDatabase.getInstance("https://passwords-839fe-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference.child("userinfo").child(userId)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(UserInfo::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerView.adapter = PasswordsAdapter(userArrayList, this@PasswordsFragment)

                    //for visibility or invisibility of recycler view
                    if (userArrayList.isEmpty()){
                        userRecyclerView.visibility = View.GONE
                        emptyView.visibility = View.VISIBLE
                    }
                    else {
                        userRecyclerView.visibility = View.VISIBLE
                        emptyView.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something Went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(v: View?, position: Int) {
//        Toast.makeText(context, "position $position", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, OpenPassActivity::class.java)
        intent.putExtra("passwordName", userArrayList[position].passwordName)
        intent.putExtra("userName", userArrayList[position].userName)
        intent.putExtra("password", userArrayList[position].password)
        startActivity(intent)
    }

}