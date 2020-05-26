package com.jipariz.flatmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.jipariz.flatmanager.databinding.FragmentJoinFlatBinding
import com.jipariz.flatmanager.global.CaptureActivityPortrait
import kotlinx.android.synthetic.main.dialog_create_flat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class JoinFlatFragment : Fragment(R.layout.fragment_join_flat), CoroutineScope {
    private val job = Job()

    private lateinit var binding: FragmentJoinFlatBinding

    private val model: MainViewModel by inject()

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                joinFlat(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentJoinFlatBinding.bind(view)
        binding.joinFlatButton.setOnClickListener {
            joinFlat(binding.flatCodeEditText.text.toString())
        }
        binding.createNewFlatButton.setOnClickListener {
            createNewFlat()
        }
        binding.qrScan.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            integrator.setPrompt("Scan a QR code")
            integrator.setCameraId(0) // Use a specific camera of the device
            integrator.setOrientationLocked(true)
            integrator.captureActivity = CaptureActivityPortrait::class.java

            integrator.initiateScan()
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

    private fun joinFlat(flatId: String) {
        model.joinFlat(flatId)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}