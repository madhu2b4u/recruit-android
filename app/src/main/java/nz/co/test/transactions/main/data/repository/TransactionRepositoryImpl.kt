package nz.co.test.transactions.main.data.repository

import androidx.lifecycle.liveData
import nz.co.test.transactions.main.data.remote.source.TransactionRemoteDataSource
import nz.co.test.transactions.util.Result
import javax.inject.Inject

/**
 * Implementation of TransactionRepository interface.
 * This class is responsible for fetching transaction data and emitting it as LiveData.
 *
 * @property remoteDataSource The data source used to fetch transaction data.
 */

internal class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionRemoteDataSource,
) : TransactionRepository {
    /**
     * Fetches transactions and emits the result as LiveData.
     *
     * @return LiveData wrapping a Result containing a List of Transaction objects.
     */

    override suspend fun getTransactions() = liveData {
        emit(Result.loading())
        try {
            // Attempt to fetch transactions from the remote data source
            val response = remoteDataSource.getTransaction()
            emit(Result.success(response))

        } catch (exception: Exception) {
            // If an exception occurs, emit error state

            emit(Result.error(exception.message ?: "", null))
        }
    }
}