package com.example.client.base

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.event.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        compositeDisposable.clear()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        bindEventBus(event)
    }

    protected fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    protected fun showDialogErrorMessage(message: String) {
        PrimaryDialog({}, {}).setDescription(message)
                .hideBtnCancel(false)
                .show(supportFragmentManager)
    }

    protected open fun initData() {}
    protected open fun bindData() {}
    protected open fun bindComponent() {}
    protected open fun bindEvent() {}
    protected open fun bindEventBus(event: Event) {}
}