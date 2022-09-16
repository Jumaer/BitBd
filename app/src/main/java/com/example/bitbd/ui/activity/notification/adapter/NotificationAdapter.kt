package com.example.bitbd.ui.activity.notification.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.databinding.LayoutNotificationsItemBinding
import com.example.bitbd.ui.activity.notification.model.NotificationResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class NotificationAdapter(
    private val notificationList: List<NotificationResponse>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(
        val binding: LayoutNotificationsItemBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.baseItemNotification.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val binding = LayoutNotificationsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding, onItemClicked)
    }


    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
        with(holder) {
            with(notificationList[position]) {
                binding.baseTextNotification.text = this.message
            }
        }
    }

    override fun getItemCount() = notificationList.size
}