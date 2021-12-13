package com.example.dz1_empty_pj.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.dz1_empty_pj.service.ContactInterface
import com.example.dz1_empty_pj.R
import com.example.dz1_empty_pj.data.Contact
import com.example.dz1_empty_pj.databinding.ContactListFragmentBinding
import com.example.dz1_empty_pj.service.GetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactListFragment : Fragment(R.layout.contact_list_fragment) {
    private var _binding: ContactListFragmentBinding? = null
    private val binding get() = _binding!!

    private var contactInterface: ContactInterface? = null
    private var getContactService: GetService? = null
    private var data: List<Contact>? = null

    private fun getViewBinding() {
        requireView().post {
            with(binding) {
                listName.text = data?.get(0)?.name
                listMobile.text = data?.get(0)?.firstMobile
                data?.get(0)?.avatarUri?.let { listImage.setImageResource(it) }
                toolbar.title = getString(R.string.contactList)
                cardView.setOnClickListener {
                    contactInterface?.navigationToContactDetailsFragment(data?.get(0)?.contactId.toString())
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            data = withContext(Dispatchers.IO) {
                while (!(getContactService?.getStatusBound() as Boolean)){}
                getContactService?.getService()?.getContactList()
            }
            getViewBinding()
        }
        _binding = ContactListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        contactInterface = null
        getContactService = null
        super.onDetach()
    }
}
