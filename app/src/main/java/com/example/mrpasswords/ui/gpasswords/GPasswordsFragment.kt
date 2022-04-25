package com.example.mrpasswords.ui.gpasswords

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mrpasswords.R
import com.example.mrpasswords.databinding.FragmentGpasswordsBinding
import java.lang.StringBuilder
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


class GPasswordsFragment : Fragment() {

    private lateinit var textPassword: TextView

    private var _binding: FragmentGpasswordsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_gpasswords, container, false)

        return view
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ownPass: Button = view.findViewById(R.id.createYourOwn)
        ownPass.setOnClickListener{
            val intent = Intent(activity, CreatePassword::class.java)
            startActivity(intent)
        }


        val seekBar: SeekBar = view.findViewById(R.id.seekBar)
        seekBar.min = 4
        seekBar.max = 50

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val textLength: TextView = view.findViewById(R.id.text_length)
                textLength.text = "Password Length is $progress "

                val password = generatePassword(progress)
                textPassword = view.findViewById(R.id.text_password)
                textPassword.text = password

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                Toast.makeText(context,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                Toast.makeText(context,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })


        //passing the text password as an intent
        val createPass: Button = view.findViewById(R.id.createPass)
        createPass.setOnClickListener{
            try {
                val intent = Intent(activity, CreatePassword::class.java)
                intent.putExtra("pass", textPassword.text.toString())
                startActivity(intent)
            }
            catch (e: Exception){
                Snackbar.make(it,"Change the seekbar to set the password", Snackbar.LENGTH_SHORT).show()
            }
        }

        //for coping the password to clipboard
        val imageButton: ImageButton = view.findViewById(R.id.copy)
        imageButton.setOnClickListener{
            if(textPassword.text.isEmpty()){
                val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clipData =  ClipData.newPlainText("pass", textPassword.text.toString())
                clipboard?.setPrimaryClip(clipData)
                Toast.makeText(context,"Copied",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Please Generate Password",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generatePassword(length: Int = 12) : String{

        val characters = "abcdefghjiklmnopqrstuwvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()=?"

        val sb = StringBuilder(length)

        for (x in 0 until length){
            val random = (characters.indices).random()
            sb.append(characters[random])
        }
//        sb.insert((0 until length).random())
        return sb.toString()
    }
}