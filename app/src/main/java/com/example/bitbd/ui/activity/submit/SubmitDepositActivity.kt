package com.example.bitbd.ui.activity.submit

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.DEPOSIT
import com.example.bitbd.constant.MEDIA_TYPE
import com.example.bitbd.databinding.ActivitySubmitDepositBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.fragment.deposit.model.PaymentMethod
import com.example.bitbd.util.BitBDUtil
import com.example.bitbd.util.ImageHandler
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SubmitDepositActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitDepositBinding
    private var depositSubmit: PaymentMethod? = null
    private lateinit var preference: BitBDPreferences
    var customDialog: BottomSheetDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel =
            ViewModelProvider(this)[SubmitDepositViewModel::class.java]
        preference = BitBDPreferences(this)
        title = getString(R.string.depositDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        depositSubmit = intent.getParcelableExtra(DEPOSIT)
        customDialog = BottomSheetDialog(this)
        setViewData(depositSubmit)

        binding.submitButton.setOnClickListener {
            depositDataSubmit(viewModel)
        }

        setObservers(viewModel)


    }

    private fun setObservers(viewModel: SubmitDepositViewModel) {
        var loading : LoadingProgress? = null

        viewModel.progress.observe(this){
            if(it != null){
                if(it) {
                    loading = BitBDUtil.showProgress(this@SubmitDepositActivity)
                }
                else{
                    loading?.dismiss()

                }
            }
        }

        viewModel.deposit.observe(this){
            if(it != null){
                BitBDUtil.showMessage(it.get("message").toString(),this@SubmitDepositActivity)
                loading?.dismiss()
            }
        }

    }

    private fun depositDataSubmit(viewModel: SubmitDepositViewModel) {
        val amount = binding.trxAmountLayout.editText?.text.toString().trim()
        val account = binding.trxAccountLayout.editText?.text.toString().trim()
        val method = depositSubmit?.id.toString()
        val trxId = binding.trxIdLayout.editText?.text.toString().trim()


        if (amount == "") {
            binding.trxAmountLayout.error = getString(R.string.this_field_is_required)
        } else binding.trxAmountLayout.error = null

        if (account == "") {
            binding.trxAccountLayout.error = getString(R.string.this_field_is_required)
        } else binding.trxAccountLayout.error = null

        if (trxId == "") {
            binding.trxIdLayout.error = getString(R.string.this_field_is_required)
        } else binding.trxIdLayout.error = null


        if (amount == "" || account == "" || trxId == "") {
            return
        }

        if(amount.toString().toDouble() < preference.getMinDeposit()!!.toDouble()){
              binding.trxAmountLayout.error = "Minimum ${preference.getMinDeposit().toString()} is must"
              return
        } else binding.trxAmountLayout.error = null

        val partAmount = amount.toRequestBody(MEDIA_TYPE)
        val partAccount = account.toRequestBody(MEDIA_TYPE)
        val partMethod = method.toRequestBody(MEDIA_TYPE)
        val partTrxId = trxId.toRequestBody(MEDIA_TYPE)


        if(multipartImage == null){
            BitBDUtil.showMessage("Please provide the screen shoot of transaction", this)
            return
        }

        lifecycleScope.launch {
            viewModel.submitDeposit(this@SubmitDepositActivity, partAccount, partMethod, partTrxId,partAmount, multipartImage!!)
        }

    }

    private var multipartImage: MultipartBody.Part? = null

    @SuppressLint("SetTextI18n")
    private fun setViewData(depositSubmit: PaymentMethod?) {
        if (depositSubmit != null) {
            binding.trxAccountLayout.editText?.text =
                BitBDUtil.editable(depositSubmit.account.toString())
        }
        binding.trxAccountLayout.editText?.inputType = InputType.TYPE_NULL
        binding.minDeposit.text = "(${preference.getMinDeposit()}*)"
        binding.coinLayout.editText?.text = BitBDUtil.editable("0")
        binding.coinLayout.editText?.inputType = InputType.TYPE_NULL
        binding.trxAmountLayout.editText?.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                val valueOfAmount = it.toString().toDouble()
                binding.coinLayout.editText?.text = BitBDUtil.editable(
                    "${
                        valueOfAmount / (preference.getRateOfDeposit()
                            ?.toDouble()!!)
                    }"
                )
            } else binding.coinLayout.editText?.text = BitBDUtil.editable("0")
        }

        binding.uploadImage.setOnClickListener {
            customDialog?.let { it1 ->
                BitBDUtil.openDialogToGetImage(it1, object : BitBDUtil.ClickUploadPicture {
                    override fun cameraClick() {

                        cropImage.launch(
                            options {
                                setImageSource(false, true)
                                setCropShape(CropImageView.CropShape.RECTANGLE)
                                setActivityTitle("Crop Image")
                            }
                        )

                    }

                    override fun chooseFolderPicture() {
                        cropImage.launch(
                            options {
                                setImageSource(true, false)
                                setCropShape(CropImageView.CropShape.RECTANGLE)
                                setActivityTitle("Crop Image")
                            }
                        )

                    }

                })
            }
        }
    }

    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            customDialog?.dismiss()
            // use the returned uri
            val uriContent = result.uriContent
            binding.trxImage.setImageURI(uriContent)
            // val uriFilePath = result.getUriFilePath(this) // optional usage
            if (uriContent != null) {
                ImageHandler.getBitmapFormUri(this, uriContent)?.let {
                    ImageHandler.bitmapToFile(it, object : ImageHandler.FileReturn {
                        override fun getFileForMultipart(file: File) {
                            val filePart = file.asRequestBody("image/*".toMediaTypeOrNull())
                            multipartImage =
                                MultipartBody.Part.createFormData("image", file.name, filePart)
                        }
                    })
                }
            }
        } else {
            // an error occurred
            val exception = result.error
            Log.d("exception", exception.toString())
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