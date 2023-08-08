package mx.axa.insurance_policy_list.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mx.axa.insurance_policy_list.domain.viewmodel.LoginViewModel
import mx.axa.insurance_policy_list.R
import mx.axa.insurance_policy_list.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.btnCallLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            viewModel.callLogin(email = email, password = password)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.message.observe(viewLifecycleOwner) {
            showSnackBar(it)
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            findNavController().navigate(
                resId = R.id.action_loginFragment_to_policyListFragment,
                args = bundle
            )
        }
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
}