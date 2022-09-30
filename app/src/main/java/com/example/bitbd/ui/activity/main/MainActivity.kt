package com.example.bitbd.ui.activity.main


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.bitbd.BuildConfig
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.databinding.ActivityMainBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.BaseActivity
import com.example.bitbd.ui.activity.accounts.AccountManagementActivity
import com.example.bitbd.ui.activity.login.LogInActivity
import com.example.bitbd.ui.activity.main.mainViewModel.MainViewModel
import com.example.bitbd.ui.activity.notification.NotificationActivity
import com.example.bitbd.util.BitBDUtil
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var preference: BitBDPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        preference = BitBDPreferences(this@MainActivity)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
          //  openChatView(binding)
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
                R.id.nav_transaction,
                R.id.nav_affiliate,
                R.id.nav_logOut
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setHeaderView(navView)


    }

    fun getNavView(): NavigationView {
        return binding.navView
    }

    fun setHeaderView(navView: NavigationView) {
        navView.getHeaderView(0).findViewById<TextView>(R.id.username).text =
            preference.getName().toString()

        val setMailNumberValue = if (preference.getMobileNumber()?.isNotEmpty() == true) {
            preference.getMobileNumber()
        } else if (preference.geEmail()?.isNotEmpty() == true) {
            preference.geEmail()
        } else {
            ""
        }
        val emailPhone = navView.getHeaderView(0).findViewById<TextView>(R.id.mobileOrEmail)
        if (setMailNumberValue == "") {
            emailPhone.visibility = View.GONE
        } else emailPhone.text = setMailNumberValue

        val profileImage = navView.getHeaderView(0).findViewById<ShapeableImageView>(R.id.imageView)
        val profileImageLoader =
            navView.getHeaderView(0).findViewById<LottieAnimationView>(R.id.animation_view)
        val urlProfileImage = BuildConfig.SERVER_URL + preference.getImageUrl()?.toString()
        BitBDUtil.loadImage(profileImage, profileImageLoader, urlProfileImage, this)



        if (preference.getAffiliate() == 1) {
            val item: MenuItem = navView.menu.getItem(5)
            item.isVisible = true
        }

        else {
            val item: MenuItem = navView.menu.getItem(5)
            item.isVisible = false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notice -> {
                // Action goes here
                startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
                true
            }
            R.id.action_settings -> {
                // Action goes here
                startActivity(Intent(this@MainActivity, AccountManagementActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logOutFromApplication(item: MenuItem) {

        BitBDUtil.showAlertDialog(this@MainActivity,
            "Attention!",
            "Do you really want to log out ?",
            "Agree","Cancel",::performLogOut)

    }

    private fun performLogOut() {
        var loading: LoadingProgress? = null
        viewModel.progressLogOut.observe(this) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(this@MainActivity)
                } else {
                    BitBDUtil.showMessage("Logout successfully", SUCCESS)
                    loading?.dismiss()
                    redirectToLogIn()
                }
            }
        }

        lifecycleScope.launch{
            viewModel.logOut(this@MainActivity)
        }
    }

    private fun redirectToLogIn() {
        preference.logOut()
        startActivity(Intent(this@MainActivity, LogInActivity::class.java))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openChatView(bind: ActivityMainBinding){
        bind.appBarMain.activityMainWebview.visibility = View.VISIBLE
        val webView = bind.appBarMain.mainWebview

        webView.settings.javaScriptEnabled = true;
        webView.settings.loadWithOverviewMode = true;
        webView.settings.useWideViewPort = true;
        webView.webViewClient = WebViewClient()

//        webView.loadData("<html><body>Hello, world!</body></html>",
//            "text/html", "UTF-8");

        webView.loadUrl("file:///android_asset/"+"message"+".html");

    }

        // If you deal with HTML then execute loadData instead of loadUrl
        //webView.loadData("YOUR HTML CONTENT","text/html", "UTF-8");

    }
