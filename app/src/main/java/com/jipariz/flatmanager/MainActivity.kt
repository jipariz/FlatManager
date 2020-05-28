package com.jipariz.flatmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.jipariz.flatmanager.databinding.ActivityMainBinding
import com.jipariz.flatmanager.global.hide
import com.jipariz.flatmanager.global.show
import com.jipariz.flatmanager.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private val job = Job()

    private val model: MainViewModel by inject()

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model.liveState.observe(this, Observer { renderState(it) })
        model.getData()

        model.liveState.observe(this, Observer { renderState(it) })

        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)
        supportActionBar?.hide()
        model.progressVisibility.observe(this, Observer {
            progress.visibility = it
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.bottom_menu, menu)
        binding.navigationView.setupWithNavController(menu!!, navController)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    private fun renderState(it: PageState?) {
        when (it?.flat) {
            null -> {
                navController.navigate(R.id.nav_join, null)

                binding.navigationView.hide()
            }
            else -> {
                if (navController.currentDestination?.id == R.id.nav_join) navController.navigate(
                    R.id.nav_home,
                    null
                )

                binding.navigationView.show()
            }
        }


    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish()
            exitProcess(0);
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
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
