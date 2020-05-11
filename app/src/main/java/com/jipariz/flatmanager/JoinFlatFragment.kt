package com.jipariz.flatmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jipariz.flatmanager.databinding.FragmentJoinFlatBinding
import kotlinx.android.synthetic.main.dialog_create_flat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class JoinFlatFragment : Fragment(R.layout.fragment_join_flat), CoroutineScope {
    private val job = Job()

    private lateinit var binding: FragmentJoinFlatBinding

    val model: MainViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentJoinFlatBinding.bind(view)
        binding.joinFlatButton.setOnClickListener {
            joinFlat()
        }
        binding.createNewFlatButton.setOnClickListener {
            createNewFlat()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createNewFlat() {
        val dialogContentView = View.inflate(context, R.layout.dialog_create_flat, null)


        android.app.AlertDialog.Builder(context)
            .setView(dialogContentView)
            .setPositiveButton(R.string.create) { _, _ ->
                model.createNewFlat(dialogContentView.create_flat_dialog_edit_text.text.toString())

            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun joinFlat() {
        // TODO
        Toast.makeText(context, "Joining Flat", Toast.LENGTH_SHORT).show()
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}