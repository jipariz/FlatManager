package com.jipariz.flatmanager

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class JoinFlatFragment : Fragment(R.layout.fragment_flat_choose) {
    private lateinit var binding: FragmentJoinFlatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_flat_choose,
            container, false
        )
        val button: Button = view.findViewById<View>(R.id.create_new_flat_button) as Button
        button.setOnClickListener(object : View.OnClickListener {
            fun onClick(v: View?) {
                // do something
            }
        })
        return inflater.inflate(R.layout.fragment_flat_choose, container, false)
    }
}