package nz.co.test.transactions.main.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import nz.co.test.transactions.R
import nz.co.test.transactions.databinding.FragmentTransactionDetailBinding
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.util.BaseFragment
import nz.co.test.transactions.util.toReadableString
import java.text.DecimalFormat

@AndroidEntryPoint
class TransactionDetailFragment :
    BaseFragment<FragmentTransactionDetailBinding>(FragmentTransactionDetailBinding::inflate) {

    private val args: TransactionDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {

        val transaction = args.transaction
        binding.apply {
            summary.text = transaction.summary
            transactionDate.text = transaction.transactionDate.toReadableString()
            amount.text = if (transaction.debit != 0.0) {
                amount.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
                getString(R.string.debit, transaction.debit.toString())
            } else {
                amount.setTextColor(requireContext().getColor(android.R.color.holo_green_dark))
                getString(R.string.credit, transaction.credit.toString())
            }
            gst.text = getString(R.string.gst, calculateGST(transaction))
        }
    }

    private fun calculateGST(transaction: Transaction): String {
        val decimalFormat = DecimalFormat("#.00")
        return if (transaction.debit != 0.0) {
            decimalFormat.format(transaction.debit * 0.15)
        } else {
            decimalFormat.format(transaction.credit * 0.15)
        }
    }
}