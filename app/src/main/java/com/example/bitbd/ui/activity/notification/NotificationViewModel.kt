package com.example.bitbd.ui.activity.notification

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.activity.notification.model.NotificationResponse
import com.example.bitbd.ui.activity.notification.model.NotificationsBaseResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel : ViewModel() {
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _userNotifications = MutableLiveData<NotificationsBaseResponse>()
    val userNotifications: LiveData<NotificationsBaseResponse>
        get() = _userNotifications


    fun getAllNotifications(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getAllNotifications()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to log in", context)
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _userNotifications.value = response.body()
                _progress.value = false
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }
            }
        }

    }

}