package com.example.bitbd.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitbd.animation.LoadingProgress
import com.example.bitbd.databinding.FragmentHomeBinding
import com.example.bitbd.ui.fragment.deposit.adapter.PaymentItemsAdapter
import com.example.bitbd.ui.fragment.deposit.model.PaymentMethod
import com.example.bitbd.ui.fragment.home.adapter.DashboardAdapter
import com.example.bitbd.ui.fragment.home.model.custom.CustomView
import com.example.bitbd.util.BitBDUtil
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
         val root: View = binding.root
         createDisplayForDashBoard()
         setObservers(homeViewModel)

        lifecycleScope.launch {
            homeViewModel.dashboardData(requireContext())
        }


        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers(viewModel: HomeViewModel) {

        var loading: LoadingProgress? = null

        viewModel.progress.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    loading = BitBDUtil.showProgress(requireContext())
                } else loading?.dismiss()
            }
        }



        viewModel.dashBoard.observe(viewLifecycleOwner) {
            if(it != null){
                val baseObject = it.data
                loading?.dismiss()
                if(baseObject != null){
                    itemList.clear()
                    itemList.add(CustomView("In Total Number","Deposit",baseObject.deposit.toString()))
                    itemList.add(CustomView("In Total Number","Withdraw",baseObject.withdraw.toString()))
                    itemList.add(CustomView("In Total Amount","Balance",baseObject.balance.toString()))
                    adapter.notifyDataSetChanged()
                }

            }

        }

        }


    lateinit var adapter: DashboardAdapter
    private var itemList: MutableList<CustomView> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    private fun createDisplayForDashBoard() {
        adapter = DashboardAdapter(itemList, ::onAdapterItemClick, requireContext())
        binding.dashBoardRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.dashBoardRecycle.adapter = adapter
        binding.dashBoardRecycle.adapter?.notifyDataSetChanged()

    }

    private fun onAdapterItemClick(position: Int) {

        Log.d("Implement" , "Not yet for $position")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}