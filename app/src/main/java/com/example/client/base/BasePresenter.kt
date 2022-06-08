package com.example.client.base

import com.example.client.R
import com.example.client.app.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

open class BasePresenter<V : IBaseView>(view: V) : IBasePresenter {
    var mView: V? = null
    private val mCompositeDisposable = CompositeDisposable()
    init {
        mView = view
    }

    override fun onStart() {}

    override fun onStop() {}

    override fun onDestroy() {
        mView = null
        mCompositeDisposable.clear()
    }

    protected fun add(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    protected fun <T> subscribe(observable: Observable<T>, response: Consumer<T>, throwable: Consumer<Throwable>) {
        val disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response, throwable)
        mCompositeDisposable.add(disposable)
    }

    override fun onReadyUI() {}

    override fun onViewCreated() {}

    protected fun getErrorMessage(code: Int): Int {
        var errMessage = -1
        when (code) {
            Constants.ErrorCode.ERROR_1001 -> errMessage = R.string.err_code_1001
            Constants.ErrorCode.ERROR_1002 -> errMessage = R.string.err_code_1002
            Constants.ErrorCode.ERROR_1003 -> errMessage = R.string.err_code_1003
            Constants.ErrorCode.ERROR_1004 -> errMessage = R.string.err_code_1004
            Constants.ErrorCode.ERROR_1005 -> errMessage = R.string.err_code_1005
            Constants.ErrorCode.ERROR_1006 -> errMessage = R.string.err_code_1006
            Constants.ErrorCode.ERROR_1007 -> errMessage = R.string.err_code_1007
            Constants.ErrorCode.ERROR_1008 -> errMessage = R.string.err_code_1008
            Constants.ErrorCode.ERROR_1009 -> errMessage = R.string.err_code_1009
            Constants.ErrorCode.ERROR_1010 -> errMessage = R.string.err_code_1010
            Constants.ErrorCode.ERROR_1011 -> errMessage = R.string.err_code_1011
            Constants.ErrorCode.ERROR_1012 -> errMessage = R.string.err_code_1012
            Constants.ErrorCode.ERROR_1013 -> errMessage = R.string.err_code_1013
            Constants.ErrorCode.ERROR_1014 -> errMessage = R.string.err_code_1014

        }
        return errMessage
    }

}