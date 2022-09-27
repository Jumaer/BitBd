package com.example.bitbd.ui.fragment.withdraw_money

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.deposit.model.BaseDepositResponse
import com.example.bitbd.ui.fragment.withdraw_money.model.ResponseAccountInfo
import com.example.bitbd.ui.fragment.withdraw_money.model.ResponseWithdraw
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Field

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
                BitBDUtil.showMessage("Unable to show anything", ERROR)
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
                BitBDUtil.showMessage("Unable to show anything", ERROR)
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







    private val _withdrawStore = MutableLiveData<JsonObject>()
    val withdrawStore: LiveData<JsonObject>
        get() = _withdrawStore


    suspend fun withdrawSubmit(context: Context, account: String,
                               amount: String,
                               type: String) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.submitForWithdraw(account, amount, type)
                }

            } catch (e: Exception) {
                 BitBDUtil.showMessage("Something wrong", ERROR)
                _progress.value = false
                 return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _withdrawStore.value = response.body()
                BitBDUtil.showMessage("Successfully Submitted withdraw", SUCCESS)
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }



    private val _deleteWithdraw = MutableLiveData<JsonObject>()
    val deleteWithdraw: LiveData<JsonObject>
        get() = _deleteWithdraw


    suspend fun withdrawItemDelete(context: Context, id : String) {
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.deleteItemWithdraw(id)
                }

            } catch (e: Exception) {
                BitBDUtil.showMessage("Something wrong", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _deleteWithdraw.value = response.body()
                BitBDUtil.showMessage("Successfully delete the withdraw", SUCCESS)
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }
}