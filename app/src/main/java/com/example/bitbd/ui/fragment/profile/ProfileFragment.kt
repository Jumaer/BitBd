package com.example.bitbd.ui.fragment.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.bitbd.BuildConfig
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.MEDIA_TYPE
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.databinding.FragmentProfileBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.main.MainActivity
import com.example.bitbd.util.BitBDUtil
import com.example.bitbd.util.ImageHandler
import com.example.bitbd.util.ImageHandler.bitmapToFile
import com.example.bitbd.util.ImageHandler.getBitmapFormUri
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ProfileFragment : Fragment() {
    private var multipartImage: MultipartBody.Part? = null
    private var _binding: FragmentProfileBinding? = null
    var customDialog : BottomSheetDialog? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var preference : BitBDPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        preference = BitBDPreferences(requireContext())
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        customDialog =  BottomSheetDialog(requireContext())

        setDisplayView()
        prepareToSubmitForUpdate(profileViewModel)
        return root
    }

    private fun prepareToSubmitForUpdate(viewModel: ProfileViewModel) {
        var loading : LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner){
            if(it != null){
                if(it) {
                    loading = BitBDUtil.showProgress(requireContext())
                }
                else{
                    loading?.dismiss()

                }
            }
        }

        viewModel.updateProfile.observe(viewLifecycleOwner){ userBase->
            if(userBase != null){
                val user  = userBase.data
                user?.name?.let { it1 -> preference.putName(it1.toString()) }
                user?.mobile?.let { it1 -> preference.putMobileNumber(it1.toString()) }
                user?.email?.let { it1 -> preference.putEmail(it1.toString()) }
                user?.image?.let { it1 -> preference.putImageUrl(it1.toString()) }
                user?.affiliateStatus?.let { it1 -> preference.putAffiliate(it1.toInt()) }
                user?.affiliateCode?.let { it1 -> preference.putAffiliateCode(it1.toString()) }
                user?.username?.let { it1 -> preference.putUserName(it1.toString()) }
                user?.slug?.let { it1 -> preference.putSlug(it1.toString()) }

                BitBDUtil.showMessage(userBase.message.toString(), SUCCESS)

                (activity as MainActivity?)?.getNavView()
                    ?.let { (activity as MainActivity?)?.setHeaderView(it) }
            }

        }
        binding.signUpButton.setOnClickListener {
            updateInfo(viewModel)
        }
    }

    private fun updateInfo(viewModel: ProfileViewModel) {
        try {
            val imm: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
        } catch (e: java.lang.Exception) {
        //    BitBDUtil.showMessage("Something wrong", requireContext())
        }

        val name = binding.nameLayout.editText?.text.toString()
        val email = binding.emailLayout.editText?.text.toString()
        val phone = binding.phoneLayout.editText?.text.toString()
        val password = binding.passwordLayout.editText?.text.toString()
        val passwordConf = binding.retypePasswordLayout.editText?.text.toString()

        if (name == "") {
            binding.nameLayout.error = getString(R.string.this_field_is_required)
        }
        else binding.nameLayout.error = null

        if (phone == "") {
            binding.phoneLayout.error = getString(R.string.this_field_is_required)
        }
        else binding.phoneLayout.error = null

        if (password != passwordConf) {
            binding.passwordLayout.error = getString(R.string.password_match)
            binding.retypePasswordLayout.error = getString(R.string.password_match)
        }
        else{
            binding.passwordLayout.error = null
            binding.retypePasswordLayout.error = null
        }



        if (name == "" || phone == "" || password != passwordConf) {
            return
        }

       val partName = name.toRequestBody(MEDIA_TYPE)
       val partEmail = email.toRequestBody(MEDIA_TYPE)
       val partPhone = phone.toRequestBody(MEDIA_TYPE)
       val partPassword = password.toRequestBody(MEDIA_TYPE)
       val partRetypePassword = passwordConf.toRequestBody(MEDIA_TYPE)



       lifecycleScope.launch {
           if(multipartImage != null){
               viewModel.updateProfileWithImage(partName,partEmail,partPhone,partPassword,partRetypePassword,
                   preference.getUserSlug().toString(),
                   multipartImage!!,requireContext())
           }
           else{
               viewModel.updateProfileWithoutImage(name,email,phone,password,passwordConf,
                   preference.getUserSlug().toString(),requireContext())
           }
       }

    }

    @SuppressLint("SetTextI18n")
    private fun setDisplayView() {
        val profileImageLoader = binding.animationView
        val urlProfileImage = BuildConfig.SERVER_URL + preference.getImageUrl()?.toString()
        BitBDUtil.loadImage(binding.profileImageView,profileImageLoader,urlProfileImage,requireContext())
        binding.refCode.text = "Reference Code : "+ preference.getAffiliateCode()
        binding.nameLayout.editText?.text = BitBDUtil.editable(preference.getName().toString())
        binding.phoneLayout.editText?.text = BitBDUtil.editable(preference.getMobileNumber().toString())
        binding.phoneLayout.editText?.inputType = InputType.TYPE_NULL
        binding.userNameLayout.editText?.text = BitBDUtil.editable(preference.getUsername().toString())
        binding.userNameLayout.editText?.inputType = InputType.TYPE_NULL
        binding.emailLayout.editText?.text = BitBDUtil.editable(preference.geEmail().toString())


        binding.uploadImage.setOnClickListener {
            customDialog?.let { it1 ->
                BitBDUtil.openDialogToGetImage(it1, object : BitBDUtil.ClickUploadPicture{
                    override fun cameraClick() {

                        cropImage.launch(
                            options {
                                setImageSource(false,true)
                                setCropShape(CropImageView.CropShape.OVAL)
                                setAspectRatio(1,1)
                                setActivityTitle("Crop Image")
                            }
                        )

                    }

                    override fun chooseFolderPicture() {
                        cropImage.launch(
                            options {
                                setImageSource(true,false)
                                setCropShape(CropImageView.CropShape.OVAL)
                                setAspectRatio(1,1)
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
            binding.profileImageView.setImageURI(uriContent)
            // val uriFilePath = result.getUriFilePath(this) // optional usage
            if (uriContent != null) {
                getBitmapFormUri(requireActivity(),uriContent)?.let { bitmapToFile(it,object : ImageHandler.FileReturn{
                    override fun getFileForMultipart(file: File) {
                        val filePart = file.asRequestBody("image/*".toMediaTypeOrNull())
                        multipartImage = MultipartBody.Part.createFormData("avatar", file.name, filePart)
                    }
                }) }
            }
        } else {
            // an error occurred
            val exception = result.error
            Log.d("exception",exception.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}