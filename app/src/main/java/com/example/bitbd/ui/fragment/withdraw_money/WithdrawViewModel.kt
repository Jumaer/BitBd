package com.example.bitbd.ui.fragment.withdraw_money

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.deposit.model.BaseDepositResponse
import com.example.bitbd.ui.fragment.withdraw_money.model.ResponseAccountInfo
import com.example.bitbd.ui.fragment.withdraw_money.model.ResponseWithdraw
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WithdrawViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is withdraw Fragment"
    }
    val text: LiveData<String> = _text



    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _withdraw = MutableLiveData<ResponseWithdraw>()
    val withdraw: LiveData<ResponseWithdraw>
        get() = _withdraw


    suspend fun withdraw(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getWithdraw()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _withdraw.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }


    private val _withdrawAccount = MutableLiveData<ResponseAccountInfo>()
    val withdrawAccount: LiveData<ResponseAccountInfo>
        get() = _withdrawAccount


    suspend fun withdrawAccountInfo(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getWithdrawAccountInfo()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _withdrawAccount.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }
}