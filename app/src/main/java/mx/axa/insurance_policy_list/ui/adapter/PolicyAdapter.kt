package mx.axa.insurance_policy_list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.axa.insurance_policy_list.data.model.Policy
import mx.axa.insurance_policy_list.databinding.AdapterPolicyBinding
import java.text.NumberFormat
import java.util.*

class PolicyAdapter(val policies: List<Policy>, val callback: PolicyCallback) : RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AdapterPolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(policies[position])
    }

    override fun getItemCount(): Int {
        return policies.size
    }


    inner class ViewHolder(val binding: AdapterPolicyBinding, val callback: PolicyCallback) : RecyclerView.ViewHolder(binding.root) {

        fun bind(policy: Policy) {
            binding.tvInsuredName.text = "Asegurado: ${policy.insuredName}"
            binding.tvInsuredPolicy.text = "Poliza: ${policy.policy}"
            binding.tvInsuredAmount.text = "Cantidad asegurada: ${formatNumberText(policy.insuredAmount)}"

            binding.root.setOnClickListener {
                callback.onPolicySelected(policy)
            }
        }
    }
}

interface PolicyCallback {
    fun onPolicySelected(policy: Policy)
}

fun formatNumberText(amountText: String): String {
    val amount = amountText.toLongOrNull() ?: return amountText
    val numberFormat = NumberFormat.getNumberInstance(Locale("es", "MX"))
    return "$" + numberFormat.format(amount)
}


