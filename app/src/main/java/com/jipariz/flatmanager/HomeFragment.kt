package com.jipariz.flatmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jipariz.flatmanager.databinding.FragmentHomeBinding
import com.jipariz.flatmanager.firebase.database.DatabaseService
import kotlinx.android.synthetic.main.users_recycler_item.view.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UserListAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseService : DatabaseService = get()

        binding = FragmentHomeBinding.bind(view)
        linearLayoutManager = LinearLayoutManager(context)
        binding.userList.layoutManager = linearLayoutManager
        binding.flatName.text = databaseService.flat.value?.name
        binding.flatCode.text = databaseService.flat.value?.flatId

        launch {
            val userList = databaseService.getUserNames()
            adapter = UserListAdapter(userList)
            binding.userList.adapter = adapter

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