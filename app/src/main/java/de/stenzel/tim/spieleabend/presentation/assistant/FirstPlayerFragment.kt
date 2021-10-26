package de.stenzel.tim.spieleabend.presentation.assistant

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.FirstPlayerFragmentBinding
import de.stenzel.tim.spieleabend.glide.GlideApp
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.helpers.showToast

class FirstPlayerFragment : Fragment() {

    private var _binding: FirstPlayerFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirstPlayerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FirstPlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.amount.observe(viewLifecycleOwner, Observer { event ->
            val resource = event.getContentIfNotHandled()
            resource?.let { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        hideProgressbar()
                        response.data?.let { amount ->
                            val action = FirstPlayerFragmentDirections.actionFirstPlayerFragmentToLuckyWheelFragment(amount)
                            findNavController().navigate(action)
                        }
                    }
                    Status.ERROR -> {
                        hideProgressbar()
                        response.message?.let { message ->
                            showToast(message)
                        }
                    }
                    Status.LOADING -> {
                        showProgressbar()
                    }
                }
            }
        })

        binding.firstPlayerConfirmBtn.setOnClickListener {
            viewModel.validateInput(binding.firstPlayerAmountEt.text.toString())
        }

    }

    private fun showProgressbar() {
        binding.progressbar.root.show()
    }

    private fun hideProgressbar() {
        binding.progressbar.root.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}