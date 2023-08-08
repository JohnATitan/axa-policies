package mx.axa.insurance_policy_list.data.external.dto

data class ServiceDto<T>(
    val success: Boolean,
    val data: T,
    val errorMessage: String,
)