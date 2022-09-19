package com.example.bitbd.ui.fragment.affiliate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.bitbd.databinding.FragmentAffiliateBinding

import com.example.bitbd.ui.fragment.profile.ProfileViewModel


class AffiliateFragment : Fragment() {
    private var _binding: FragmentAffiliateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val affiliateViewModel =
            ViewModelProvider(this)[AffiliateViewModel::class.java]

        _binding = FragmentAffiliateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        affiliateViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}