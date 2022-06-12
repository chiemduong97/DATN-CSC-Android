package com.example.client.screens.product.navigate

import androidx.fragment.app.Fragment

interface INavigateProduct {
    fun replaceFragment(fragment: Fragment?, TAG: String)
    fun addFragment(fragment: Fragment?, TAG: String)
    fun popFragment()
}