package com.example.bitbd.ui.activity.otp_verify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bitbd.R
import com.example.bitbd.databinding.ActivityOtpVerifyBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.login.LogInActivity
import com.example.bitbd.util.BitBDUtil


class OtpVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerifyBinding
    private lateinit var preferences: BitBDPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = BitBDPreferences(this)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_content) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.numberInputFragment,
                R.id.otpInputFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
        customBackPress()

    }
    private fun customBackPress(){
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    BitBDUtil.showAlertDialog(this@OtpVerifyActivity,
                        "Attention!",
                        "As you are not verified, the verification is must for you to login. " +
                                "Don't worry it is just an one time verification. Do you really want to leave?",
                        "Agree","Cancel",::performAction)
                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun performAction() {
        preferences.logOut()
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }
}