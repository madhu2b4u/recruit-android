package nz.co.test.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.main.data.remote.service.TransactionsService
import nz.co.test.transactions.main.data.remote.source.TransactionRemoteDataSource
import nz.co.test.transactions.main.data.remote.source.TransactionRemoteDataSourceImpl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response

@ExperimentalCoroutinesApi
class TransactionsRemoteDataSourceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remoteDataSource: TransactionRemoteDataSource

    private lateinit var service: TransactionsService

    private val coroutineContext = Dispatchers.Default

    private val dispatcher = StandardTestDispatcher()

    // Mock data for testing
    private val mockTransactionList = listOf(
        Transaction(
            id = 1,
            transactionDate = "2021-04-09T16:46:44",
            summary = "Sample Transaction",
            credit = 4325.0,
            debit = 0.0
        )
    )

    @OptIn(DelicateCoroutinesApi::class)
    @Before
    fun init() {
        // Initialize mock service with a successful response
        service = mock {
            onBlocking { getTransactions() } doReturn GlobalScope.async {
                Response.success(mockTransactionList)
            }
        }

        // Initialize the data source with mock service
        remoteDataSource = TransactionRemoteDataSourceImpl(service, coroutineContext)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun testGetTransaction() {
        CoroutineScope(dispatcher).launch {
            // Re-initialize mock service with successful response
            service = mock {
                onBlocking { getTransactions() } doReturn GlobalScope.async {
                    Response.success(mockTransactionList)
                }
            }

            // Re-initialize data source with new mock service
            remoteDataSource = TransactionRemoteDataSourceImpl(service, coroutineContext)

            // Execute the method under test
            val result = remoteDataSource.getTransaction()

            // Assert that the result matches our mock data
            assert(result == mockTransactionList)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test(expected = Exception::class)
    fun testThrowGetTransactionException() = TestScope().runTest {
        // Initialize mock service with an error response
        service = mock {
            onBlocking { getTransactions() } doReturn GlobalScope.async {
                Response.error(404, null)
            }
        }

        // Re-initialize data source with new mock service
        remoteDataSource = TransactionRemoteDataSourceImpl(service, coroutineContext)

        // This should throw an exception due to the error response
        remoteDataSource.getTransaction()
    }
}