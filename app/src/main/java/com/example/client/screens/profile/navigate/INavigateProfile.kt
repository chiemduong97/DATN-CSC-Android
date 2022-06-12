package com.example.client.screens.profile.navigate

import android.content.Intent
import androidx.fragment.app.Fragment

interface INavigateProfile {
    fun replaceFragment(fragment: Fragment?, TAG: String)
    fun addFragment(fragment: Fragment?, TAG: String)
    fun popFragment()
}