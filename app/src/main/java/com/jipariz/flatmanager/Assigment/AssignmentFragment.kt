package com.jipariz.flatmanager.Assigment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.MainViewModel
import com.jipariz.flatmanager.R
import com.jipariz.flatmanager.firebase.database.User
import org.koin.androidx.viewmodel.ext.android.viewModel

class AssignmentFragment : Fragment() {
    val model by viewModel<MainViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val dummyUsers = listOf<User>(
        User(null, "Pepa", null),
        User(null, "Filip", null),
        User(null, "Serzant Stistko", null),
        User(null, "Genius", null),
        User(null, "Vitas", null),
        User(null, "Tester", null)
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmnet_assignment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(view.context)
        viewAdapter = UserAdapter(dummyUsers)
        recyclerView = view.findViewById<RecyclerView>(R.id.assignment_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}