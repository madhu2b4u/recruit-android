import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nz.co.test.transactions.R
import nz.co.test.transactions.databinding.ItemTransactionBinding
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.util.toReadableString
import java.text.DecimalFormat


// Adapter class for displaying a list of transactions in a RecyclerView


class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactions: List<Transaction> = emptyList()

    private var onClick: ((transaction: Transaction) -> Unit)? = null

    fun onClickListener(onClick: (Transaction) -> Unit) {
        this.onClick = onClick
    }

    fun updateList(transactions: List<Transaction>) {
        this.transactions = transactions
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {


            val context = binding.summary.context
            val decimalFormat = DecimalFormat("#.00")
            // Calculate GST amount (15% of transaction amount)
            val gstAmount = if (transaction.debit != 0.0) {
                decimalFormat.format(transaction.debit * 0.15)
            } else {
                decimalFormat.format(transaction.credit * 0.15)
            }

            binding.apply {
                summary.text = transaction.summary
                transactionDate.text = transaction.transactionDate.toReadableString()
                amount.text = if (transaction.debit != 0.0) {
                    amount.setTextColor(context.getColor(android.R.color.holo_red_dark))
                    context.getString(R.string.debit, transaction.debit.toString())
                } else {
                    amount.setTextColor(context.getColor(android.R.color.holo_green_dark))
                    context.getString(R.string.credit, transaction.credit.toString())
                }
                gst.text = context.getString(R.string.gst, gstAmount)
                itemView.setOnClickListener {
                    onClick?.invoke(transaction)
                }

                // Set content description for accessibility

                itemView.contentDescription = "Transaction with ${transaction.summary} on ${transaction.transactionDate}. Amount ${binding.amount.text}. ${binding.gst.text}."
            }
        }
    }
}
