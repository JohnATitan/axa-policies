package mx.axa.insurance_policy_list.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.axa.insurance_policy_list.data.external.ConnectivityUtil
import mx.axa.insurance_policy_list.data.external.Repository
import mx.axa.insurance_policy_list.data.external.RetrofitClient
import mx.axa.insurance_policy_list.data.external.interceptor.NetworkConnectionInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AxaModule {

    @Provides
    @Singleton
    fun provideNetworkInterceptor(connectivityUtil: ConnectivityUtil): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(connectivityUtil)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideConnectivityInterceptor(application: Application): ConnectivityUtil {
        return ConnectivityUtil(application)
    }

    @Provides
    @Singleton
    fun provideRepository(networkConnectionInterceptor: NetworkConnectionInterceptor, httpLoggingInterceptor: HttpLoggingInterceptor): RetrofitClient {
        return RetrofitClient(networkConnectionInterceptor, httpLoggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(retrofitClient: RetrofitClient): Repository {
        return Repository(retrofitClient)
    }
}