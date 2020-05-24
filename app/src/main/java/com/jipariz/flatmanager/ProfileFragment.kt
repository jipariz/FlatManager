package com.jipariz.flatmanager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.jipariz.flatmanager.databinding.FragmentProfileBinding
import com.jipariz.flatmanager.login.LoginActivity
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private lateinit var binding: FragmentProfileBinding

    private val firebaseAuth: FirebaseAuth by inject()

    val model: MainViewModel by inject()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        binding.logoutButton.setOnClickListener {
            signOut()
        }

        Picasso.get()
            .load(firebaseAuth.currentUser?.photoUrl)
            .resize(500, 500)
            .centerCrop()
            .into(binding.profilePicture)
        binding.profileName.text = firebaseAuth.currentUser?.displayName
        binding.email.text = firebaseAuth.currentUser?.email

    }
    private fun signOut() {
        startActivity(activity?.applicationContext?.let { LoginActivity.getLaunchIntent(it) })
        FirebaseAuth.getInstance().signOut()
    }

}