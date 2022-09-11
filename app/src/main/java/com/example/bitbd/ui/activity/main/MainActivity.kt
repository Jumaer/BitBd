package com.example.bitbd.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.databinding.ActivityMainBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.login.LogInActivity
import com.example.bitbd.ui.activity.login.LogInViewModel
import com.example.bitbd.ui.activity.main.mainViewModel.MainViewModel
import com.example.bitbd.util.BitBDUtil

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var preference : BitBDPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        preference = BitBDPreferences(this@MainActivity)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your chatting action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_profile,
                R.id.nav_deposit,
                R.id.nav_withdraw,
                R.id.nav_transaction
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logOutFromApplication(item: MenuItem) {
        var loading : LoadingProgress? = null

        viewModel.progress.observe(this){
            if(it != null){
                if(it) {
                  loading =  BitBDUtil.showProgress(this@MainActivity)
                }
                else loading?.dismiss()
            }
        }

        viewModel.userLogOut.observe(this){
            if(it != null){
                BitBDUtil.showMessage(it.get("message").toString(), this@MainActivity)
                loading?.dismiss()
                redirectToLogIn()
            }
        }

        viewModel.logOut(this@MainActivity)
    }

    private fun redirectToLogIn() {
        preference.logOut()
        startActivity(Intent(this@MainActivity,LogInActivity::class.java))
        finish()
    }
}