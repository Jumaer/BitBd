package com.example.bitbd.ui.fragment.withdraw_money

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
import com.example.bitbd.databinding.FragmentDepositBinding
import com.example.bitbd.databinding.FragmentViewWitdrawBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.fragment.deposit.DepositViewModel
import com.example.bitbd.ui.fragment.deposit.adapter.DepositItemAdapter
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.ui.fragment.withdraw_money.adapter.WithdrawItemsAdapter
import com.example.bitbd.ui.fragment.withdraw_money.model.BaseResponseWithdraw
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ViewWithdrawFragment : Fragment() {
    private var _binding: FragmentViewWitdrawBinding? = null
    var withdrawItemListFromServer: MutableList<BaseResponseWithdraw> = ArrayList()
    var slideshowViewModel : WithdrawViewModel? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var preferences : BitBDPreferences? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        slideshowViewModel = ViewModelProvider(this)[WithdrawViewModel::class.java]
        _binding = FragmentViewWitdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        preferences = BitBDPreferences(requireContext())
        createDisplayAdapterForWithdraws()
        getExpectedData(slideshowViewModel!!)



        binding.appCompatEditText.doAfterTextChanged {
            val searchableText = it.toString()
            withdrawItemList.clear()
            adapter.notifyDataSetChanged()
            searchItem(searchableText)
        }

        return root
    }
    var dateList: MutableList<String> = ArrayList()
    var accNameList: MutableList<String> = ArrayList()
    var trxAccNoList: MutableList<String> = ArrayList()
    var trxTypeList: MutableList<String> = ArrayList()
    var amountList: MutableList<String> = ArrayList()
    var statusList: MutableList<String> = ArrayList()

    var searchListsCollection: MutableList<List<String>> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    private fun searchItem(searchableText: String) {
        lifecycleScope.launch {
            if (searchableText.isNotEmpty()) {
                withdrawItemList.addAll(
                    BitBDUtil.getResultListFromAllTypeWithdrawLists(
                        searchableText,
                        searchListsCollection,
                        withdrawItemListFromServer
                    )
                )
            } else {
                withdrawItemList.addAll(withdrawItemListFromServer)
            }
            adapter.notifyDataSetChanged()
        }

    }

    lateinit var adapter: WithdrawItemsAdapter
    private var  withdrawItemList: MutableList<BaseResponseWithdraw> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayAdapterForWithdraws() {
        adapter = WithdrawItemsAdapter(withdrawItemList, ::onAdapterItemClick, requireContext())
        binding.withdrawRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.withdrawRecycle.adapter = adapter
        binding.withdrawRecycle.adapter?.notifyDataSetChanged()
    }

    private fun onAdapterItemClick(position: Int) {
           val withdrawItem = withdrawItemListFromServer[position]
           lifecycleScope.launch{
               slideshowViewModel?.withdrawItemDelete(requireContext(), withdrawItem.id.toString())
           }

    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getExpectedData(viewModel: WithdrawViewModel) {

        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }



        viewModel.withdraw.observe(viewLifecycleOwner) {
            if (it != null) {
                val baseResponse = it.data ?: return@observe
                withdrawItemListFromServer.clear()
                withdrawItemList.clear()
                for (element in baseResponse) {
                    withdrawItemListFromServer.add(element)

                }
                withdrawItemList.addAll(withdrawItemListFromServer)
                adapter.notifyDataSetChanged()
                createExpectedSearchList(withdrawItemList, loading)
            }
        }


        viewModel.deleteWithdraw.observe(viewLifecycleOwner){
            if(it != null){
                lifecycleScope.launch {
                    viewModel.withdraw(requireContext())
                }
            }
        }



        lifecycleScope.launch {
            viewModel.withdraw(requireContext())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExpectedSearchList(
        withdrawList: MutableList<BaseResponseWithdraw>,
        loading: LoadingProgress?
    ) {
        clearAllSearchList()
        for (withdraw in withdrawList) {
            dateList.add(
                ZonedDateTime.parse(withdraw.createdAt)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                    )
            )
            withdraw.trxName?.let { accNameList.add(it) }
            withdraw.trxType?.let { trxTypeList.add(it) }
            withdraw.amount?.let { amountList.add(it.toString()) }
            withdraw.withdrawAccount?.let { trxAccNoList.add(it) }
            withdraw.status?.let { statusList.add(it) }
        }

        searchListsCollection.add(dateList)
        searchListsCollection.add(accNameList)
        searchListsCollection.add(trxTypeList)
        searchListsCollection.add(amountList)
        searchListsCollection.add(trxAccNoList)
        searchListsCollection.add(statusList)

        loading?.dismiss()
    }

    private fun clearAllSearchList() {
        dateList.clear()
        accNameList.clear()
        trxAccNoList.clear()
        trxTypeList.clear()
        amountList.clear()
        statusList.clear()
        searchListsCollection.clear()
    }


    override fun onResume() {
        super.onResume()

        if( preferences?.getAnyChangeWithdraw() == true){
            preferences?.setAnyChangeWithdraw(false)
            lifecycleScope.launch {
                slideshowViewModel?.withdraw(requireContext())
            }
        }
    }


}