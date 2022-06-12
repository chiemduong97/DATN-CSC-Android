package com.example.client.screens.login.navigate

import android.content.Intent
import androidx.fragment.app.Fragment

interface INavigateLogin {
    fun replaceFragment(fragment: Fragment?, TAG: String)
    fun addFragment(fragment: Fragment?, TAG: String)
    fun popFragment()
}