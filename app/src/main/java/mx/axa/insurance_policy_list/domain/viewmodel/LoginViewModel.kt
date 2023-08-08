package mx.axa.insurance_policy_list.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.axa.insurance_policy_list.data.external.AxaException
import mx.axa.insurance_policy_list.data.external.Repository
import mx.axa.insurance_policy_list.data.model.Login
import mx.axa.insurance_policy_list.data.model.User
import mx.axa.insurance_policy_list.data.external.network.NoConnectivityException
import mx.axa.insurance_policy_list.ui.activity.AxaResult
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val message = MutableLiveData<String>()
    val user = MutableLiveData<User>()

    fun callLogin(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        val loginRequest = Login(email = email, password = password)
        repository.callLogin(request = loginRequest).collect { axaResult: AxaResult ->
            when (axaResult) {
                is AxaResult.Failure -> {
                    when (axaResult.error) {
                        is AxaException -> message.postValue(axaResult.error.message)
                        is NoConnectivityException -> message.postValue("Verifica tu conexion a internet")
                        else -> message.postValue("Ocurrio un error inesperado")
                    }
                }
                is AxaResult.Success<*> -> {
                    val result = axaResult.data as User
                    user.postValue(result)
                }
            }
        }
    }
}