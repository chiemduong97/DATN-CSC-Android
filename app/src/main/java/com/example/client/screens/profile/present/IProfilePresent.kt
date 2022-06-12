package com.example.client.screens.profile.present

import com.example.client.base.IBasePresenter

interface IProfilePresent: IBasePresenter {
    fun bindData()
    fun onLogout()
    fun navigateToContact()
}