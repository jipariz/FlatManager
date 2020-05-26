package com.jipariz.flatmanager

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.jipariz.flatmanager.databinding.FragmentHomeBinding
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.koin.android.ext.android.inject


class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager


    val model: MainViewModel by inject()

    private var myClipboard: ClipboardManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.liveState.observe(viewLifecycleOwner, Observer { renderState(it) })

        binding = FragmentHomeBinding.bind(view)
        linearLayoutManager = LinearLayoutManager(context)
        binding.leaveFlatButton.setOnClickListener {
            model.removeFlat()
        }

        binding.shareButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey join our flat with this code: " + binding.flatCode.text
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

        myClipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    }

    private fun renderState(it: PageState?) {
        binding.flatName.text = it?.flat?.name
        binding.flatCode.text = it?.flat?.flatId
        it?.flat?.flatId?.let { it1 -> generateQrCode(it1) }
    }


    private fun generateQrCode(flatId: String) {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix =
                multiFormatWriter.encode(flatId, BarcodeFormat.QR_CODE, 500, 500)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding.flatQrCode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

}
