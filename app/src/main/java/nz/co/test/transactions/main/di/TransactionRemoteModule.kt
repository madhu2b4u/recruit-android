package nz.co.test.transactions.main.di


import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.test.transactions.main.data.remote.service.TransactionsService
import nz.co.test.transactions.main.data.remote.source.TransactionRemoteDataSource
import nz.co.test.transactions.main.data.remote.source.TransactionRemoteDataSourceImpl
import retrofit2.Retrofit

// This module is installed in the SingletonComponent, ensuring application-wide singletons.
@Module(includes = [TransactionRemoteModule.Binders::class])
@InstallIn(SingletonComponent::class)
class TransactionRemoteModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Binders {

        /**
         * Binds the implementation of TransactionRemoteDataSource to its interface.
         *
         * @param remoteDataSourceImpl The concrete implementation of TransactionRemoteDataSource.
         * @return An instance of TransactionRemoteDataSource.
         */

        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: TransactionRemoteDataSourceImpl
        ): TransactionRemoteDataSource
    }

    /**
     * Provides an instance of TransactionsService.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return An implementation of TransactionsService.
     */

    @Provides
    fun provideTransactionService(retrofit: Retrofit): TransactionsService =
        retrofit.create(TransactionsService::class.java)
}