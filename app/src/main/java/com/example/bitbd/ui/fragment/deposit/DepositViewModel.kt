package com.example.bitbd.ui.fragment.deposit

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.deposit.model.BaseDepositResponse
import com.example.bitbd.ui.fragment.deposit.model.DepositSubmit
import com.example.bitbd.ui.fragment.deposit.model.GetPaymentBaseResponse
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class DepositViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is deposit Fragment"
    }
    val text: LiveData<String> = _text


    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress


    private val _deposit = MutableLiveData<BaseDepositResponse>()
    val deposit: LiveData<BaseDepositResponse>
        get() = _deposit


    suspend fun deposit(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.deposit()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _deposit.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }

    private val _payment = MutableLiveData<GetPaymentBaseResponse>()
    val payment: LiveData<GetPaymentBaseResponse>
        get() = _payment

    suspend fun getPaymentInfo(context: Context) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getPaymentInfo()
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to get payment info", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _payment.value = response.body()

            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                    BitBDUtil.showMessage(response.message(), context)
                }
            }
        }
    }



    private val _depositDelete = MutableLiveData<JsonObject>()
    val depositDelete: LiveData<JsonObject>
        get() = _depositDelete

    suspend fun deleteDeposit(context: Context, id : String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.deleteDeposit(id)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Unable to delete anything", context)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _depositDelete.value = response.body()
                 BitBDUtil.showMessage("Refreshing list", context)
                 deposit(context)
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }
                deposit(context)
            }
        }
    }



}