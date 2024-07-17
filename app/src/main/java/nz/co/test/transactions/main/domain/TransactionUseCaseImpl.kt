package nz.co.test.transactions.main.domain

import nz.co.test.transactions.main.data.repository.TransactionRepository

import javax.inject.Inject
import javax.inject.Singleton


//This class is responsible for executing the business logic related to fetching transactions.

@Singleton
internal class TransactionUseCaseImpl @Inject constructor(private val repository: TransactionRepository) :
    TransactionUseCase {

    /**
     * Retrieves transactions by delegating to the repository.
     *
     * @return LiveData wrapping a Result containing a List of Transaction objects,
     *         as provided by the repository.
     */

    override suspend fun getTransactions() = repository.getTransactions()
}
