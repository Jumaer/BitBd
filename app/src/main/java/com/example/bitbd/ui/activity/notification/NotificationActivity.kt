package com.example.bitbd.ui.activity.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.databinding.ActivityNotificationBinding
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch


class NotificationActivity : AppCompatActivity() {
    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.notice)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        getNotifications(viewModel)
    }

    private fun getNotifications(viewModel: NotificationViewModel) {
        var loading : LoadingProgress? = null

        viewModel.progress.observe(this){
            if(it != null){
                if(it) {
                    loading =  BitBDUtil.showProgress(this)
                }
                else loading?.dismiss()
            }
        }

        viewModel.userNotifications.observe(this){
            if(it != null){
                BitBDUtil.showMessage(it.message.toString(), this)
                loading?.dismiss()
            }
        }

        lifecycleScope.launch{
            viewModel.getAllNotifications(this@NotificationActivity)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}