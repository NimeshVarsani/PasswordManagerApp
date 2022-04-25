package com.example.mrpasswords.ui.passwords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mrpasswords.R


class PasswordsAdapter(private val userList: ArrayList<com.example.mrpasswords.UserInfo>, private val listener: OnItemListener) : RecyclerView.Adapter<PasswordsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordsAdapter.ViewHolder, position: Int) {
        val currentItem = userList[position]
//        holder.userName.text = currentItem.userName
//        holder.pass.text = currentItem.password
        holder.passName.text = currentItem.passwordName
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

//        val userName: TextView = itemView.findViewById(R.id.dispUName)
//        val pass: TextView = itemView.findViewById(R.id.dispPass)
        val passName: TextView = itemView.findViewById(R.id.dispPassName)
//        val deleteButton: ImageButton = itemView.findViewById(R.id.del)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = this.layoutPosition
            listener.onItemClick(v, position)
        }
    }

    interface OnItemListener {
        fun onItemClick(v: View?, position: Int)
    }
}