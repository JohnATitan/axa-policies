package mx.axa.insurance_policy_list.data.external.interceptor

import mx.axa.insurance_policy_list.data.external.ConnectivityUtil
import mx.axa.insurance_policy_list.data.external.network.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(val connectivityInterceptor: ConnectivityUtil) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityInterceptor.isOnline()) {
            throw NoConnectivityException()
        }

        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}