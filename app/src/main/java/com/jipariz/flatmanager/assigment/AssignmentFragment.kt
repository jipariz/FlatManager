package com.jipariz.flatmanager.assigment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.MainViewModel
import com.jipariz.flatmanager.PageState
import com.jipariz.flatmanager.R
import com.jipariz.flatmanager.databinding.AssignmentRecyclerItemFirstBinding
import com.jipariz.flatmanager.databinding.FragmentAssignmentBinding
import kotlinx.android.synthetic.main.assignment_recycler_item_first.*
import org.koin.android.ext.android.inject


class AssignmentFragment : Fragment(R.layout.fragment_assignment), UserAdapter.onItemClicListener {

    val model: MainViewModel by inject()

    private lateinit var binding: FragmentAssignmentBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: UserAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAssignmentBinding.bind(view)
        viewManager = LinearLayoutManager(view.context)
        model.liveState.value?.flat?.usersList?.map { it["userName"] ?: "" }?.let {
            viewAdapter = UserAdapter(it, model, this, this)

        }

        recyclerView = binding.assignmentRecycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        model.liveState.observe(viewLifecycleOwner, Observer { renderState(it) })

        model.buttonVisibility.observe(viewLifecycleOwner, Observer {
        })

        model.finishedVisibility.observe(viewLifecycleOwner, Observer {
        })






    }

    private fun renderState(it: PageState?) {
        binding.assignmentRecycler.layoutManager = viewManager
        viewAdapter = UserAdapter(it?.flat?.usersList?.map { it["userName"] ?: ""}?: emptyList(), model, this, this)
        binding.assignmentRecycler.adapter = viewAdapter


    }

    override fun onItemClick(position: Int) {
        Log.d("TAG", "onClick $position")
    }

    override fun onButtonClick(position: Int) {
        model.clean()
        Log.d("TAG", "onClick button")    }


}