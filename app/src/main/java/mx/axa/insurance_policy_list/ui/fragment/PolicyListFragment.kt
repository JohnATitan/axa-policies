package mx.axa.insurance_policy_list.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mx.axa.insurance_policy_list.domain.viewmodel.PolicyListViewModel
import mx.axa.insurance_policy_list.R
import mx.axa.insurance_policy_list.data.model.Policy
import mx.axa.insurance_policy_list.data.model.User
import mx.axa.insurance_policy_list.databinding.FragmentPolicyListBinding
import mx.axa.insurance_policy_list.ui.adapter.PolicyAdapter
import mx.axa.insurance_policy_list.ui.adapter.PolicyCallback

@AndroidEntryPoint
class PolicyListFragment : Fragment(), PolicyCallback {
    private lateinit var binding: FragmentPolicyListBinding

    private lateinit var user: User

    private val viewModel: PolicyListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = it.getParcelable("user")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPolicyListBinding.inflate(inflater)

        binding.tvUserName.text = "Hola ${user.name}"
        binding.tvUserEmail.text = "${user.email}"

        Glide.with(this)
            .load(user.avatarUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
            .into(binding.ivAvatar)

        viewModel.isEmpty.observe(viewLifecycleOwner) {
            binding.clEmpty.visibility = View.VISIBLE
            binding.nsvPolicy.visibility = View.GONE
        }

        with(binding.rvAuto) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.autoPolicies.observe(viewLifecycleOwner) {
                adapter = PolicyAdapter(it, this@PolicyListFragment)
            }
            isNestedScrollingEnabled = false
        }

        with(binding.rvDamage) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.damagePolicies.observe(viewLifecycleOwner) {
                adapter = PolicyAdapter(it, this@PolicyListFragment)
            }
            isNestedScrollingEnabled = false
        }

        with(binding.rvHealth) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.healthPolicies.observe(viewLifecycleOwner) {
                adapter = PolicyAdapter(it, this@PolicyListFragment)
            }
            isNestedScrollingEnabled = false
        }

        with(binding.rvLife) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.lifePolicies.observe(viewLifecycleOwner) {
                adapter = PolicyAdapter(it, this@PolicyListFragment)
            }
            isNestedScrollingEnabled = false
        }

        binding.tvAuto.setOnClickListener {
            val visibility = binding.rvAuto.visibility
            binding.rvAuto.visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        binding.tvDamage.setOnClickListener {
            val visibility = binding.rvDamage.visibility
            binding.rvDamage.visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        binding.tvHealth.setOnClickListener {
            val visibility = binding.rvHealth.visibility
            binding.rvHealth.visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        binding.tvLife.setOnClickListener {
            val visibility = binding.rvLife.visibility
            binding.rvLife.visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        viewModel.message.observe(viewLifecycleOwner) {
            showSnackBar(it)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPolicyList()
    }

    private fun showSnackBar(message: String) {
        val mySnackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
            setAction("OK") { dismiss() }
            setBackgroundTint(requireContext().getColor(R.color.sienna_200))
            setTextColor(requireContext().getColor(R.color.white))
            setActionTextColor(requireContext().getColor(R.color.white))
        }
        mySnackBar.show()
    }

    override fun onPolicySelected(policy: Policy) {
        val bundle = Bundle()
        bundle.putParcelable("policy", policy)
        findNavController().navigate(R.id.action_policyListFragment_to_policyDetailFragment, bundle)
    }
}
