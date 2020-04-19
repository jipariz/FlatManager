package com.jipariz.flatmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.jipariz.flatmanager.databinding.ActivityMainBinding
import com.jipariz.flatmanager.firebase.database.DatabaseService
import com.jipariz.flatmanager.global.hide
import com.jipariz.flatmanager.global.show
import com.jipariz.flatmanager.login.LoginActivity
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private val job = Job()

    private val databaseService: DatabaseService by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)





        launch {
            val user = databaseService.getUser()
            inicializeObservers()
            if(user?.flatId.isNullOrEmpty()){
                title="Join flat"
                loadFragment(JoinFlatFragment())
                binding.navigationView.hide()
            } else {
                databaseService.getFlat(user?.flatId ?: "")
                goToHomeFragment()
            }
        }



        binding.navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home-> {
                    title=resources.getString(R.string.home_title)
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_assignment-> {
                    title=resources.getString(R.string.assignment_title)
                    loadFragment(AssignmentFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_profile-> {
                    title=resources.getString(R.string.profile_title)
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }
    }

    private fun inicializeObservers(){
        databaseService.user.observe(this, Observer {
            if(it == null || FirebaseAuth.getInstance().currentUser?.isAnonymous != false){
                startActivity(LoginActivity.getLaunchIntent(applicationContext))
                FirebaseAuth.getInstance().signOut()
            }
        })
        databaseService.flat.observe(this, Observer {
            if(it != null){
                goToHomeFragment()
            }
        })
    }

    private fun goToHomeFragment(){
        title=resources.getString(R.string.home_title)
        loadFragment(HomeFragment())
        binding.navigationView.show()
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous != false) {
            startActivity(LoginActivity.getLaunchIntent(this))
            finish()
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}
