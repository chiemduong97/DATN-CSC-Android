package com.example.client.screens.wallet.fragment

import com.example.client.app.Constants
import com.example.client.base.IBaseCollectionView
import com.example.client.models.profile.ProfileModel
import com.example.client.models.transaction.TransactionModel

interface IWalletView: IBaseCollectionView {
    fun showWallet(profile: ProfileModel)
    fun showTransactions(items: List<TransactionModel>, method: Constants.Transaction)
    fun showEmptyData()
}