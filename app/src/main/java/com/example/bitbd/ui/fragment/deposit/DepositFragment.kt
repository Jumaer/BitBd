package com.example.bitbd.ui.fragment.deposit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.constant.MESSAGE
import com.example.bitbd.constant.SUCCESS
import com.example.bitbd.constant.TIME_CREATED
import com.example.bitbd.constant.TIME_UPDATED
import com.example.bitbd.databinding.FragmentDepositBinding
import com.example.bitbd.sharedPref.BitBDPreferences
import com.example.bitbd.ui.activity.notification.NotificationDetailsActivity
import com.example.bitbd.ui.activity.notification.adapter.NotificationAdapter
import com.example.bitbd.ui.activity.notification.model.NotificationResponse
import com.example.bitbd.ui.fragment.deposit.adapter.DepositItemAdapter
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null
    var depositItemListFromServer: MutableList<DepositDataResponse> = ArrayList()
    var slideshowViewModel : DepositViewModel? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var preferences : BitBDPreferences? = null

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         slideshowViewModel = ViewModelProvider(this)[DepositViewModel::class.java]
         _binding = FragmentDepositBinding.inflate(inflater, container, false)
         val root: View = binding.root
         isDeletePressed = false
         preferences = BitBDPreferences(requireContext())
         createDisplayAdapterForDeposits()
         getExpectedData(slideshowViewModel!!)



        binding.appCompatEditText.doAfterTextChanged {
            val searchableText = it.toString()
            depositItemList.clear()
            adapter.notifyDataSetChanged()
            searchItem(searchableText)
        }

        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchItem(searchableText: String) {
        lifecycleScope.launch {
            if (searchableText.isNotEmpty()) {
                depositItemList.addAll(
                    BitBDUtil.getResultListFromAllTypeLists(
                        searchableText,
                        searchListsCollection,
                        depositItemListFromServer
                    )
                )
            } else {
                depositItemList.addAll(depositItemListFromServer)
            }
            adapter.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun getExpectedData(viewModel: DepositViewModel) {
        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                }
                else loading?.dismiss()
            }
        }



        viewModel.deposit.observe(viewLifecycleOwner) {
            if (it != null) {
                val baseResponse = it.data ?: return@observe
                depositItemListFromServer.clear()
                depositItemList.clear()
                for (element in baseResponse) {
                    depositItemListFromServer.add(element)

                }
                depositItemList.addAll(depositItemListFromServer)
                adapter.notifyDataSetChanged()
                createExpectedSearchList(depositItemList, loading)
            }
        }


        slideshowViewModel?.depositDelete?.observe(viewLifecycleOwner){
            if(it != null && isDeletePressed){
                isDeletePressed = false
                lifecycleScope.launch{
                    viewModel.deposit(requireContext())
                }
            }
        }


        lifecycleScope.launch {
            viewModel.deposit(requireContext())
        }


    }

    var dateList: MutableList<String> = ArrayList()
    var accNameList: MutableList<String> = ArrayList()
    var trxAccNoList: MutableList<String> = ArrayList()
    var trxTypeList: MutableList<String> = ArrayList()
    var trxIdList: MutableList<String> = ArrayList()
    var amountList: MutableList<String> = ArrayList()
    var statusList: MutableList<String> = ArrayList()

    var searchListsCollection: MutableList<List<String>> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createExpectedSearchList(
        depositItemList: MutableList<DepositDataResponse>,
        loading: LoadingProgress?
    ) {
        clearAllSearchList()
        for (depositItem in depositItemList) {
            dateList.add(
                ZonedDateTime.parse(depositItem.createdAt)
                     .withZoneSameInstant(ZoneId.systemDefault())
                     .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                      )
            )
            depositItem.trxName?.let { accNameList.add(it) }
            depositItem.trxId?.let { trxIdList.add(it) }
            depositItem.trxType?.let { trxTypeList.add(it) }
            depositItem.amount?.let { amountList.add(it.toString()) }
            depositItem.vtrxAccount?.let { trxAccNoList.add(it) }
            depositItem.status?.let { statusList.add(it) }
        }

        searchListsCollection.add(dateList)
        searchListsCollection.add(accNameList)
        searchListsCollection.add(trxIdList)
        searchListsCollection.add(trxTypeList)
        searchListsCollection.add(amountList)
        searchListsCollection.add(trxAccNoList)
        searchListsCollection.add(statusList)

        loading?.dismiss()
    }


    lateinit var adapter: DepositItemAdapter
    private var depositItemList: MutableList<DepositDataResponse> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayAdapterForDeposits() {
        adapter = DepositItemAdapter(depositItemList, ::onAdapterItemClick, requireContext())
        binding.depositRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.depositRecycle.adapter = adapter
        binding.depositRecycle.adapter?.notifyDataSetChanged()
    }

    private var currentPosition: Int? = null
    private var isDeletePressed = false
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onAdapterItemClick(position: Int) {
        currentPosition = position
        BitBDUtil.showAlertDialog(requireContext(),
            "Attention!",
            "Do you really want to delete this ?",
            "Agree","Cancel",::performDeleteInfo)

    }

    private fun performDeleteInfo() {
        isDeletePressed = true
        lifecycleScope.launch{
            slideshowViewModel?.deleteDeposit(requireContext(),
                depositItemListFromServer[currentPosition!!].id.toString())
        }
    }


    private fun clearAllSearchList() {
        dateList.clear()
        accNameList.clear()
        trxAccNoList.clear()
        trxTypeList.clear()
        trxIdList.clear()
        amountList.clear()
        statusList.clear()
        searchListsCollection.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        if( preferences?.getAnyChange() == true){
            preferences?.setAnyChange(false)
            lifecycleScope.launch {
                slideshowViewModel?.deposit(requireContext())
            }
        }
    }

}