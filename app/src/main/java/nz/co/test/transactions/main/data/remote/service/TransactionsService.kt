package nz.co.test.transactions.main.data.remote.service

import kotlinx.coroutines.Deferred
import nz.co.test.transactions.main.data.models.Transaction
import retrofit2.Response
import retrofit2.http.GET

//Service interface for handling transaction-related API calls.

interface TransactionsService {

    /**
     * Retrieves a list of transactions from the server.
     *
     * @return A Deferred Response containing a List of Transaction objects.
     *         The use of Deferred allows for asynchronous handling of the network request.
     * @GET annotation specifies this is a GET request to the "credit_cards" endpoint.
     */

    @GET("500f2716604dc1e8e2a3c6d31ad01830/raw/4d73acaa7caa1167676445c922835554c5572e82/test-data.json")
    fun getTransactions(): Deferred<Response<List<Transaction>>>
}

