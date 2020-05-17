package com.jipariz.flatmanager.assigment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.R
import com.jipariz.flatmanager.firebase.database.User

class UserAdapter(private val users: List<String>) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    class UserHolder(v: View) : RecyclerView.ViewHolder(v) {
    }

    override fun getItemViewType(position: Int): Int {
        // To differ first and others items in recycler
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        // create a new view
        val inflater = LayoutInflater.from(parent.context)
        val view: View

        view = when(viewType) {
            0 -> inflater.inflate(R.layout.assignment_recycler_item_first, parent, false)
            1 -> inflater.inflate(R.layout.assignment_recycler_item_next, parent, false)
            else -> inflater.inflate(R.layout.assignment_recycler_item, parent, false)

        }
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        when(position) {
            0 -> {
                holder.itemView.findViewById<TextView>(R.id.week_view).text = "This week turn"
            }
            1 -> {
                holder.itemView.findViewById<TextView>(R.id.week_view).text = "Next week"
            }
        }
        holder.itemView.findViewById<TextView>(R.id.user_name).text = users[position]
    }
}