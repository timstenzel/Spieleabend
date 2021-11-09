package de.stenzel.tim.spieleabend.presentation.events

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.livedatapermission.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.EventCreationFragmentBinding
import de.stenzel.tim.spieleabend.helpers.*
import java.util.*

@AndroidEntryPoint
class EventCreationFragment : Fragment(), PermissionManager.PermissionObserver {

    private var _binding: EventCreationFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventCreationViewModel by viewModels()

    private var imageUri: Uri? = null

    private lateinit var now: Calendar
    private lateinit var start: Calendar
    private lateinit var end: Calendar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = EventCreationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //pick image from gallery
        binding.eventCreationChooseImageBtn.setOnClickListener {
            PermissionManager.requestPermissions(this@EventCreationFragment, REQUEST_ID, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        //event time (start and end)
        now = Calendar.getInstance()
        val currYear = now.get(Calendar.YEAR)
        val currMonth = now.get(Calendar.MONTH)
        val currDay = now.get(Calendar.DAY_OF_MONTH)
        val currHour = now.get(Calendar.HOUR_OF_DAY)
        val currMinute = now.get(Calendar.MINUTE)

        start = Calendar.getInstance()
        end = Calendar.getInstance()

        binding.eventCreationStartDate.setOnClickListener { startDate ->
            val dpd = DatePickerDialog(requireActivity(), { _, year, month, day ->
                setDateToEdittext(startDate, year, month, day)
                start.set(Calendar.YEAR, year)
                start.set(Calendar.MONTH, month)
                start.set(Calendar.DAY_OF_MONTH, day)
            }, currYear, currMonth, currDay )

            dpd.show()
        }

        binding.eventCreationStartTime.setOnClickListener { startTime ->
            val tpd = TimePickerDialog(requireContext(), { _, hour, min ->
                setTimeToEdittext(startTime, hour, min)
                start.set(Calendar.HOUR_OF_DAY, hour)
                start.set(Calendar.MINUTE, min)
            }, currHour, currMinute, true)

            tpd.show()
        }

        binding.eventCreationEndDate.setOnClickListener { endDate ->
            val dpd = DatePickerDialog(requireActivity(), { _, year, month, day ->
                setDateToEdittext(endDate, year, month, day)
                end.set(Calendar.YEAR, year)
                end.set(Calendar.MONTH, month)
                end.set(Calendar.DAY_OF_MONTH, day)
            }, currYear, currMonth, currDay )

            dpd.show()
        }

        binding.eventCreationEndTime.setOnClickListener { endTime ->
            val tpd = TimePickerDialog(requireContext(), { _, hour, min ->
                setTimeToEdittext(endTime, hour, min)
                end.set(Calendar.HOUR_OF_DAY, hour)
                end.set(Calendar.MINUTE, min)
            }, currHour, currMinute, true)

            tpd.show()
        }

        //save event (first check if all user input is valid)
        binding.eventCreationSaveBtn.setOnClickListener {
            val errors = EventCreationUtil.isFormValid(
                binding.eventCreationTitle.text.toString(),
                binding.eventCreationDescription.text.toString(),
                binding.eventCreationLocation.text.toString(),
                binding.eventCreationLocationLat.text.toString(),
                binding.eventCreationLocationLon.text.toString(),
                now,
                binding.eventCreationStartDate.text.toString(),
                binding.eventCreationStartTime.text.toString(),
                start,
                binding.eventCreationEndDate.text.toString(),
                binding.eventCreationEndTime.text.toString(),
                end
            )

            if (errors.isEmpty()) {
                binding.eventCreationStartDate.clearError()
                binding.eventCreationStartTime.clearError()
                binding.eventCreationEndDate.clearError()
                binding.eventCreationEndTime.clearError()
                passDataToSaveEvent()
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_TITLE)) {
                binding.eventCreationTitle.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_DESCRIPTION)) {
                binding.eventCreationDescription.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_LOCATION)) {
                binding.eventCreationLocation.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_LAT)) {
                binding.eventCreationLocationLat.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_LON)) {
                binding.eventCreationLocationLon.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_START_DATE)) {
                binding.eventCreationStartDate.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_START_TIME)) {
                binding.eventCreationStartTime.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_END_DATE)) {
                binding.eventCreationEndDate.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.MISSING_END_TIME)) {
                binding.eventCreationEndTime.error = getString(R.string.error_required)
            }
            if (errors.contains(EventCreationUtil.FormErrors.START_IN_PAST)) {
                binding.eventCreationStartDate.error = getString(R.string.error_start_in_past)
                binding.eventCreationStartTime.error = getString(R.string.error_start_in_past)
            }
            if (errors.contains(EventCreationUtil.FormErrors.END_IN_PAST)) {
                binding.eventCreationEndDate.error = getString(R.string.error_end_in_past)
                binding.eventCreationEndTime.error = getString(R.string.error_end_in_past)
            }
            if (errors.contains(EventCreationUtil.FormErrors.END_BEFORE_START)) {
                binding.eventCreationStartDate.error = getString(R.string.error_end_before_start)
                binding.eventCreationStartTime.error = getString(R.string.error_end_before_start)
                binding.eventCreationEndDate.error = getString(R.string.error_end_before_start)
                binding.eventCreationEndTime.error = getString(R.string.error_end_before_start)
            }
        }

        viewModel.state.observe(viewLifecycleOwner, androidx.lifecycle.Observer { event ->
            val resource = event.getContentIfNotHandled()
            resource?.let { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        //upload successful
                        showToast("Veranstaltung wurde erstellt")
                        findNavController().popBackStack(R.id.eventFragment, false)
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
            }
        })

        binding.errorView.errorRetryBtn.setOnClickListener {
            hideErrorView()
            showProgressbar()
            retry()
        }

        //override onbackpressed so the user sees that all input is lost
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showToast(getString(R.string.event_creation_onBackPressed))
            findNavController().popBackStack(R.id.eventFragment, false)
        }
    }

    private fun passDataToSaveEvent() {
        showProgressbar()
        if (isNetworkAvailable(requireContext())) {
            viewModel.createEvent(
                imageUri,
                binding.eventCreationTitle.text.toString(),
                binding.eventCreationDescription.text.toString(),
                binding.eventCreationLocation.text.toString(),
                binding.eventCreationLocationLat.text.toString(),
                binding.eventCreationLocationLon.text.toString(),
                now,
                binding.eventCreationStartDate.text.toString(),
                binding.eventCreationStartTime.text.toString(),
                start,
                binding.eventCreationEndDate.text.toString(),
                binding.eventCreationEndTime.text.toString(),
                end,
                binding.eventCreationPublic.isChecked
            )
        } else {
            hideProgressbar()
            showErrorView("No internet connection")
        }
    }

    private fun setDateToEdittext(view: View, year: Int, month: Int, day: Int) {
        val s = getString(R.string.date_formated, day, month+1, year)
        val editable = Editable.Factory.getInstance().newEditable(s)
        when (view.id) {
            R.id.event_creation_start_date -> {
                binding.eventCreationStartDate.text = editable
            }
            R.id.event_creation_end_date -> {
                binding.eventCreationEndDate.text = editable
            }
        }
    }

    private fun setTimeToEdittext(view: View, hour: Int, min: Int) {
        val h = hour.toString()
        val m = if (min < 10) {
            "0$min"
        } else {
            min.toString()
        }
        val s = getString(R.string.time_formated, h, m)
        val editable = Editable.Factory.getInstance().newEditable(s)
        when (view.id) {
            R.id.event_creation_start_time -> {
                binding.eventCreationStartTime.text = editable
            }
            R.id.event_creation_end_time -> {
                binding.eventCreationEndTime.text = editable
            }
        }
    }

    //observe permission result
    override fun setupObserver(permissionResultLiveData: LiveData<PermissionResult>) {
        permissionResultLiveData.observe(this@EventCreationFragment, {
            when (it) {
                is PermissionResult.PermissionGranted -> {
                    if (it.requestCode == REQUEST_ID) {
                        getImageFromGallery()
                    }
                }
                is PermissionResult.PermissionDenied -> {
                    if (it.requestCode == REQUEST_ID) {
                        showToast(getString(R.string.permission_denied))
                    }
                }
                is PermissionResult.PermissionDeniedPermanently -> {
                    if (it.requestCode == REQUEST_ID) {
                        showToast(getString(R.string.permission_denied_permanent))
                    }
                }
                is PermissionResult.ShowRational -> {
                    if (it.requestCode == REQUEST_ID) {
                        shouldShowRequestPermissionRationale(getString(R.string.permission_ex_storage_rationale))
                    }
                }
            }
        })
    }

    //open gallery
    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    //get image from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            //val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            imageUri = data?.data

            binding.eventCreationImage.setImageURI(imageUri)
        }
    }

    private fun retry() {
        passDataToSaveEvent()
    }

    private fun showProgressbar() {
        binding.progressbar.root.show()
    }

    private fun hideProgressbar() {
        binding.progressbar.root.hide()
    }

    private fun showErrorView(message: String) {
        binding.eventCreationSv.visibility = View.GONE
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorTextview.text = message
    }

    private fun hideErrorView() {
        binding.eventCreationSv.visibility = View.VISIBLE
        binding.errorView.root.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_ID = 1
        const val REQUEST_IMAGE = 2
    }
}