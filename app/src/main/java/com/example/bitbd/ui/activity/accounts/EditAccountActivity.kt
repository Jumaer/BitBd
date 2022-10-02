package com.example.bitbd.ui.activity.accounts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.Edit_INFO_OBJECT
import com.example.bitbd.constant.INFO
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.databinding.ActivityAccountManagementBinding
import com.example.bitbd.databinding.ActivityEditAccountBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.BaseActivity
import com.example.bitbd.ui.fragment.accounts.AccountViewModel
import com.example.bitbd.ui.fragment.accounts.model.EditInformationObject
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch

class EditAccountActivity : BaseActivity() {
    private lateinit var binding: ActivityEditAccountBinding
    private lateinit var preferences: BitBDPreferences
    lateinit var editableDataObject: EditInformationObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.accountsEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        editableDataObject = intent.extras?.get(Edit_INFO_OBJECT) as EditInformationObject

        preferences = BitBDPreferences(this)
        binding.submitButton.setOnClickListener {
            submitToAddNew(viewModel)
        }

        seeDisplayDataView(editableDataObject)

    }


    private fun submitToAddNew(viewModel: AccountViewModel) {
        if (valueOfType.isEmpty()) {
            binding.typeLayout.error = getString(R.string.this_field_is_required)
        } else binding.typeLayout.error = null

        if (valueOfAccountNumber.isEmpty()) {
            binding.trxAccountLayout.error = getString(R.string.this_field_is_required)
        } else binding.trxAccountLayout.error = null


        if (valueOfAccountName.isEmpty()) {
            binding.trxAccountNameLayout.error = getString(R.string.this_field_is_required)
        } else binding.trxAccountNameLayout.error = null


        if(valueOfBranch.isEmpty() && valueOfType == "Bank"){
            binding.trxBranchLayout.error = getString(R.string.this_field_is_required)
            return
        }
        else binding.trxBranchLayout.error = null

        if (activeStatus == null) {
            BitBDUtil.showMessage("Please set the status", INFO)
        }

        if (valueOfType.isEmpty() || valueOfAccountNumber.isEmpty() || valueOfAccountName.isEmpty() || activeStatus == null) {
            return
        }

        var loading: LoadingProgress? = null

        viewModel.progress.observe(this) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(this)
                } else loading?.dismiss()
            }
        }

        viewModel.submitEdit.observe(this) {
            if (it != null) {
                preferences.setRecallWithdraw(true)
                preferences.setAnyAccount(true)
                binding.typeLayout.editText?.text = BitBDUtil.editable("")
                binding.trxAccountLayout.editText?.text = BitBDUtil.editable("")
                binding.trxAccountNameLayout.editText?.text = BitBDUtil.editable("")
                binding.trxBranchLayout.editText?.text = BitBDUtil.editable("")
                BitBDUtil.showMessage(it.get("message").asString, SUCCESS)
                loading?.dismiss()
                onBackPressed()
            }
        }



        lifecycleScope.launch {
            viewModel.submitEditInfo(
                this@EditAccountActivity, valueOfAccountName, valueOfAccountNumber, valueOfType,
                activeStatus.toString(), editableDataObject.id.toString(), valueOfBranch
            )
        }

    }


    private var typeList = ArrayList<String>()
    private var valueOfType: String = ""
    private var valueOfBranch: String = ""
    private var valueOfAccountNumber: String = ""
    private var valueOfAccountName: String = ""
    private var activeStatus: Int? = null

    private fun seeDisplayDataView(editableDataObject: EditInformationObject) {
        typeList.clear()
        typeList.add("Mobile Banking")
        typeList.add("Bank Account")
        binding.typeLayout.setOnKeyListener(null)
        if (editableDataObject.type.toString() == "Mobile") {
            binding.typeLayout.editText?.text = BitBDUtil.editable("Mobile Banking")
        } else {
            binding.typeLayout.editText?.text = BitBDUtil.editable("Bank Account")
        }

        binding.typeTextView.inputType = InputType.TYPE_NULL
        this.let {
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
               if (text.toString() == "Mobile Banking") {
                   valueOfType =   "Mobile"
                   valueOfBranch = ""
                   binding.trxBranchLayout.editText?.text = BitBDUtil.editable("")
                   binding.branch.visibility = View.GONE
                } else if (text.toString() == "Bank Account") {
                   valueOfType =   "Bank"
                   binding.branch.visibility = View.VISIBLE
                } else {
                   valueOfType = ""
                   binding.branch.visibility = View.GONE
                }
            }
        }



        binding.trxAccountLayout.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (valueOfAccountNumber.isEmpty())
                        binding.trxAccountLayout.error = getString(R.string.this_field_is_required)
                } else {
                    binding.trxAccountLayout.editText?.doOnTextChanged { text, _, _, _ ->
                        if (text?.isNotEmpty() == true) {
                            valueOfAccountNumber = text.toString()
                        }
                    }
                }
            }

        binding.trxAccountNameLayout.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (valueOfAccountName.isEmpty())
                        binding.trxAccountNameLayout.error =
                            getString(R.string.this_field_is_required)
                } else {
                    binding.trxAccountNameLayout.editText?.doOnTextChanged { text, _, _, _ ->
                        if (text?.isNotEmpty() == true) {
                            valueOfAccountName = text.toString()
                        }
                    }
                }
            }

        binding.trxBranchLayout.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (valueOfBranch.isEmpty())
                        binding.trxBranchLayout.error = getString(R.string.this_field_is_required)
                } else {
                    binding.trxBranchLayout.editText?.doOnTextChanged { text, _, _, _ ->
                        if (text?.isNotEmpty() == true && valueOfType == "Bank") {
                            valueOfBranch = text.toString()
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




        valueOfType = editableDataObject.type.toString()
        valueOfAccountName = editableDataObject.name.toString()
        valueOfAccountNumber = editableDataObject.account.toString()
        valueOfBranch = editableDataObject.branch.toString()
        binding.trxAccountLayout.editText?.text =
            BitBDUtil.editable(editableDataObject.account.toString())
        binding.trxAccountNameLayout.editText?.text =
            BitBDUtil.editable(editableDataObject.name.toString())
        if(valueOfType == "Bank" ){
            if(editableDataObject.branch != null){
                binding.trxBranchLayout.editText?.text = BitBDUtil.editable(editableDataObject.branch.toString())
            }
            binding.branch.visibility = View.VISIBLE
        }
        else{
            binding.trxBranchLayout.editText?.text = BitBDUtil.editable("")
        }

        if (editableDataObject.status.toString() == "1") {
            binding.active.isChecked = true
            binding.inactive.isChecked = false
        } else {
            binding.active.isChecked = false
            binding.inactive.isChecked = true
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