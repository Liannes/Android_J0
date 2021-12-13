package com.example.dz1_empty_pj.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.dz1_empty_pj.BirthdayReceiver
import com.example.dz1_empty_pj.`object`.Constants.CONTACTS_ID
import com.example.dz1_empty_pj.`object`.Constants.CONTACT_NAME
import com.example.dz1_empty_pj.`object`.Constants.TAG_DETAILS
import com.example.dz1_empty_pj.service.ContactInterface
import com.example.dz1_empty_pj.R
import com.example.dz1_empty_pj.data.Contact
import com.example.dz1_empty_pj.databinding.ContactDetailsFragmentBinding
import com.example.dz1_empty_pj.service.GetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ContactDetailsFragment : Fragment(R.layout.contact_details_fragment) {
    private var _binding: ContactDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactInterface: ContactInterface? = null
    private var getContactService: GetService? = null

    private var data: Contact? = null
    private var name: String? = null
    private var id: String? = null

    inner class CurrectTime {
        fun calendarBirthday(): Long {
            val notificationCalendar = GregorianCalendar.getInstance()
            val leap = data?.birthDay?.isLeapYear(Calendar.YEAR)

            data?.birthDay?.get(Calendar.DATE)?.let { notificationCalendar.set(Calendar.DATE, it) }
            data?.birthDay?.get(Calendar.MONTH)?.let { notificationCalendar.set(Calendar.MONTH, it) }
            data?.birthDay?.get(Calendar.YEAR)?.let { notificationCalendar.set(Calendar.YEAR, it) }

            notificationCalendar.set(Calendar.HOUR_OF_DAY, 0)
            notificationCalendar.set(Calendar.MINUTE, 59)
            notificationCalendar.set(Calendar.SECOND, 30)

            if(leap == true) notificationCalendar.add(Calendar.YEAR,4)

            Log.d(
                TAG_DETAILS, "setCalendar = ${notificationCalendar.get(Calendar.DATE)} " +
                        "${notificationCalendar.get(Calendar.MONTH)} " +
                        "${notificationCalendar.get(Calendar.YEAR)}/" + " leap = $leap"
            )

            return notificationCalendar.timeInMillis
        }
    }

    companion object {
        private const val CONTACT_ID = "contact_Id"
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to contactId)
        }
    }

    private fun sentNotification() {
        Log.d(TAG_DETAILS, "ALARM ON")
        val intent = Intent(context, BirthdayReceiver::class.java)
        intent.putExtra(CONTACTS_ID, id)
        intent.putExtra(CONTACT_NAME, name)
        Log.d(TAG_DETAILS, "CONTACTS_ID = $CONTACT_ID / ID = $CONTACT_ID")
        val alarmIntent =
            PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val reverseContactBirthday: Long = CurrectTime().calendarBirthday()

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, reverseContactBirthday, alarmIntent)
    }

    private fun hideNotification() {
        Log.d(TAG_DETAILS, "ALARM OFF")
        val intent = Intent(context, BirthdayReceiver::class.java)
        val alarmIntent =
            PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }

    private fun getViewBinding() {
        requireView().post {
            with(binding) {
                id = data?.contactId
                name = data?.name

                detailsName.text = name
                data?.avatarUri?.let { detailsImage.setImageResource(it) }
                detailsNum.text = data?.firstMobile
                detailsEmail.text = data?.firstEmail
                detailsNumTwo.text = data?.secondMobile
                detailsEmailTwo.text = data?.secondEmail
                detailsNote.text = data?.description

                detailsBirthday.text =
                    data?.birthDay?.let { currectData(it) }

                toolbar.title = getString(R.string.contactDetails)

                switchButton.setOnClickListener {
                    if (binding.switchButton.isChecked) {
                        sentNotification()
                    } else {
                        hideNotification()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactInterface && context is GetService) {
            contactInterface = context
            getContactService = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            data = withContext(Dispatchers.IO) {
                while (!(getContactService?.getStatusBound() as Boolean)) {
                }
                getContactService?.getService()
                    ?.getContactDetails(arguments?.getString(CONTACT_ID).toString())
            }
            getViewBinding()
        }
        _binding = ContactDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        contactInterface = null
        getContactService = null
        super.onDetach()
    }
}