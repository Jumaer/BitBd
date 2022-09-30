package com.example.bitbd.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.emrekotun.toast.CpmToast.Companion.toastError
import com.emrekotun.toast.CpmToast.Companion.toastInfo
import com.emrekotun.toast.CpmToast.Companion.toastSuccess
import com.emrekotun.toast.CpmToast.Companion.toastWarning
import com.example.bitbd.constant.ERROR
import com.example.bitbd.util.BitBDUtil
import com.example.bitbd.util.UserToastCommunicator

abstract class BaseActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayToast()
        if(!BitBDUtil.isNetworkAvailable(this)){
            BitBDUtil.showMessage("No Internet Connection", ERROR)
        }
    }

    private fun displayToast(){
        BitBDUtil.displayMessageFromUi(object : UserToastCommunicator {
            override fun displayErrorMessage(message: String) {
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {toastError(message)})

            }

            override fun displayInfoMessage(message: String) {
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {toastInfo(message)})

            }

            override fun displaySuccessMessage(message: String) {
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {toastSuccess(message)})

            }

            override fun displayWarningMessage(message: String) {
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {toastWarning(message)})

            }

        })
    }


}