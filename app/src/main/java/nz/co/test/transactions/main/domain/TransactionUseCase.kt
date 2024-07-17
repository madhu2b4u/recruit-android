package nz.co.test.transactions.main.domain

import androidx.lifecycle.LiveData
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.util.Result

// This interface represents a single business logic operation in the domain layer.
interface TransactionUseCase {

    /**
     * Retrieves a list of transactions.
     *
     * @return A LiveData object wrapping a Result containing a List of Transaction objects.
     *         This allows for observing the asynchronous operation and handling loading, success, and error states.
     */

    suspend fun getTransactions(): LiveData<Result<List<Transaction>>>
}