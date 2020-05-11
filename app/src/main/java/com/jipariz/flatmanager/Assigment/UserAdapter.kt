package com.jipariz.flatmanager.Assigment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.R
import com.jipariz.flatmanager.firebase.database.User
import kotlinx.android.synthetic.main.assignment_recycler_item_next.view.*

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    class UserHolder(v: View) : RecyclerView.ViewHolder(v) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.assignment_recycler_item_next, parent, false) as View
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        if (position == 0) {
            holder.itemView.findViewById<TextView>(R.id.week_view).text = "Now its your turn!"
        } else {
            holder.itemView.findViewById<TextView>(R.id.week_view).text = "$position. Week"
        }
        holder.itemView.findViewById<TextView>(R.id.user_name).text = users[position].name
    }
}