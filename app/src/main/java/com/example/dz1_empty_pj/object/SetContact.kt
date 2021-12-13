package com.example.dz1_empty_pj.`object`

import com.example.dz1_empty_pj.R
import com.example.dz1_empty_pj.data.Contact
import java.util.*

object SetContact {
    val setContact = listOf(
        Contact(
            contactId = "1",
            name = "Халлон Морнэмир",
            firstMobile = "+7(900)878-12-18",
            secondMobile = "+7(985)645-98-47",
            firstEmail = "gahkc@gm.com",
            secondEmail = "htfk@ya.es",
            description = "Много пьет, много знает",
            avatarUri = R.drawable.ic_git_cat,
            birthDay = GregorianCalendar(2021, Calendar.DECEMBER, 13)
        )
    )
}