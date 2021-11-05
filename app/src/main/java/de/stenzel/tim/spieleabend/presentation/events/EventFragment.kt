package de.stenzel.tim.spieleabend.presentation.events

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.livedatapermission.PermissionManager
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.EventFragmentBinding
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import de.stenzel.tim.spieleabend.helpers.showToast
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment(), PermissionManager.PermissionObserver {

    private var _binding: EventFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels()

    @Inject
    lateinit var eventsAdapter : EventAdapter

    private var loggedIn = false
    private var permissionGranted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = EventFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionGranted = false

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.eventsRv.hasFixedSize()
        binding.eventsRv.layoutManager = manager
        binding.eventsRv.adapter = eventsAdapter

        binding.eventsRv.addItemDecoration(StickyHeaderItemDecoration(binding.eventsRv, eventsAdapter))

        eventsAdapter.onEventItemClick = { eventModel ->
            val action = EventFragmentDirections.actionEventFragmentToEventDetailFragment(eventModel)
            findNavController().navigate(action)
        }

        viewModel.events.observe(viewLifecycleOwner, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideProgressbar()
                    response.data?.let { eventList ->
                        eventsAdapter.setData(eventList)
                    }
                }
                Status.ERROR -> {
                    hideProgressbar()
                    response.message?.let { message ->
                        showErrorView(message)
                    }
                }
                Status.LOADING -> {
                    showProgressbar()
                    hideErrorView()
                }
            }
        })

        viewModel.location.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { locationModel ->
                        eventsAdapter.setLocationData(locationModel.latitude, locationModel.longitude)
                    }
                }
                Status.ERROR -> {
                    resource.message?.let { message ->
                        showToast(message)
                    }
                }
            }
        })

        binding.errorView.errorRetryBtn.setOnClickListener {
            startLoading()
        }
        
        binding.eventFabAdd.setOnClickListener {
            if (loggedIn) {
                findNavController().navigate(R.id.action_eventFragment_to_eventCreationFragment)
            } else {
                showToast(getString(R.string.login_please))
            }
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner, { isLoggedIn ->
            loggedIn = isLoggedIn
        })

        startLoading()
    }

    private fun requestPermissions() {
        PermissionManager.requestPermissions(this@EventFragment,
            EventCreationFragment.REQUEST_ID,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    //observe permission result
    override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
        permissionResultLiveData.observe(this@EventFragment, {
            when (it) {
                is PermissionResult.PermissionGranted -> {
                    if (it.requestCode == EventCreationFragment.REQUEST_ID) {
                        permissionGranted = true
                        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            viewModel.getCurrentLocation()
                        } else {
                            showToast("Bitte GPS aktivieren")
                        }
                    }
                }
                is PermissionResult.PermissionDenied -> {
                    if (it.requestCode == EventCreationFragment.REQUEST_ID) {
                        showToast(getString(R.string.permission_denied))
                    }
                }
                is PermissionResult.PermissionDeniedPermanently -> {
                    if (it.requestCode == EventCreationFragment.REQUEST_ID) {
                        showToast(getString(R.string.permission_denied_permanent))
                    }
                }
                is PermissionResult.ShowRational -> {
                    if (it.requestCode == EventCreationFragment.REQUEST_ID) {
                        shouldShowRequestPermissionRationale(getString(R.string.permission_ex_storage_rationale))
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.statusCheck()
    }

    private fun startLoading() {
        requestPermissions()
        showProgressbar()
        if (isNetworkAvailable(requireContext())) {
            viewModel.getAllEvents()
        } else {
            hideProgressbar()
            showErrorView("No internet connection")
        }
    }

    private fun showProgressbar() {
        binding.progressbar.root.show()
    }

    private fun hideProgressbar() {
        binding.progressbar.root.hide()
    }

    private fun showErrorView(message: String) {
        binding.eventsRv.visibility = View.GONE
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorTextview.text = message
    }

    private fun hideErrorView() {
        binding.eventsRv.visibility = View.VISIBLE
        binding.errorView.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}