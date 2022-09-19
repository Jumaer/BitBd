package com.example.bitbd.util
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.ui.fragment.profile.ProfileFragment
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

    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun showMessage( message : String , context: Context){
        context.toast(message)
    }
    fun showSessionExpireMessage( context: Context){
        context.toast("Please log in again")
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


}