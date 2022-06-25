package com.example.client.base
import android.os.Bundle
import android.view.View


abstract class BaseActivityMVP<P : IBasePresenter?> : BaseActivity() {
    private var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = presenter
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mPresenter?.onViewCreated()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        mPresenter?.onViewCreated()
    }

    override fun onDestroy() {
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    protected abstract val presenter: P
}