package com.example.bitbd.ui.fragment.withdraw_money

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.bitbd.databinding.FragmentDepositsContainerBinding
import com.example.bitbd.databinding.FragmentWithdrawBinding
import com.example.bitbd.ui.fragment.deposit.adapter.DepositPagerAdapter
import com.example.bitbd.ui.fragment.withdraw_money.adapter.WithdrawPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WithdrawFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WithdrawFragment : Fragment() {

    private var _binding: FragmentWithdrawBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.viewPager.adapter = WithdrawPagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, index ->
            tab.text= when(index){
                0-> "View All"
                1-> "Add New"
                else -> {throw Resources.NotFoundException("Position not found")}
            }
        }.attach()
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WithdrawFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WithdrawFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}