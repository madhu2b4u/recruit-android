package nz.co.test.transactions.main.presentation.fragments

import TransactionAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nz.co.test.transactions.databinding.FragmentTransactionsBinding
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.main.presentation.viewmodel.TransactionViewModel
import nz.co.test.transactions.util.BaseFragment

@AndroidEntryPoint
class TransactionsFragment :
    BaseFragment<FragmentTransactionsBinding>(FragmentTransactionsBinding::inflate) {

    private val viewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {

        observeOnViewModel()
    }

    // Set up observers for the ViewModel's LiveData
    private fun observeOnViewModel() = with(viewModel) {
        lifecycle.addObserver(this)

        loader.observe(viewLifecycleOwner) {
            showLoader()
        }

        errorData.observe(viewLifecycleOwner) {
            showError()
        }

        transactionsResult.observe(viewLifecycleOwner) {
            if (it.data.isNullOrEmpty()) {
                showError()
            } else {
                handleTransactionResult(it.data)
            }

        }
    }

    // Handle the transaction result
    private fun handleTransactionResult(it: List<Transaction>) {
        if (it.isNotEmpty()) {
            updateTransactionList(it)
            hideLoader()
        } else {
            showError()
        }
    }

    // Update the RecyclerView with the transaction list

    private fun updateTransactionList(transactions: List<Transaction>) {
        val list = sortTransactionsByDate(transactions)
        binding.rvTransactionList.layoutManager = LinearLayoutManager(this.context)
        val transactionAdapter = TransactionAdapter()
        binding.rvTransactionList.adapter = transactionAdapter
        transactionAdapter.updateList(list)
        transactionAdapter.onClickListener {
            val action = TransactionsFragmentDirections
                .actionTransactionListFragmentToTransactionDetailFragment(it)
            findNavController().navigate(action)

        }
    }

    private fun sortTransactionsByDate(transactions: List<Transaction>): List<Transaction> {
        return transactions.sortedByDescending { it.transactionDate }
    }

    // Hide loader and show RecyclerView
    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
        binding.tvErrorText.visibility = View.GONE
        binding.rvTransactionList.visibility = View.VISIBLE
    }

    // Show loader and hide other views
    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvErrorText.visibility = View.GONE
        binding.rvTransactionList.visibility = View.GONE
    }

    // Show error message and hide other views
    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.tvErrorText.visibility = View.VISIBLE
        binding.rvTransactionList.visibility = View.GONE
    }
}