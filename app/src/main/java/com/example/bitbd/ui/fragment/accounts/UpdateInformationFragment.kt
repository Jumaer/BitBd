package com.example.bitbd.ui.fragment.accounts

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.INFO
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.databinding.FragmentUpdateInformationBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch


class UpdateInformationFragment : Fragment() {
    private var _binding: FragmentUpdateInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: BitBDPreferences
    lateinit var viewModel : AccountViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]
        // Inflate the layout for this fragment
        _binding = FragmentUpdateInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        preferences = BitBDPreferences(requireContext())
        binding.submitButton.setOnClickListener {
            submitToAddNew(viewModel)
        }

        seeDisplayDataView()
        return root
    }

    private fun submitToAddNew(viewModel: AccountViewModel) {
        if(valueOfType.isEmpty()){
            binding.typeLayout.error = getString(R.string.this_field_is_required)
        }
        binding.typeLayout.error = null

        if(valueOfAccountNumber.isEmpty()){
            binding.trxAccountLayout.error = getString(R.string.this_field_is_required)
        }

        binding.trxAccountLayout.error = null


        if(valueOfAccountName.isEmpty()){
            binding.trxAccountNameLayout.error = getString(R.string.this_field_is_required)
        }

        binding.trxAccountNameLayout.error = null



        if(valueOfBranch.isEmpty() && valueOfType == "Bank"){
            binding.trxBranchLayout.error = getString(R.string.this_field_is_required)
            return
        }

        binding.trxBranchLayout.error = null


        if(activeStatus == null){
            BitBDUtil.showMessage("Please set the status", INFO)
        }




        if(valueOfType.isEmpty() || valueOfAccountNumber.isEmpty() || valueOfAccountName.isEmpty() || activeStatus == null ){
            return
        }

        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }

        viewModel.accountSubmit.observe(viewLifecycleOwner){
            if(it != null){
                preferences.setAnyAccount(true)
                binding.typeLayout.editText?.text = BitBDUtil.editable("")
                binding.trxAccountLayout.editText?.text = BitBDUtil.editable("")
                binding.trxAccountNameLayout.editText?.text = BitBDUtil.editable("")
                binding.trxBranchLayout.editText?.text = BitBDUtil.editable("")
                seeDisplayDataView()
                BitBDUtil.showMessage(it.get("message").asString, SUCCESS)
                loading?.dismiss()
            }
        }



        lifecycleScope.launch{
            viewModel.submitToNew(requireContext(),valueOfAccountName,valueOfAccountNumber,valueOfType,activeStatus.toString(),valueOfBranch)
        }

    }


    private var typeList = ArrayList<String>()
    private var valueOfType: String = ""
    private var valueOfAccountNumber : String = ""
    private var valueOfAccountName : String = ""
    private var valueOfBranch : String = ""
    private var activeStatus : Int? = null

    private fun seeDisplayDataView() {
        typeList.clear()
        typeList.add("Mobile Banking")
        typeList.add("Bank Account")
        binding.typeLayout.setOnKeyListener(null)
        binding.typeTextView.inputType = InputType.TYPE_NULL
        context?.let {
            ArrayAdapter(
                it, android.R.layout.simple_list_item_1,
                typeList
            ).also { adapter ->
                binding.typeTextView.setAdapter(adapter)
                adapter.notifyDataSetChanged()
            }
        }
        binding.typeLayout.editText?.doOnTextChanged { text, _, _, _ ->
            if (text?.isNotEmpty() == true) {
                if(text.toString() == "Mobile Banking"){
                    valueOfType = "Mobile"
                    valueOfBranch = ""
                    binding.trxBranchLayout.editText?.text = BitBDUtil.editable("")
                    binding.branch.visibility = View.GONE
                }
                else if(text.toString() == "Bank Account"){
                    valueOfType = "Bank"
                    binding.branch.visibility = View.VISIBLE
                }
                else{
                    valueOfType = ""
                    binding.branch.visibility = View.GONE
                }

            }
        }

        binding.trxBranchLayout.editText?.onFocusChangeListener =
        OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if(valueOfBranch.isEmpty())
                    binding.trxBranchLayout.error = getString(R.string.this_field_is_required)
            } else {
                binding.trxBranchLayout.editText?.doOnTextChanged{ text, _, _, _ ->
                    if (text?.isNotEmpty() == true && valueOfType == "Bank") {
                        valueOfBranch = text.toString()
                    }
                }
            }
        }



        binding.trxAccountLayout.editText?.onFocusChangeListener =
            OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if(valueOfAccountNumber.isEmpty())
                    binding.trxAccountLayout.error = getString(R.string.this_field_is_required)
                } else {
                    binding.trxAccountLayout.editText?.doOnTextChanged{ text, _, _, _ ->
                        if (text?.isNotEmpty() == true) {
                            valueOfAccountNumber = text.toString()
                        }
                    }
                }
            }

        binding.trxAccountNameLayout.editText?.onFocusChangeListener =   OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if(valueOfAccountName.isEmpty())
                binding.trxAccountNameLayout.error = getString(R.string.this_field_is_required)
            } else {
                binding.trxAccountNameLayout.editText?.doOnTextChanged{ text, _, _, _ ->
                    if (text?.isNotEmpty() == true) {
                        valueOfAccountName = text.toString()
                    }
                }
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            activeStatus = when (checkedId) {
                R.id.active -> 1
                R.id.inactive -> 0
                else -> null
            }
        }

        binding.active.isChecked = true
        binding.inactive.isChecked = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}