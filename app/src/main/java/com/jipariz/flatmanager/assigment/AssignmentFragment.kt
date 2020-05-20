package com.jipariz.flatmanager.assigment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.MainViewModel
import com.jipariz.flatmanager.R
import com.jipariz.flatmanager.databinding.FragmentAssignmentBinding
import org.koin.android.ext.android.inject


class AssignmentFragment : Fragment(R.layout.fragment_assignment) {

    val model: MainViewModel by inject()

    private lateinit var binding: FragmentAssignmentBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAssignmentBinding.bind(view)
        viewManager = LinearLayoutManager(view.context)
        model.liveState.value?.flat?.usersList?.map { it["userName"] ?: "" }?.let {
            viewAdapter = UserAdapter(it)

        }
        recyclerView = binding.assignmentRecycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }




//    private fun renderState(it: PageState?) {
//        val timeLines = mutableListOf<MyTimeLine>().apply {
//            it?.flat?.usersList?.map { it["userName"] ?: ""}?.forEach {name ->
//                add(MyTimeLine(Status.UN_COMPLETED, name))
//                add(MyTimeLine(Status.COMPLETED, name + "test"))
//                add(MyTimeLine(Status.ATTENTION, name + "test2"))
//
//            }
//        }
//        adapter?.swapItems(timeLines)
//
//
//    }
}