package mx.axa.insurance_policy_list.data.external

import mx.axa.insurance_policy_list.data.external.interceptor.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(private val networkInterceptor: NetworkConnectionInterceptor, private val logInterceptor: HttpLoggingInterceptor) {

    private val httpClient = OkHttpClient.Builder().apply {
        readTimeout(1, TimeUnit.MINUTES)
        writeTimeout(1, TimeUnit.MINUTES)
        connectTimeout(1, TimeUnit.MINUTES)
        addInterceptor(networkInterceptor)
        addInterceptor(logInterceptor)
    }.build()

    var endpoints: Endpoints = Retrofit.Builder()
        .baseUrl("https://rodrigo.aartek.info/examenaxa/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(Endpoints::class.java)

}