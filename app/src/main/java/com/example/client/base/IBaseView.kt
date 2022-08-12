package com.example.client.base

import com.example.client.models.rating.RatingModel

interface IBaseView {
    fun showLoading()
    fun hideLoading()
    fun onBackPress()
}