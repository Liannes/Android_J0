package com.example.dz1_empty_pj.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class ContactDetailsFragment : Fragment(R.layout.contact_details_fragment) {
    private var _binding: ContactDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactInterface: ContactInterface? = null
    private var getContactService: GetService? = null
    private var data: Contact? = null

    companion object {
        private const val CONTACT_ID = "contact_id"
        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to contactId)
        }
    }

    private fun getViewBinding() {
        requireView().post {
            with(binding) {
                detailsName.text = data?.name
                data?.avatarUri?.let { detailsImage.setImageResource(it) }
                detailsNum.text = data?.firstMobile
                detailsEmail.text = data?.firstEmail
                detailsNumTwo.text = data?.secondMobile
                detailsEmailTwo.text = data?.secondEmail
                detailsNote.text = data?.description
                toolbar.title = getString(R.string.contactDetails)
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
                while (!(getContactService?.getStatusBound() as Boolean)){}
                getContactService?.getService()?.getContactDetails(arguments?.getString(CONTACT_ID).toString())
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