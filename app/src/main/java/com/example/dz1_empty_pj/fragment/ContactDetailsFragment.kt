package com.example.dz1_empty_pj.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.dz1_empty_pj.ContactInterface
import com.example.dz1_empty_pj.R
import com.example.dz1_empty_pj.data.Contact
import com.example.dz1_empty_pj.databinding.ContactDetailsFragmentBinding
import com.example.dz1_empty_pj.service.GetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ContactDetailsFragment : Fragment(R.layout.contact_details_fragment),
    CompoundButton.OnCheckedChangeListener {
    private var _binding: ContactDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactInterface: ContactInterface? = null
    private var getContactService: GetService? = null
    private var data: Contact? = null
    private var alarmManager: AlarmManager? = null


    companion object {
        private const val MEMO = "ID"
        private const val CONTACT_ID_NOTIFICATION = "contactId"
        private const val ADDRESS = "com.example.dz1_empty_pj.fragment"

        private const val CONTACT_ID = "contact_id"
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to contactId)
        }
    }

    private fun getViewBinding() {
        requireView().post {
            with(binding) {
                detailsBirthDay.text = data?.birthDay.toString()
                detailsName.text = data?.name
                data?.avatarUri?.let { detailsImage.setImageResource(it) }
                detailsNum.text = data?.firstMobile
                detailsEmail.text = data?.firstEmail
                detailsNumTwo.text = data?.secondMobile
                detailsEmailTwo.text = data?.secondEmail
                detailsNote.text = data?.description
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    detailsBirthDay.text = SimpleDateFormat("dd-MM-yyyy").format(data?.birthDay?.time).toString()
                }
                toolbar.title = getString(R.string.contactDetails)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val contactId = "contactId"

        val intent = Intent(ADDRESS)
        intent.putExtra(CONTACT_ID_NOTIFICATION, contactId)
        intent.putExtra(MEMO, data?.name + activity?.getString(R.string.text_notification))

        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)

        if (isChecked) {
            Log.d("CONTACT_DETAILS", "Alarm on")

            val calendar = GregorianCalendar.getInstance(TimeZone.getDefault(), Locale.getDefault())

            data?.birthDay?.get(GregorianCalendar.YEAR)
                ?.let { calendar.set(GregorianCalendar.YEAR, it) }
            data?.birthDay?.get(GregorianCalendar.MONTH)
                ?.let { calendar.set(GregorianCalendar.MONTH, it) }
            data?.birthDay?.get(GregorianCalendar.DATE)
                ?.let { calendar.set(GregorianCalendar.DATE, it) }

            calendar.set(GregorianCalendar.DAY_OF_WEEK, 0)
            calendar.set(GregorianCalendar.MINUTE, 0)
            calendar.set(GregorianCalendar.SECOND, 0)

            if (System.currentTimeMillis() > calendar.timeInMillis) calendar.add(
                GregorianCalendar.YEAR,
                1
            )

            alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
        } else {
            Log.d("CONTACT_DETAILS", "Alarm off")
            alarmManager?.cancel(alarmIntent)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactInterface && context is GetService) {
            contactInterface = context
            getContactService = context
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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