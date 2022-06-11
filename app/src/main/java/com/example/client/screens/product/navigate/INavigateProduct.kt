package com.example.client.screens.product.navigate

import android.content.Intent
import androidx.fragment.app.Fragment

interface INavigateProduct {
    fun replaceFragment(fragment: Fragment?, TAG: String)
    fun addFragment(fragment: Fragment?, TAG: String)
    fun popFragment()
    fun finish()
}