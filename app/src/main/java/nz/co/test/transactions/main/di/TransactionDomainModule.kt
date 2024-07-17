package nz.co.test.transactions.main.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.test.transactions.main.data.repository.TransactionRepository
import nz.co.test.transactions.main.data.repository.TransactionRepositoryImpl
import nz.co.test.transactions.main.domain.TransactionUseCase
import nz.co.test.transactions.main.domain.TransactionUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class TransactionDomainModule {

    /**
     * Binds the implementation of TransactionRepository to its interface.
     *
     * @param repoImpl The concrete implementation of TransactionRepository.
     * @return An instance of TransactionRepository.
     */

    @Binds
    internal abstract fun bindRepository(
        repoImpl: TransactionRepositoryImpl
    ): TransactionRepository

    /**
     * Binds the implementation of TransactionUseCase to its interface.
     *
     * @param useCaseImpl The concrete implementation of TransactionUseCase.
     * @return An instance of TransactionUseCase.
     */


    @Binds
    internal abstract fun bindsUseCase(
        useCaseImpl: TransactionUseCaseImpl
    ): TransactionUseCase

}