package com.jipariz.flatmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jipariz.flatmanager.databinding.ActivityMainBinding
import com.jipariz.flatmanager.global.hide
import com.jipariz.flatmanager.global.show
import com.jipariz.flatmanager.login.LoginActivity
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private val job = Job()

    val model: MainViewModel by inject()

    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.liveState.observe(this, Observer { renderState(it) })
        model.getData()

        model.liveState.observe(this, Observer { renderState(it) })

        navController = findNavController(R.id.nav_host_fragment)
        supportActionBar?.hide()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu,menu)
        binding.navigationView.setupWithNavController(menu!!,navController)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    private fun renderState(it: PageState?) {
        when {
            it?.flat == null -> {
                navController.navigate(R.id.navigation_join,null)
//                navController.navigate(R.id.navigation_join,null, NavOptions.Builder().setEnterAnim(R.anim.enter_from_left).setExitAnim(R.anim.exit_to_left).build())

                //loadFragment(JoinFlatFragment())
                binding.navigationView.hide()
            }
            else -> {
                navController.navigate(R.id.navigation_home,null)

                binding.navigationView.show()
            }
        }


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
