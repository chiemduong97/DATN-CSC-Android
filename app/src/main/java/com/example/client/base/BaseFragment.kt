package com.example.client.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.client.dialog.PrimaryDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment: Fragment(){
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        bindComponent()
        bindEvent()
        bindData()
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
    protected fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    protected fun showDialogErrorMessage(message: String) {
        PrimaryDialog({}, {}).setDescription(message)
                .showBtnCancel(false)
                .show(childFragmentManager)
    }
    protected fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    protected open fun initData() {}
    protected open fun bindData() {}
    protected open fun bindComponent() {}
    protected open fun bindEvent() {}
}