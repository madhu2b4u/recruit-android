package nz.co.test.transactions


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.main.data.remote.source.TransactionRemoteDataSource
import nz.co.test.transactions.main.data.repository.TransactionRepository
import nz.co.test.transactions.main.data.repository.TransactionRepositoryImpl
import nz.co.test.transactions.util.Status
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class TransactionRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repositoryTest: TransactionRepository

    @Mock
    lateinit var remoteDataStore: TransactionRemoteDataSource

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

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        remoteDataStore = Mockito.mock(TransactionRemoteDataSource::class.java)
        repositoryTest = TransactionRepositoryImpl(remoteDataStore)
    }

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun getTransactions_success() {

        CoroutineScope(dispatcher).launch {
            // Arrange
            whenever(remoteDataStore.getTransaction()).doReturn(mockTransactionList)

            // Act
            val result = repositoryTest.getTransactions()

            // Assert
            assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)
            delay(2500) // Simulate the delay
            assert(LiveDataTestUtil.getValue(result).status == Status.SUCCESS)
            assert(LiveDataTestUtil.getValue(result).data == result)
        }

    }

    @Test(expected = Exception::class)
    fun getTransactions_throwsException(): Unit = runTest {
        // Arrange
        whenever(remoteDataStore.getTransaction()).thenThrow(Exception("no data"))

        // Act
        repositoryTest.getTransactions()
    }

}