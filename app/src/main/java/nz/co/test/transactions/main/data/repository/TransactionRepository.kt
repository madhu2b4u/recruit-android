package nz.co.test.transactions.main.data.repository

import androidx.lifecycle.LiveData
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.util.Result

/**
 * Repository interface for managing transaction data.
 * This interface defines the contract for fetching transaction data,
 * abstracting the data source from the rest of the application.
 */


interface TransactionRepository {

    /**
     * Fetches a list of transactions.
     *
     * @return A LiveData object wrapping a Result containing a List of Transaction objects.
     *         This allows for observing the asynchronous operation and handling success or failure states.
     */

    suspend fun getTransactions(): LiveData<Result<List<Transaction>>>

}