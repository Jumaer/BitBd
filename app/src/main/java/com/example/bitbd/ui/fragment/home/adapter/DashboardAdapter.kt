package com.example.bitbd.ui.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.databinding.LayoutDashBoardItemBinding
import com.example.bitbd.ui.fragment.home.model.custom.CustomView


class DashboardAdapter( private val list: List<CustomView>,
private val onItemClicked: (position: Int) -> Unit, private val context: Context
) : RecyclerView.Adapter<DashboardAdapter .DashboardAdapterViewHolder>() {


    inner class DashboardAdapterViewHolder (val binding: LayoutDashBoardItemBinding,
                                       private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.baseView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClicked(bindingAdapterPosition)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapterViewHolder {
        val binding = LayoutDashBoardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: DashboardAdapterViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.value.text = this.value
                binding.title.text = this.title
                binding.subTitle.text = this.subTitle
            }
        }
    }

    override fun getItemCount() = list.size
}