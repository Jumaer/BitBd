package com.example.bitbd.ui.activity.accounts.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bitbd.ui.fragment.accounts.UpdateInformationFragment
import com.example.bitbd.ui.fragment.accounts.ViewInformationFragment


class TabFragmentAdapter(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ViewInformationFragment()
            1 -> UpdateInformationFragment()
            else -> {
                throw Resources.NotFoundException("Position not found")
            }

        }
    }
}