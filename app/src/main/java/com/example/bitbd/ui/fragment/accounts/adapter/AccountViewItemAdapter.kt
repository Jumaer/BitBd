package com.example.bitbd.ui.fragment.accounts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.R
import com.example.bitbd.databinding.LayoutAccountItemBinding
import com.example.bitbd.ui.fragment.accounts.model.AccountViewObject


class AccountViewItemAdapter (private val itemList: List<AccountViewObject>,
                              private val onItemClicked: (position: Int) -> Unit,
                              private val onDeleteClicked: (position: Int) -> Unit, private val context: Context
) : RecyclerView.Adapter<AccountViewItemAdapter.AccountViewItemAdapterViewHolder>() {

    inner class AccountViewItemAdapterViewHolder(
        val binding: LayoutAccountItemBinding,
        private val onItemClicked: (position: Int) -> Unit,
        private val onDeleteClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deletePerform.setOnClickListener {
                onDeleteClicked(bindingAdapterPosition)
            }
            binding.editPerform.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewItemAdapter.AccountViewItemAdapterViewHolder {
        val binding = LayoutAccountItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewItemAdapterViewHolder(binding, onItemClicked,onDeleteClicked)
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: AccountViewItemAdapter.AccountViewItemAdapterViewHolder,
        position: Int
    ) {
        with(holder) {
            with(itemList[position]) {
                 binding.action.visibility = View.GONE
                 binding.ActionPerform.visibility = View.VISIBLE
                 binding.name.text = this.name
                 binding.type.text = this.type
                 binding.accountNo.text = this.account
                 if(this.branch == null || this.branch == "null" ){
                     binding.branch.text = "N/A"
                 }
                 else{
                     binding.branch.text = this.branch
                 }

                 if(this.status == "1" ){
                      binding.status.text = "Active"
                      binding.status.setTextColor(ContextCompat.getColor(context, R.color.white))
                      binding.status.background = ContextCompat.getDrawable(context, R.drawable.item_shape_approved)
                 }
                 else{
                     binding.status.text = "Inactive"
                     binding.status.setTextColor(ContextCompat.getColor(context, R.color.white))
                     binding.status.background = ContextCompat.getDrawable(context,
                         R.drawable.item_shape_delete
                     )
                 }


                 binding.deletePerform.background = ContextCompat.getDrawable(context, R.drawable.item_shape_delete)
                 binding.editPerform.background = ContextCompat.getDrawable(context, R.drawable.edit_background)



            }
        }
    }

    override fun getItemCount() = itemList.size
}