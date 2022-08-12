package com.example.client.screens.rating.community.fragment

import com.example.client.base.IBaseCollectionView
import com.example.client.models.rating.RatingModel

interface ICommunityView : IBaseCollectionView {
    fun navigateToRatingDetailScreen(rating: RatingModel)
    fun showErrorMessage(errMessage: Int)
    fun showEmptyData()
    fun showMoreData(items: List<RatingModel>)
    fun showData(items: List<RatingModel>)
}