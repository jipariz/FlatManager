package com.jipariz.flatmanager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.jipariz.flatmanager.databinding.FragmentHomeBinding
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.users_recycler_item.view.*
import org.koin.android.ext.android.inject


class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UserListAdapter


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

        myClipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    }

    private fun renderState(it: PageState?) {
        binding.userList.layoutManager = linearLayoutManager
        binding.flatName.text = it?.flat?.name
        binding.flatCode.text = it?.flat?.flatId
        adapter = UserListAdapter(it?.flat?.usersList?.map { it["userName"] ?: ""})
        binding.userList.adapter = adapter
        it?.flat?.flatId?.let { it1 -> generateQrCode(it1) }
    }

    fun copyText(view: View) {
        ClipData.newPlainText("flat code", binding.flatCode.text).let {
            myClipboard?.setPrimaryClip(it)
        }

        Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
    }

    fun generateQrCode(flatId: String) {
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

class UserListAdapter(private val itemList: List<String>?) :
    RecyclerView.Adapter<ListItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.users_recycler_item, parent, false)

        return ListItemHolder(itemView)
    }


    override fun getItemCount(): Int = itemList?.size ?: 0


    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let { holder.bind(it) }
    }
}

class ListItemHolder(v: View) : RecyclerView.ViewHolder(v){

    private var view: View = v



    fun bind(item: String) {
        view.name_title.text = item
    }




}