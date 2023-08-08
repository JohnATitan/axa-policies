package mx.axa.insurance_policy_list.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.axa.insurance_policy_list.R
import mx.axa.insurance_policy_list.data.model.Policy
import mx.axa.insurance_policy_list.databinding.FragmentPolicyDetailBinding
import mx.axa.insurance_policy_list.ui.adapter.formatNumberText

private const val ARG_POLICY = "policy"
private const val REQUEST_CALL_PHONE = 1

class PolicyDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentPolicyDetailBinding
    lateinit var map: GoogleMap
    private lateinit var policy: Policy
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            policy = it.getParcelable(ARG_POLICY)!!
        }

        requestPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                makePhoneCall()
            } else {
                showSnackBar("Accede a tu configuracion para otorgar permisos de llamada")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPolicyDetailBinding.inflate(inflater)

        val background: Int
        val icon: Drawable?
        val iconColor: Int
        val title: String
        val textColor: Int
        when (policy.type) {
            "DANOS" -> {
                background = requireContext().getColor(R.color.damage_background)
                icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_damage)
                iconColor = requireContext().getColor(R.color.damage_complement)
                title = "Poliza de daños"
                textColor = requireContext().getColor(R.color.damage_text_color)
            }
            "VIDA" -> {
                background = requireContext().getColor(R.color.life_background)
                icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_life)
                iconColor = requireContext().getColor(R.color.life_complement)
                title = "Poliza de vida"
                textColor = requireContext().getColor(R.color.life_text_color)
            }
            "AUTO" -> {
                background = requireContext().getColor(R.color.auto_background)
                icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_car)
                iconColor = requireContext().getColor(R.color.auto_complement)
                title = "Poliza de auto"
                textColor = requireContext().getColor(R.color.auto_text_color)
            }
            else -> { // SALUD
                background = requireContext().getColor(R.color.health_background)
                icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_health)
                iconColor = requireContext().getColor(R.color.health_complement)
                title = "Poliza de salud"
                textColor = requireContext().getColor(R.color.health_text_color)
            }
        }
        binding.clMain.setBackgroundColor(background)
        binding.ivType.setImageDrawable(icon)
        binding.ivType.setColorFilter(iconColor)
        binding.tvPolicy.text = title
        binding.tvPolicy.setTextColor(textColor)

        binding.dvPolicy.setValue(policy.policy)
        binding.dvType.setValue(policy.type)
        binding.dvInsured.setValue(policy.insuredName)
        binding.dvAmount.setValue(formatNumberText(policy.insuredAmount))
        binding.dvTelephone.setValue(policy.servicePhone)
        binding.dvCoinsurance.setValue(formatNumberText(policy.coinsurance))
        binding.dvDetail.setValue(policy.detail)

        if (policy.type == "DANOS") {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else {
            binding.tvMap.visibility = View.GONE
            binding.map.visibility = View.GONE
        }

        binding.ivCall.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        val geocoder = Geocoder(requireContext())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(policy.detail, 1) {
                setLocation(it[0])
            }
        } else {
            val list = geocoder.getFromLocationName(policy.detail, 1)
            list?.let {
                setLocation(it[0])
            }
        }
    }

    private fun setLocation(address: Address) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val latLng = LatLng(address.latitude, address.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map.animateCamera(cameraUpdate)
            val marker = MarkerOptions().apply {
                position(latLng)
                draggable(false)
                title(policy.detail)
            }
            map.addMarker(marker)
        }
    }

    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val phoneNumber = "tel:" + policy.servicePhone
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse(phoneNumber)
            startActivity(callIntent)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            }
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