package com.example.bitbd.util
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.*
import com.example.bitbd.ui.fragment.accounts.model.AccountViewObject
import com.example.bitbd.ui.fragment.affiliate.model.AffiliateObject
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.ui.fragment.profile.ProfileFragment
import com.example.bitbd.ui.fragment.transaction.model.TransactionObject
import com.example.bitbd.ui.fragment.withdraw_money.model.BaseResponseWithdraw
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
object BitBDUtil {

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    fun showMessage( message : String , messageType : String){
        if(messageType == WARNING)
        objectUserMsg?.displayWarningMessage(message)
        if(messageType == ERROR)
        objectUserMsg?.displayErrorMessage(message)
        if(messageType == INFO)
        objectUserMsg?.displayInfoMessage(message)
        if(messageType == SUCCESS)
        objectUserMsg?.displaySuccessMessage(message)
    }

    var objectUserMsg : UserToastCommunicator? = null

    fun displayMessageFromUi(objectUserMsg : UserToastCommunicator){
        this.objectUserMsg = objectUserMsg
    }


    private var loadingProgress : LoadingProgress? = null
    fun showProgress(context: Context) : LoadingProgress{
        loadingProgress = LoadingProgress(context)
        loadingProgress?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingProgress!!.show()

        return loadingProgress as LoadingProgress
    }


    fun editable (value : String): Editable? {
      return  Editable.Factory.getInstance().newEditable(value)
    }

    fun loadImage(profileImageView: ShapeableImageView, profileImageLoader: LottieAnimationView, urlProfileImage: String, context: Context) {

        Glide.with(context)
            .load(urlProfileImage)
            .placeholder(R.drawable.ic_baseline_account_box_24)
            .error(R.drawable.ic_baseline_account_box_24).listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    profileImageLoader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    profileImageLoader.visibility = View.GONE
                    return false
                }

            })
            .into(profileImageView)
    }


    @SuppressLint("QueryPermissionsNeeded", "UnsupportedChromeOsCameraSystemFeature")
    fun openDialogToGetImage(customDialog: BottomSheetDialog, cameraFolder : ClickUploadPicture) {
        customDialog.setContentView(R.layout.dialog_take_photo)
        val cameraBtn: LinearLayout? = customDialog.findViewById(R.id.camera)
        val folderBtn: LinearLayout? = customDialog.findViewById(R.id.folder)
        cameraBtn?.setOnClickListener {
           cameraFolder.cameraClick()
        }
        folderBtn?.setOnClickListener {
           cameraFolder.chooseFolderPicture()
        }
        customDialog.show()
    }


    interface ClickUploadPicture{
        fun cameraClick()
        fun chooseFolderPicture()
    }


    private suspend fun checkIsValueInList(value : String , list : List<String>) : List<Boolean>{
        val listOfResult : MutableList<Boolean> = ArrayList()
        for (searchValue in list){
           if(value in searchValue) {
              listOfResult.add(true)
           }
           else{
               listOfResult.add(false)
          }
        }
        return listOfResult
    }

    suspend fun getResultListFromAllTypeLists(value : String,
                                      lists : List<List<String>> ,
                                      searchList : List<DepositDataResponse>) : MutableList<DepositDataResponse> {
        var listOfResult : MutableList<DepositDataResponse> = ArrayList()

        for (passList in lists ){
          val listOfResultBoolean = checkIsValueInList(value,passList)
            for (i in listOfResultBoolean.indices) {
                if(listOfResultBoolean[i]){
                    listOfResult.add(searchList[i])
                    listOfResult = listOfResult.distinct().toMutableList()
                }
            }
        }

        return listOfResult
    }


    suspend fun getResultListFromAllTypeWithdrawLists(value : String,
                                              lists : List<List<String>> ,
                                              searchList : List<BaseResponseWithdraw>) : MutableList<BaseResponseWithdraw> {
        var listOfResult : MutableList<BaseResponseWithdraw> = ArrayList()

        for (passList in lists ){
            val listOfResultBoolean = checkIsValueInList(value,passList)
            for (i in listOfResultBoolean.indices) {
                if(listOfResultBoolean[i]){
                    listOfResult.add(searchList[i])
                    listOfResult = listOfResult.distinct().toMutableList()
                }
            }
        }

        return listOfResult
    }

      suspend fun getResultListFromAllTypeTransactionLists(value : String,
                                                 lists : List<List<String>> ,
                                                 searchList : List<TransactionObject>): MutableList<TransactionObject> {

        var listOfResult : MutableList<TransactionObject> = ArrayList()

        for (passList in lists ){
            val listOfResultBoolean = checkIsValueInList(value,passList)
            for (i in listOfResultBoolean.indices) {
                if(listOfResultBoolean[i]){
                    listOfResult.add(searchList[i])
                    listOfResult = listOfResult.distinct().toMutableList()
                }
            }
        }

        return listOfResult

    }

    suspend fun getResultListFromAllTypeAffiliateLists(value : String,
                                               lists : List<List<String>> ,
                                               searchList : List<AffiliateObject>): MutableList<AffiliateObject> {

        var listOfResult : MutableList<AffiliateObject> = ArrayList()

        for (passList in lists ){
            val listOfResultBoolean = checkIsValueInList(value,passList)
            for (i in listOfResultBoolean.indices) {
                if(listOfResultBoolean[i]){
                    listOfResult.add(searchList[i])
                    listOfResult = listOfResult.distinct().toMutableList()
                }
            }
        }

        return listOfResult

    }

    suspend fun getResultListFromAllTypeAccountItemLists(value : String,
                                                         lists : List<List<String>>,
                                                         searchList : List<AccountViewObject>): MutableList<AccountViewObject> {

        var listOfResult : MutableList<AccountViewObject> = ArrayList()

        for (passList in lists ){
            val listOfResultBoolean = checkIsValueInList(value,passList)
            for (i in listOfResultBoolean.indices) {
                if(listOfResultBoolean[i]){
                    listOfResult.add(searchList[i])
                    listOfResult = listOfResult.distinct().toMutableList()
                }
            }
        }

        return listOfResult

    }



    fun showAlertDialog(context: Context,
                        title : String ,
                        description : String ,
                        positiveTag : String ,
                        negativeTag : String , onActionPerform: () -> Unit){
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context)

        // set message of alert dialog
        dialogBuilder.setMessage(description)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton(positiveTag, DialogInterface.OnClickListener {
                    dialog, _ ->
                dialog.cancel()
                onActionPerform()
            })
            // negative button text and action
            .setNegativeButton(negativeTag, DialogInterface.OnClickListener {
                    dialog, _ ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(title)
        // show alert dialog
        alert.show()
    }



}