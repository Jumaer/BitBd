package com.example.bitbd.ui.activity.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.util.RunTimeValue
import com.example.bitbd.databinding.ActivityLogInBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.main.MainActivity
import com.example.bitbd.ui.activity.login.model.UserLogIn
import com.example.bitbd.util.BitBDUtil

class LogInActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LogInViewModel
    private lateinit var binding: ActivityLogInBinding
    private lateinit var preference: BitBDPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this)[LogInViewModel::class.java]
        preference = BitBDPreferences(this@LogInActivity)
        dataObserver()

        binding.loginButton.setOnClickListener {
            logInSubmit()
        }

        if(preference.getAuthToken()?.isEmpty() == false){
            moveToNextMainPage()
        }



    }

    private fun logInSubmit() {

        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: java.lang.Exception) {

        }
        val username = binding.usernameLayout.editText?.text.toString()
        val password = binding.passwordLayout.editText?.text.toString()

        if (username.isEmpty()) {
            binding.usernameLayout.error = getString(R.string.this_field_is_required)
            return
        }
        binding.usernameLayout.error = null

        if (password.isEmpty()) {
            binding.passwordLayout.error = getString(R.string.this_field_is_required)
            return
        }
        binding.passwordLayout.error = null
        loginViewModel.login(UserLogIn(username, password = password),this@LogInActivity)
    }


    private fun dataObserver(){

        var loading : LoadingProgress? = null

        loginViewModel.progress.observe(this){
            if(it != null){
                if(it) {
                   loading = BitBDUtil.showProgress(this@LogInActivity)
                }
                else{
                    loading?.dismiss()

                }
            }
        }

        loginViewModel.userLogin.observe(this){
            if(it != null){
                RunTimeValue.logInResponse = it
                it.authorisation?.token
                preference.putAuthToken(it.authorisation?.token)
                it.user?.name?.let { it1 -> preference.putName(it1) }
                it.user?.mobile?.let { it1 -> preference.putMobileNumber(it1) }
                loading?.dismiss()
                moveToNextMainPage()
            }
        }
    }

    private fun moveToNextMainPage() {
        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}