package com.example.dz1_empty_pj.fragment

import android.annotation.SuppressLint
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
import com.example.dz1_empty_pj.CONTACT_NAME
import com.example.dz1_empty_pj.service.ContactInterface
import com.example.dz1_empty_pj.R
import com.example.dz1_empty_pj.data.Contact
import com.example.dz1_empty_pj.databinding.ContactDetailsFragmentBinding
import com.example.dz1_empty_pj.service.GetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Array.getInt
import java.util.*

class ContactDetailsFragment : Fragment(R.layout.contact_details_fragment) {
    private var _binding: ContactDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactInterface: ContactInterface? = null
    private var getContactService: GetService? = null
    private var data: Contact? = null

    private var contactName: String? = null
    private var contactBirthday: Calendar? = null
    private var id: Int? = null

    companion object {
        private const val TAG = "CONTACT_DETAILS"
        private const val CONTACT_ID = "contact_Id"
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to contactId)
        }
    }

    private fun sentNotification() {
        Log.d(TAG, "ALARM ON")
        val intent = Intent(context, BirthdayReceiver::class.java)
        intent.putExtra(CONTACT_ID, 0)
        intent.putExtra(CONTACT_NAME, contactName)
        val alarmIntent = id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val reverseContactBirthday: Long = contactBirthday?.timeInMillis ?: 0
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, reverseContactBirthday, alarmIntent)
    }

    private fun hideNotification() {
        Log.d(TAG, "ALARM OFF")
        val intent = Intent(context, BirthdayReceiver::class.java)
        val alarmIntent =
            id?.let {
                PendingIntent.getBroadcast(
                    context,
                    it,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
    }

    @SuppressLint("SetTextI18n")
    private fun getViewBinding() {
        requireView().post {
            with(binding) {
                contactName = data?.name
                contactBirthday = data?.birthDay

                detailsName.text = data?.name
                data?.avatarUri?.let { detailsImage.setImageResource(it) }
                detailsNum.text = data?.firstMobile
                detailsEmail.text = data?.firstEmail
                detailsNumTwo.text = data?.secondMobile
                detailsEmailTwo.text = data?.secondEmail
                detailsNote.text = data?.description

                detailsBirthday.text =
                    data?.birthDay?.get(GregorianCalendar.DAY_OF_MONTH).toString() + "/" +
                            data?.birthDay?.get(GregorianCalendar.MONTH).toString()

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
            id = data?.contactId?.toInt()
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