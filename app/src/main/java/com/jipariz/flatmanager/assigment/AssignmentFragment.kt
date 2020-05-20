package com.jipariz.flatmanager.assigment

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
        model.liveState.value?.flat?.usersList?.map { it["userName"] ?: "" }?.let {
            viewAdapter = UserAdapter(it)

        }
        recyclerView = view.findViewById<RecyclerView>(R.id.assignment_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}