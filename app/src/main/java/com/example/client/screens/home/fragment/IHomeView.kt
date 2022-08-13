package com.example.client.screens.home.fragment

import com.example.client.base.IBaseView
import com.example.client.models.category.CategoryModel
import com.example.client.models.branch.BranchModel
import com.example.client.models.order.OrderLocation
import com.example.client.models.category.HomeSectionModel
import com.example.client.models.profile.ProfileModel

interface IHomeView : IBaseView {
    fun showCategories(items: List<CategoryModel>)
    fun hideCategories()
    fun showBranch(branch: BranchModel)
    fun showProfile(profile: ProfileModel)
    fun showOrderLocation(orderLocation: OrderLocation)
    fun toBranchScreen()
    fun showErrorMessage(errMessage: Int)
    fun showHomeSections(items: List<HomeSectionModel>)
}