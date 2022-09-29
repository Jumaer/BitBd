package com.example.bitbd.ui.fragment.accounts

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitbd.R
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.INFO
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.databinding.FragmentUpdateInformationBinding
import com.example.bitbd.databinding.FragmentViewInformationBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.fragment.accounts.adapter.AccountViewItemAdapter
import com.example.bitbd.ui.fragment.accounts.model.AccountViewObject
import com.example.bitbd.ui.fragment.affiliate.AffiliateViewModel
import com.example.bitbd.ui.fragment.affiliate.model.AffiliateObject
import com.example.bitbd.ui.fragment.deposit.adapter.DepositItemAdapter
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class ViewInformationFragment : Fragment() {
    private var _binding: FragmentViewInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences : BitBDPreferences
    lateinit var viewModel : AccountViewModel

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[AccountViewModel::class.java]
        // Inflate the layout for this fragment
        _binding = FragmentViewInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        preferences = BitBDPreferences(requireContext())
        createDisplayAdapterForDeposits()
        getExpectedData(viewModel)
        binding.appCompatEditText.doAfterTextChanged {
            val searchableText = it.toString()
            accountItemList.clear()
            adapter.notifyDataSetChanged()
            searchItem(searchableText)
        }
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchItem(searchableText: String) {
        lifecycleScope.launch {
            if (searchableText.isNotEmpty()) {
                accountItemList.addAll(
                    BitBDUtil.getResultListFromAllTypeAccountItemLists(
                        searchableText,
                        searchListsCollection,
                        serverAccountViewObjectList
                    )
                )
            } else {
                accountItemList.addAll(serverAccountViewObjectList)
            }
            adapter.notifyDataSetChanged()
        }

    }

    lateinit var adapter: AccountViewItemAdapter
    private var accountItemList: MutableList<AccountViewObject> = ArrayList()


    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayAdapterForDeposits() {
        adapter = AccountViewItemAdapter(accountItemList, ::onAdapterItemClick,::onDeleteItemClick,requireContext())
        binding.accountRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.accountRecycle.adapter = adapter
        binding.accountRecycle.adapter?.notifyDataSetChanged()
    }

    private fun onAdapterItemClick(position: Int) {
        BitBDUtil.showMessage("Perform EDIT", INFO)

    }

    private var isPressedDelete = false
    private fun onDeleteItemClick(position: Int){
        lifecycleScope.launch{
            isPressedDelete = true
            viewModel.deleteAccount(requireContext(),serverAccountViewObjectList[position].id.toString())
        }
    }


    var nameList: MutableList<String> = ArrayList()
    var typeList: MutableList<String> = ArrayList()
    var branchList: MutableList<String> = ArrayList()
    var accountList: MutableList<String> = ArrayList()



    var searchListsCollection: MutableList<List<String>> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExpectedSearchList(
        itemList: MutableList<AccountViewObject>,
        loading: LoadingProgress?
    ) {
        clearAllSearchList()
        for (item in itemList) {
            item.name?.let { nameList.add(it) }
            item.type?.let { typeList.add(it) }
            item.account?.let { accountList.add(it) }
            item.branch?.let { branchList.add(it.toString()) }
        }

        searchListsCollection.add(nameList)
        searchListsCollection.add(typeList)
        searchListsCollection.add(branchList)
        searchListsCollection.add(accountList)



        loading?.dismiss()
    }

    private fun clearAllSearchList() {
        nameList.clear()
        typeList.clear()
        branchList.clear()
        accountList.clear()
        searchListsCollection.clear()
    }

    private var serverAccountViewObjectList: MutableList<AccountViewObject> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun getExpectedData(viewModel: AccountViewModel) {

        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }

        viewModel.accountViewObject.observe(viewLifecycleOwner) {
            if (it != null) {
                val baseResponse = it.data ?: return@observe
                serverAccountViewObjectList.clear()
                for (response in baseResponse) {
                    response?.let { obj -> serverAccountViewObjectList.add(obj) }
                }
                accountItemList.clear()
                accountItemList.addAll(serverAccountViewObjectList)
                adapter.notifyDataSetChanged()
                createExpectedSearchList(serverAccountViewObjectList, loading)

            }
        }


        viewModel.accountDelete.observe(viewLifecycleOwner){
            if(it!= null && isPressedDelete){
                isPressedDelete = false
                lifecycleScope.launch {
                    viewModel.getBaseAccountViewInfo(requireContext())
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getBaseAccountViewInfo(requireContext())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        if(preferences.getAnyChangeAccount()){
            preferences.setAnyAccount(false)
            lifecycleScope.launch {
                viewModel.getBaseAccountViewInfo(requireContext())
            }
        }
    }

}