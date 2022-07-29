package com.example.client.screens.wallet.present

import com.example.client.app.Constants
import com.example.client.base.IBaseCollectionPresenter

interface IWalletPresent: IBaseCollectionPresenter {
    fun getProfile()
    fun bindData(type: Constants.Transaction)
}