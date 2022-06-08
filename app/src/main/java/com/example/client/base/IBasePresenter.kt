package com.example.client.base

interface IBasePresenter {
    fun onStart()

    fun onStop()

    fun onDestroy()

    fun onReadyUI()

    fun onViewCreated()

}