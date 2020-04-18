package com.jipariz.flatmanager

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jipariz.flatmanager.databinding.FragmentJoinFlatBinding


class JoinFlatFragment : Fragment(R.layout.fragment_join_flat) {
    private lateinit var binding: FragmentJoinFlatBinding

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
        val editText = dialogContentView.findViewById<EditText>(R.id.editText)
        editText.inputType = InputType.TYPE_CLASS_TEXT


        var flatName: String
        android.app.AlertDialog.Builder(context)
            .setView(dialogContentView)
            .setPositiveButton(R.string.create) { _, _ ->
                // Accepted, now it has to be to be created
                flatName = editText.text.toString()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun joinFlat() {
        // TODO
        Toast.makeText(context, "Joining Flat", Toast.LENGTH_SHORT).show()
    }
}