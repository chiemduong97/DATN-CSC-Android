package com.example.client.screens.wallet.fragment

import com.example.client.base.IBaseCollectionView
import com.example.client.models.profile.ProfileModel
import com.example.client.models.transaction.TransactionModel

interface IWalletView: IBaseCollectionView {
    fun showWallet(profile: ProfileModel)
    fun showData(items: List<TransactionModel>)
    fun showMoreData(items: List<TransactionModel>)
    fun showEmptyData()
    fun showErrorMessage(errorMessage: Int)
}