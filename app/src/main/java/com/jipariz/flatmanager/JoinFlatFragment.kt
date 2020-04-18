package com.jipariz.flatmanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jipariz.flatmanager.databinding.FragmentFlatChooseBinding


class JoinFlatFragment : Fragment(R.layout.fragment_flat_choose) {
    private lateinit var binding: FragmentFlatChooseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFlatChooseBinding.bind(view)
        binding.createNewFlatButton.setOnClickListener {
            createNewFlat()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createNewFlat() {
        Log.v("ahoj", "pepo")
        Toast.makeText(context, "AHAAA", Toast.LENGTH_LONG).show()
    }
}