package com.jipariz.flatmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.jipariz.flatmanager.databinding.FragmentProfileBinding
import com.jipariz.flatmanager.login.LoginActivity

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private lateinit var binding: FragmentProfileBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        binding.logoutButton.setOnClickListener {
            signOut()
        }
    }
    private fun signOut() {
        startActivity(activity?.applicationContext?.let { LoginActivity.getLaunchIntent(it) })
        FirebaseAuth.getInstance().signOut()
    }

}