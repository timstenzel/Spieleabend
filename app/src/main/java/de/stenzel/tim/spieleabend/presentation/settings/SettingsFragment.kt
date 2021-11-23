package de.stenzel.tim.spieleabend.presentation.settings

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.helpers.showToast
import de.stenzel.tim.spieleabend.notifications.SubscriptionHelperService
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var subscriptionHelper : SubscriptionHelperService

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val nightModeEnabled = sharedPreferences.getBoolean(getString(R.string.theme_key), false)
        val notificationsEnabled = sharedPreferences.getBoolean(getString(R.string.notifications_switch_key), true)

        addPreferencesFromResource(R.xml.preference_screen)

        //define category for theme (dark mode/ light mode)
        val catTheme = PreferenceCategory(context).apply {
            title = getString(R.string.theme_cat)
        }
        preferenceScreen.addPreference(catTheme)

        //switch to toggle night mode
        val switchTheme = SwitchPreference(context).apply {
            key = getString(R.string.theme_key)
            title = getString(R.string.theme_title)
            summary = getString(R.string.theme_summary)
            isChecked = nightModeEnabled
        }
        catTheme.addPreference(switchTheme)

        //define category for notifications
        val catNotifications = PreferenceCategory(context).apply {
            title = getString(R.string.notifications_cat)
        }
        preferenceScreen.addPreference(catNotifications)

        //define switch for all notifications on/off
        val switchNotifications = SwitchPreference(context).apply {
            key = getString(R.string.notifications_switch_key)
            title = getString(R.string.notifications_switch_title)
            summary = getString(R.string.notifications_switch_summary)
            isChecked = notificationsEnabled
        }
        catNotifications.addPreference(switchNotifications)

        //define chechboxes for all topics
        val topics = resources.getStringArray(R.array.news_topics)
        for (topic in topics.withIndex()) {
            val cb = CheckBoxPreference(context).apply {
                key = getString(R.string.notifications_cb_key, topics[topic.index].lowercase())
                title = topic.value
                summary = getString(R.string.notifications_cb_summary, topic.value)
                isChecked = if (!notificationsEnabled) {
                    false
                } else {
                    val topicEnabled = sharedPreferences.getBoolean(getString(R.string.notifications_cb_key, topics[topic.index].lowercase()), true)
                    topicEnabled
                }
                isVisible = notificationsEnabled
                //add click support for notification checkboxes
                onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->

                    if (newValue as Boolean) {
                        subscriptionHelper.subscribeToTopic(this.title.toString())
                    } else {
                        subscriptionHelper.unsubscribeFromTopic(this.title.toString())
                    }

                    true
                }
            }
            catNotifications.addPreference(cb)
        }

        //add a save button (only closes the fragment, since the values of the settings are saves on click)
        val saveButton = Preference(context).apply {
            key = getString(R.string.save_button_key)
            title = getString(R.string.save_button_title)
        }
        preferenceScreen.addPreference(saveButton)

        //add click support for theme switch
        switchTheme.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (newValue as Boolean) {
                //switch theme to dark
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                requireActivity().recreate()
            } else {
                //switch theme to light
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                requireActivity().recreate()
            }
            true
        }

        //add click support for notification switch
        switchNotifications.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->

            for (cb in catNotifications.children) {
                if (cb is CheckBoxPreference) {
                    if (newValue as Boolean) {
                        cb.isVisible = true
                        cb.isChecked = true
                        subscriptionHelper.subscribeToTopic(cb.title.toString())
                    } else {
                        cb.isVisible = false
                        cb.isChecked = false
                        subscriptionHelper.unsubscribeFromTopic(cb.title.toString())
                    }
                }
            }

            true
        }

        //add click support for save button
        saveButton.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            showToast(getString(R.string.save_button_settings_saved))
            findNavController().popBackStack(R.id.profileFragment, false)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //override onbackpressed so the user sees the settings are saved
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showToast(getString(R.string.save_button_settings_saved))
            findNavController().popBackStack(R.id.profileFragment, false)
        }
    }
}