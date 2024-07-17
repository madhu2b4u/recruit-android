package nz.co.test.transactions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import nz.co.test.transactions.main.data.models.Transaction
import nz.co.test.transactions.main.data.repository.TransactionRepository
import nz.co.test.transactions.main.domain.TransactionUseCase
import nz.co.test.transactions.main.domain.TransactionUseCaseImpl
import nz.co.test.transactions.util.Result
import nz.co.test.transactions.util.Status
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock


@ExperimentalCoroutinesApi
class TransactionUseCaseTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: TransactionUseCase

    private lateinit var repository: TransactionRepository

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

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun testTransactionsRequestLoading() {
        CoroutineScope(dispatcher).launch {
            repository = mock {
                onBlocking {
                    getTransactions()
                } doReturn liveData {
                    emit(Result.loading())
                }
            }

            useCase = TransactionUseCaseImpl(repository)

            val result = useCase.getTransactions()

            assert(LiveDataTestUtil.getValue(result).status == Status.LOADING)
        }
    }

    @Test
    fun testTransactionsRequestSuccess() {
        CoroutineScope(dispatcher).launch {
            repository = mock {
                onBlocking {
                    getTransactions()
                } doReturn liveData {
                    emit(Result.success(mockTransactionList))
                }
            }

            useCase = TransactionUseCaseImpl(repository)

            val result = useCase.getTransactions()

            assert(
                LiveDataTestUtil.getValue(result).data == result &&
                        LiveDataTestUtil.getValue(result).status == Status.SUCCESS
            )
        }
    }

    @Test
    fun testTransactionsRequestErrorData() {
        CoroutineScope(dispatcher).launch {
            repository = mock {
                onBlocking { getTransactions() } doReturn liveData {
                    emit(Result.error("no data"))
                }
            }
            useCase = TransactionUseCaseImpl(repository)

            val result = useCase.getTransactions()
            result.observeForever { }
            assert(
                LiveDataTestUtil.getValue(result).status == Status.ERROR && LiveDataTestUtil.getValue(
                    result
                ).message == "no data"
            )
        }

    }

}