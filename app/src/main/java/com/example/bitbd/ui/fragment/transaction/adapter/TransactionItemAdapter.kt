package com.example.bitbd.ui.fragment.transaction.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bitbd.R
import com.example.bitbd.databinding.LayoutTransactionItemBinding
import com.example.bitbd.ui.fragment.transaction.model.TransactionObject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TransactionItemAdapter( private val itemList: List<TransactionObject>,
private val onItemClicked: (position: Int) -> Unit,private val context: Context
) : RecyclerView.Adapter<TransactionItemAdapter.TransactionItemAdapterViewHolder>() {

    inner class TransactionItemAdapterViewHolder(
        val binding: LayoutTransactionItemBinding,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {

            binding.baseItem.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            onItemClicked(adapterPosition)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionItemAdapter.TransactionItemAdapterViewHolder {
        val binding = LayoutTransactionItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionItemAdapterViewHolder(binding, onItemClicked)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: TransactionItemAdapter.TransactionItemAdapterViewHolder,
        position: Int
    ) {
        with(holder) {
            with(itemList[position]) {
                binding.Date.text = ZonedDateTime.parse(this.createdAt)
                    .withZoneSameInstant(ZoneId.of("Asia/Dhaka"))
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy"
                        )
                    )
                binding.Type.text = this.type
                binding.AccountName.text = this.trxName
                binding.TrxID.text = this.trxId
                binding.TrxType.text = this.trxType
                binding.Amount.text = this.amount.toString()
                binding.TrxAccNumber.text = this.trxAccount
                binding.Status.text = this.status
                if(this.status == "Pending"){
                    binding.Status.background = ContextCompat.getDrawable(context, R.drawable.item_shape_pending)
                }
                else if(this.status == "Approved"){
                    binding.Status.background = ContextCompat.getDrawable(context, R.drawable.item_shape_approved)
                }

                else if(this.status == "Decline"){
                    binding.Status.background = ContextCompat.getDrawable(context,
                        R.drawable.item_shape_delete
                    )
                }
                // binding.Status.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    override fun getItemCount() = itemList.size
}