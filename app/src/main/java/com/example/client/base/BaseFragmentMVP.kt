package com.example.client.base

import android.os.Bundle
import android.view.View

abstract class BaseFragmentMVP<P : IBasePresenter?>: BaseFragment(), IBaseView {
    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = presenter
    }
    override fun onStart() {
        super.onStart()
        mPresenter?.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.run {
            onReadyUI()
            onViewCreated()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }

    protected abstract val presenter: P

}