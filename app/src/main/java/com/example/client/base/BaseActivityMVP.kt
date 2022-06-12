package com.example.client.base
import android.os.Bundle


abstract class BaseActivityMVP<P : IBasePresenter?> : BaseActivity() {
    private var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = presenter
    }

    override fun onDestroy() {
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    protected abstract val presenter: P
}