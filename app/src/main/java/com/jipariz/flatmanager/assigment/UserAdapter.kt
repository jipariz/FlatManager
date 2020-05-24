package com.jipariz.flatmanager.assigment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.MainViewModel
import com.jipariz.flatmanager.R

class UserAdapter(
    private val users: List<String>,
    val model: MainViewModel,
    var mListener: onItemClicListener,
    var lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<UserAdapter.UserHolder>() {


     interface  onItemClicListener {
         fun onItemClick(position: Int)
        fun onButtonClick(position: Int)
    }



    class UserHolder(v: View, val listener: onItemClicListener) : RecyclerView.ViewHolder(v){

        var cleanButton: Button? = v.findViewById(R.id.clean_button)



        init {
            v.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            cleanButton?.setOnClickListener {
                if(adapterPosition == 0) listener.onButtonClick(adapterPosition )
            }

        }

    }


    override fun getItemViewType(position: Int): Int {
        // To differ first and others items in recycler
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        // create a new view
        val inflater = LayoutInflater.from(parent.context)
        val view: View


        when(viewType) {
            0 -> {
                view = inflater.inflate(R.layout.assignment_recycler_item_first, parent, false)
                view.findViewById<TextView>(R.id.week_view).text = "This week turn"

                model.buttonVisibility.observe(lifecycleOwner, Observer {
                    view.findViewById<Button>(R.id.clean_button).visibility = it
                })

                model.finishedVisibility.observe(lifecycleOwner, Observer {
                    view.findViewById<ImageView>(R.id.clean_done).visibility = it
                })

                model.pendingVisibility.observe(lifecycleOwner, Observer {
                    view.findViewById<ImageView>(R.id.clean_pending).visibility = it
                })


            }
            1 -> {
                view = inflater.inflate(R.layout.assignment_recycler_item_next, parent, false)
                view.findViewById<TextView>(R.id.week_view).text = "Next week"


            }
            else -> {
                view = inflater.inflate(R.layout.assignment_recycler_item, parent, false)
            }

        }
        view.findViewById<TextView>(R.id.user_name).text = users[viewType]
        return UserHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {}
}