package nz.co.test.transactions.main.presentation.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.main.domain.TransactionUseCase
import nz.co.test.transactions.util.Result
import nz.co.test.transactions.util.SingleLiveEvent
import nz.co.test.transactions.util.Status
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase,
) : ViewModel(), LifecycleObserver {


    val transactionsResult: LiveData<Result<List<Transaction>>> get() = _transactionsResult
    private val _transactionsResult = MediatorLiveData<Result<List<Transaction>>>()


    val loader: LiveData<Unit> get() = _loader
    private val _loader = SingleLiveEvent<Unit>()

    val errorData: LiveData<Unit> get() = _errorData
    private val _errorData = SingleLiveEvent<Unit>()

    // Initialize by fetching transactions
    init {
        getTransactions()
    }

    // Function to fetch transactions

    fun getTransactions() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _transactionsResult.addSource(transactionUseCase.getTransactions()) {
                    getTransactionsResult(it)
                }
            }
        }
    }

    // Handle the result of fetching transactions

    private fun getTransactionsResult(result: Result<List<Transaction>>) {
        when (result.status) {
            Status.LOADING -> {
                _loader.call()
            }

            Status.ERROR -> {
                _errorData.call()
            }

            Status.SUCCESS -> {
                handleResultData(result)
            }
        }
    }

    // Handle the result of fetching transactions

    private fun handleResultData(result: Result<List<Transaction>>) {
        result.data.let {
            _transactionsResult.postValue(result)
        }

    }
}
