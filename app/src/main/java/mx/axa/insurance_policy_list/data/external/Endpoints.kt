package mx.axa.insurance_policy_list.data.external

import mx.axa.insurance_policy_list.data.external.dto.ServiceDto
import mx.axa.insurance_policy_list.data.model.Login
import mx.axa.insurance_policy_list.data.model.Policy
import mx.axa.insurance_policy_list.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Endpoints {
    @POST("login.php")
    fun login(@Body login: Login): Call<ServiceDto<User>>

    @GET("policy.php?")
    fun getPolicyList(@Query("id") id: Int): Call<ServiceDto<List<Policy>>>
}
