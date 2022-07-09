package com.example.client.screens.wallet.present

import com.example.client.app.Constants
import com.example.client.base.IBaseCollectionPresenter
import com.example.client.base.IBaseCollectionView

interface IWalletPresent: IBaseCollectionPresenter {
    fun getProfile()
    fun getTransactions(label: Constants.Transaction, user: Int)
}