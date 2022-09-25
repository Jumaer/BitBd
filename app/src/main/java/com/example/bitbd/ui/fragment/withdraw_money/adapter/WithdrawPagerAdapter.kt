package com.example.bitbd.ui.fragment.withdraw_money.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bitbd.ui.fragment.withdraw_money.AddNewWithdrawFragment
import com.example.bitbd.ui.fragment.withdraw_money.ViewWithdrawFragment

class WithdrawPagerAdapter (fragment: FragmentActivity) :
    FragmentStateAdapter(fragment){
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> ViewWithdrawFragment()
            1 -> AddNewWithdrawFragment()
            else -> {throw Resources.NotFoundException("Position not found")}

        }
    }
}