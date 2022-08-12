package com.example.client.screens.rating.community.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.models.rating.RatingModel

interface ICommunityPresent : IBaseCollectionPresenter {
    fun bindData()
    fun onClickItem(rating: RatingModel)
}