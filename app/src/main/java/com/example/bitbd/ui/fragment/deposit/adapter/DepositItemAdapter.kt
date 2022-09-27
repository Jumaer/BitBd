package com.example.bitbd.ui.fragment.deposit.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.R
import com.example.bitbd.constant.INFO
import com.example.bitbd.databinding.LayoutDipositItemListBinding
import com.example.bitbd.ui.fragment.deposit.model.DepositDataResponse
import com.example.bitbd.util.BitBDUtil
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DepositItemAdapter (
    private val depositItemList: List<DepositDataResponse>,
    private val onItemClicked: (position: Int) -> Unit,private val context: Context
) : RecyclerView.Adapter<DepositItemAdapter.DepositItemAdapterViewHolder>() {

    inner class DepositItemAdapterViewHolder(
        val binding: LayoutDipositItemListBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {

            binding.ActionPerform.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if(depositItemList[adapterPosition].status == "Approved"){
                BitBDUtil.showMessage("Already Approved", INFO)
            }
            else{
                onItemClicked(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DepositItemAdapter.DepositItemAdapterViewHolder {
        val binding = LayoutDipositItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DepositItemAdapterViewHolder(binding, onItemClicked)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: DepositItemAdapter.DepositItemAdapterViewHolder,
        position: Int
    ) {
        with(holder) {
            with(depositItemList[position]) {
               binding.Action.visibility = View.GONE
               binding.ActionPerform.visibility = View.VISIBLE
               binding.Date.text = ZonedDateTime.parse(this.createdAt)
                   .withZoneSameInstant(ZoneId.of("Asia/Dhaka"))
                   .format(
                       DateTimeFormatter.ofPattern(
                           "dd MMMM yyyy"
                       )
                   )

                binding.AccountName.text = this.trxName
                binding.TrxID.text = this.trxId
                binding.TrxType.text = this.trxType
                binding.Amount.text = this.amount.toString()
                binding.TrxAccNumber.text = this.vtrxAccount
                binding.Status.text = this.status
                if(this.status == "Pending"){
                    binding.Status.background = ContextCompat.getDrawable(context, R.drawable.item_shape_pending)
                   //     binding.Status.setTextColor(ContextCompat.getColor(context, R.color.white))
                    binding.ActionPerform.background = ContextCompat.getDrawable(context, R.drawable.item_shape_delete)
                }
                else if(this.status == "Approved"){
                    binding.Status.background = ContextCompat.getDrawable(context, R.drawable.item_shape_approved)
                    binding.ActionPerform.background = ContextCompat.getDrawable(context,
                        R.drawable.item_shape_not_delete
                    )
                }
               // binding.Status.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    override fun getItemCount() = depositItemList.size
}