package nz.co.test.transactions.main.data.remote.source

import nz.co.test.transactions.main.data.models.Transaction

// Interface defining the contract for fetching transaction data from a remote source.

interface TransactionRemoteDataSource {

    /**
     * Fetches a list of transactions from a remote data source.
     *
     * @return A List of Transaction objects.
     * @throws Exception if there's an error during the data fetch operation.
     */

    suspend fun getTransaction(): List<Transaction>
}