package nz.co.test.transactions.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import nz.co.test.transactions.BuildConfig
import nz.co.test.transactions.di.qualifiers.IO
import nz.co.test.transactions.di.qualifiers.MainThread
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /**
     * Provides a singleton OkHttpClient instance.
     * This client is configured with:
     * - A logging interceptor for debugging
     * - Timeouts
     * - A custom interceptor for adding headers
     */

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient().newBuilder()
        client.connectTimeout(60, TimeUnit.SECONDS)
        client.readTimeout(60, TimeUnit.SECONDS)
        client.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
            chain.proceed(newRequest)
        }
        client.addInterceptor(logging)
        return client.build()
    }

    /**
     * Provides a singleton Moshi instance for JSON parsing.
     */

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Provides a singleton Retrofit instance.
     * This Retrofit instance is configured with:
     * - CoroutineCallAdapterFactory for supporting suspend functions
     * - MoshiConverterFactory for JSON parsing
     * - Base URL from BuildConfig
     * - Custom OkHttpClient
     */

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    /**
     * Provides the IO dispatcher for coroutines.
     */

    @IO
    @Provides
    fun providesIoDispatcher(): CoroutineContext = Dispatchers.IO

    /**
     * Provides the Main dispatcher for coroutines.
     */

    @MainThread
    @Provides
    fun providesMainThreadDispatcher(): CoroutineContext = Dispatchers.Main
}
