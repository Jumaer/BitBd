package com.example.bitbd


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bitbd.databinding.ActivityLogInBinding
import com.example.bitbd.util.BitBDUtil

import kotlinx.coroutines.GlobalScope

import kotlinx.coroutines.launch

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            BitBDUtil.showProgress(this@LogInActivity)
        }
    }

}