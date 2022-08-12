package com.example.client.screens.rating.navigate

import androidx.fragment.app.Fragment

interface INavigateRating {
    fun replaceFragment(fragment: Fragment?, TAG: String)
    fun addFragment(fragment: Fragment?, TAG: String)
    fun popFragment()
}