package mx.axa.insurance_policy_list.data.external.network

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No internet connection"
}