package com.example.client.screens.wallet.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.base.BaseCollectionPresenter
import com.example.client.screens.wallet.fragment.IWalletView

class WalletPresent(mView: IWalletView) : BaseCollectionPresenter<IWalletView>(mView),IWalletPresent {

    private val preferences by lazy { Preferences.newInstance() }

    override fun getProfile() {
        mView?.showWallet(preferences.profile)
    }

    override fun getTransactions(label: Constants.Transaction, user: Int) {
//        if (label == Constants.TRANSACTION.INPUT) {
//            val service = newInstance().create(RechargeService::class.java)
//            service.getByUser(user).enqueue(object : Callback<List<TransactionModel?>?> {
//                override fun onResponse(call: Call<List<TransactionModel?>?>, response: Response<List<TransactionModel?>?>) {
//                    wView.showTransactions(response.body(), Constants.TRANSACTION.INPUT)
//                }
//
//                override fun onFailure(call: Call<List<TransactionModel?>?>, t: Throwable) {
//                    wView.showTransactions(ArrayList(), Constants.TRANSACTION.INPUT)
//                }
//            })
//        }
//        if (label == Constants.TRANSACTION.OUPUT) {
//            val service = newInstance().create(TransactionService::class.java)
//            service.getByUser(user).enqueue(object : Callback<List<TransactionModel?>?> {
//                override fun onResponse(call: Call<List<TransactionModel?>?>, response: Response<List<TransactionModel?>?>) {
//                    wView.showTransactions(response.body(), Constants.TRANSACTION.OUPUT)
//                }
//
//                override fun onFailure(call: Call<List<TransactionModel?>?>, t: Throwable) {
//                    wView.showTransactions(ArrayList(), Constants.TRANSACTION.OUPUT)
//                }
//            })
//        }
    }

}