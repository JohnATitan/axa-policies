package mx.axa.insurance_policy_list.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.axa.insurance_policy_list.data.external.AxaException
import mx.axa.insurance_policy_list.data.external.Repository
import mx.axa.insurance_policy_list.data.model.Policy
import mx.axa.insurance_policy_list.data.external.network.NoConnectivityException
import mx.axa.insurance_policy_list.ui.activity.AxaResult
import javax.inject.Inject

@HiltViewModel
class PolicyListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val message = MutableLiveData<String>()
    val isEmpty = MutableLiveData<Boolean>()
    val healthPolicies = MutableLiveData<List<Policy>>()
    val lifePolicies = MutableLiveData<List<Policy>>()
    val damagePolicies = MutableLiveData<List<Policy>>()
    val autoPolicies = MutableLiveData<List<Policy>>()

    fun getPolicyList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getPolicyList(request = 2).collect { axaResult: AxaResult ->
            when (axaResult) {
                is AxaResult.Failure -> {
                    val error = when (axaResult.error) {
                        is AxaException -> axaResult.error.message
                        is NoConnectivityException -> "Verifica tu conexion a internet"
                        else -> "Ocurrio un error inesperado"
                    }
                    message.postValue(error)
                }
                is AxaResult.Success<*> -> {
                    val policyList = axaResult.data as List<Policy>

                    if (policyList.isEmpty()) {
                        isEmpty.postValue(true)
                    } else {
                        healthPolicies.postValue(policyList.filter { it.type == "SALUD" })
                        lifePolicies.postValue(policyList.filter { it.type == "VIDA" })
                        autoPolicies.postValue(policyList.filter { it.type == "AUTO" })
                        damagePolicies.postValue(policyList.filter { it.type == "DANOS" })
                    }
                }
            }
        }
    }
}