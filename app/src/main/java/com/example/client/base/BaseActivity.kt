package com.example.client.base

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.client.dialog.PrimaryDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseActivity: AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initData()
        bindComponent()
        bindEvent()
        bindData()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initData()
        bindComponent()
        bindEvent()
        bindData()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    protected fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    protected fun showDialogErrorMessage(message: String) {
        Log.d("error", "getErrorMessage: $message ")
        PrimaryDialog({}, {}).setDescription(message)
                .showBtnCancel(false)
                .show(supportFragmentManager)
    }

    protected open fun initData() {}
    protected open fun bindData() {}
    protected open fun bindComponent() {}
    protected open fun bindEvent() {}
}