package com.example.bitbd.ui.fragment.accounts

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitbd.constant.ERROR
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.constant.networkCall
import com.example.bitbd.ui.fragment.accounts.model.BaseAccountInformationViewResponse
import com.example.bitbd.ui.fragment.accounts.model.BaseResponseEditAccount
import com.example.bitbd.util.BitBDUtil
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Field

class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is account's Fragment"
    }
    val text: LiveData<String> = _text


    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress



    private val _accountViewObject = MutableLiveData<BaseAccountInformationViewResponse>()
    val accountViewObject: LiveData<BaseAccountInformationViewResponse>
        get() = _accountViewObject




    suspend fun getBaseAccountViewInfo(context:Context){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getAccountViewObject()
                }

            }  catch (e: Exception) {
                BitBDUtil.showMessage("Unable to show anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _accountViewObject.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }

    private val _accountSubmit = MutableLiveData<JsonObject>()
    val accountSubmit: LiveData<JsonObject>
        get() = _accountSubmit

    suspend fun submitToNew(context:Context,name: String,
                           account: String,
                           type: String,
                           status: String, branch : String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.submitForAddAccount(name, account, type, status,branch)
                }

            }  catch (e: Exception) {
                BitBDUtil.showMessage("Unable to submit anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _progress.value = false
                _accountSubmit.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }



    private val _accountDelete = MutableLiveData<JsonObject>()
    val accountDelete: LiveData<JsonObject>
        get() = _accountDelete

    suspend fun deleteAccount(context:Context,id:String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.deleteAccount(id)
                }

            }  catch (e: Exception) {
                BitBDUtil.showMessage("Unable to delete anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _progress.value = false
                _accountDelete.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }

    private val _accountEdit = MutableLiveData<BaseResponseEditAccount>()
    val accountEdit: LiveData<BaseResponseEditAccount>
        get() = _accountEdit

    suspend fun editAccountInformation(context:Context,id:String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.getEditAccountInformation(id)
                }

            }  catch (e: Exception) {
                BitBDUtil.showMessage("Unable to delete anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _progress.value = false
                _accountEdit.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }






    private val _submitEdit = MutableLiveData<JsonObject>()
    val submitEdit: LiveData<JsonObject>
        get() = _submitEdit

    suspend fun submitEditInfo(context:Context,name: String,
                               account: String,
                               type: String,
                               status: String, id:String ,  branch : String){
        _progress.value = true
        viewModelScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    networkCall(context)?.submitForEditAccount(name, account, type, status, branch, id)
                }

            }  catch (e: Exception) {
                BitBDUtil.showMessage("Unable to delete anything", ERROR)
                _progress.value = false
                return@launch
            }

            if (response?.isSuccessful == true && response.body() != null) {
                _progress.value = false
                _submitEdit.value = response.body()
            } else {
                _progress.value = false
                if (response?.code() != null) {
                    Log.d("doneValue :: ", response.message())
                }

            }
        }
    }


}