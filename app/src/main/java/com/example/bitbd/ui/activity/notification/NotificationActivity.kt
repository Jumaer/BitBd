package com.example.bitbd.ui.activity.notification

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.MESSAGE
import com.example.bitbd.constant.TIME_CREATED
import com.example.bitbd.constant.TIME_UPDATED
import com.example.bitbd.databinding.ActivityNotificationBinding
import com.example.bitbd.ui.activity.notification.adapter.NotificationAdapter
import com.example.bitbd.ui.activity.notification.model.NotificationResponse
import com.example.bitbd.ui.activity.notification.model.NotificationsBaseResponse
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
        createDisplayAdapterForNotifications()
    }


    lateinit var adapter : NotificationAdapter
    var notificationList: MutableList<NotificationResponse> = ArrayList()


    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayAdapterForNotifications() {
       adapter = NotificationAdapter(notificationList, :: onAdapterItemClick)
       binding.notificationsRecycle.layoutManager = LinearLayoutManager(this)
       binding.notificationsRecycle.adapter = adapter
       binding.notificationsRecycle.adapter?.notifyDataSetChanged()
    }



    private fun onAdapterItemClick(position: Int){
      val intent = Intent(this, NotificationDetailsActivity::class.java)
      intent.putExtra(MESSAGE, notificationList[position].message)
      intent.putExtra(TIME_CREATED, notificationList[position].createdAt)
      intent.putExtra(TIME_UPDATED, notificationList[position].updatedAt)
      startActivity(intent)
    }


    @SuppressLint("NotifyDataSetChanged")
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
                val baseResponse  = it.data ?: return@observe
                notificationList.clear()
                for(element in baseResponse) {
                   notificationList.add(element!!)
                }
                adapter.notifyDataSetChanged()
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