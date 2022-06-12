package com.example.client.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.event.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseFragment: Fragment(){
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        bindComponent()
        bindEvent()
        bindData()
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
    protected fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    protected fun showDialogErrorMessage(errorCode: Int) {
        PrimaryDialog({}, {})
                .setDescription(getString(errorCode))
                .hideBtnCancel()
                .show(childFragmentManager)
    }
    protected fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    protected open fun initData() {}
    protected open fun bindData() {}
    protected open fun bindComponent() {}
    protected open fun bindEvent() {}
    protected open fun bindEventBus(event: Event) {}
}