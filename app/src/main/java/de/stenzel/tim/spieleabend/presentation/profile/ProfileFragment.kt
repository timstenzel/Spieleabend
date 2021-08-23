package de.stenzel.tim.spieleabend.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.databinding.ProfileFragmentBinding

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var isLoggedIn = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { loggedIn ->

            isLoggedIn = loggedIn

            if (loggedIn) {
                binding.profileLoginLogoutBtn.text = "Logout"
            } else {
                binding.profileLoginLogoutBtn.text = "Login"
            }
        })

        binding.profileLoginLogoutBtn.setOnClickListener {
            if (isLoggedIn) {
                logoutUser()
            } else {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }

    private fun logoutUser() {
        viewModel.logoutUser()
    }

    override fun onResume() {
        super.onResume()
        viewModel.statusCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}