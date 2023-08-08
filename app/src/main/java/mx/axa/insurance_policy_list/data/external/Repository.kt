package mx.axa.insurance_policy_list.data.external

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import mx.axa.insurance_policy_list.data.external.dto.ServiceDto
import mx.axa.insurance_policy_list.data.model.Login
import mx.axa.insurance_policy_list.ui.activity.AxaResult
import retrofit2.Response

class Repository(private val client: RetrofitClient) {
    suspend fun callLogin(request: Login) = flow {
        val response = client.endpoints.login(request).execute()
        process(response = response)
    }.catch { e ->
        emit(AxaResult.Failure(e))
    }

    suspend fun getPolicyList(request: Int) = flow {
        val response = client.endpoints.getPolicyList(request).execute()
        process(response = response)
    }.catch { e ->
        emit(AxaResult.Failure(e))
    }
}

class AxaException(override val message: String) : Exception()

suspend fun <T> FlowCollector<AxaResult>.process(response: Response<ServiceDto<T>>) {
    if (response.isSuccessful) {
        response.body()?.let {
            if (it.success) {
                emit(AxaResult.Success(it.data))
            } else {
                throw AxaException(message = it.errorMessage)
            }
        }
    } else {
        throw AxaException("Error code: ${response.code()}")
    }
}