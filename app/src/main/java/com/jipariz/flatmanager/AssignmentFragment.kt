package com.jipariz.flatmanager

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jipariz.flatmanager.databinding.FragmentAssignmentBinding
import com.jipariz.flatmanager.login.MyTimeLine
import kotlinx.android.synthetic.main.fragment_assignment.*
import me.jerryhanks.timelineview.IndicatorAdapter
import me.jerryhanks.timelineview.interfaces.TimeLineViewCallback
import me.jerryhanks.timelineview.model.Status
import org.koin.android.ext.android.inject


class AssignmentFragment : Fragment(R.layout.fragment_assignment) {

    val model: MainViewModel by inject()

    var adapter: IndicatorAdapter<MyTimeLine>? = null

    private lateinit var binding: FragmentAssignmentBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAssignmentBinding.bind(view)
        adapter = context?.let {
            IndicatorAdapter(mutableListOf(), it, object :
                TimeLineViewCallback<MyTimeLine> {
                override fun onBindView(model: MyTimeLine, container: FrameLayout, position: Int): View {
                    val lineView = layoutInflater
                        .inflate(R.layout.sample_time_line,
                            container, false)

                    (lineView.findViewById<TextView>(R.id.tv_title)).text = model.name
                    (lineView.findViewById<TextView>(R.id.tv_content)).text = "Uklid!"
                    return@onBindView lineView
                }
            })
        }


        adapter?.let { timelineView.setIndicatorAdapter(it) }
        renderState(model.liveState.value)
        model.liveState.observe(viewLifecycleOwner, Observer { renderState(it) })
    }




    private fun renderState(it: PageState?) {
        val timeLines = mutableListOf<MyTimeLine>().apply {
            it?.flat?.usersList?.map { it["userName"] ?: ""}?.forEach {name ->
                add(MyTimeLine(Status.UN_COMPLETED, name))
                add(MyTimeLine(Status.COMPLETED, name + "test"))
                add(MyTimeLine(Status.ATTENTION, name + "test2"))

            }
        }
        adapter?.swapItems(timeLines)


    }
}