package de.stenzel.tim.spieleabend.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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

        setHasOptionsMenu(true)

        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer { loggedIn ->

            isLoggedIn = loggedIn

            if (loggedIn) {

                showProfileViews()

                binding.profileLoginLogoutBtn.text = "Logout"
            } else {

                hideProfileViews()

                binding.profileLoginLogoutBtn.text = "Login"
            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user.photoUrl != null) {
                Glide.with(requireContext()).load(user.photoUrl).into(binding.profileImage)
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_baseline_person_24).into(binding.profileImage)
            }

            binding.profileDisplayName.text = if (user.displayName != null) {
                user.displayName
            } else {
                "nice display name placeholder"
            }
            binding.profileEmail.text = user.email
            binding.profileEmailVerified.text = if (user.isEmailVerified) {
                "email verified"
            } else {
                "email not verified"
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

    private fun hideProfileViews() {
        binding.profileImage.visibility = View.GONE
        binding.profileDisplayName.visibility = View.GONE
        binding.profileEmail.visibility = View.GONE
        binding.profileEmailVerified.visibility = View.GONE
    }

    private fun showProfileViews() {
        binding.profileImage.visibility = View.VISIBLE
        binding.profileDisplayName.visibility = View.VISIBLE
        binding.profileEmail.visibility = View.VISIBLE
        binding.profileEmailVerified.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewModel.statusCheck()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
                true
            }
            else -> {
                Log.e("Profile Fragment", "error in options menu")
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}