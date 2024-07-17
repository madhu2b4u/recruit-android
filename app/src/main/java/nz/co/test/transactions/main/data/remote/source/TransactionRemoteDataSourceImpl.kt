package nz.co.test.transactions.main.data.remote.source

import kotlinx.coroutines.withContext
import nz.co.test.transactions.di.qualifiers.IO
import nz.co.test.transactions.main.data.remote.service.TransactionsService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

//Implementation of TransactionRemoteDataSource interface for fetching transaction data.

class TransactionRemoteDataSourceImpl @Inject constructor(
    private val service: TransactionsService,
    @IO private val context: CoroutineContext
) : TransactionRemoteDataSource {

    /**
     * Fetches transactions from the remote service.
     *
     * @return A List of Transaction objects.
     * @throws HttpException if the response is unsuccessful.
     * @throws NoDataException if the response is successful but the body is null.
     * @throws IOException if there's a network or I/O related error.
     */

    override suspend fun getTransaction() = withContext(context) {
        try {
            val response = service.getTransactions().await()
            if (response.isSuccessful) {
                response.body() ?: throw NoDataException("Response body is null")
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw IOException("Network error occurred: ${e.message}", e)
        }
    }
}

//Custom exception for when no data is returned in a successful response.

class NoDataException(message: String) : Exception(message)

