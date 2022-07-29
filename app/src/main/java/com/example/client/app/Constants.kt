package com.example.client.app

import com.google.gson.annotations.SerializedName

object Constants {
    object BundleKey {
        const val CATEGORY_MODEL = "CATEGORY_MODEL"
        const val PRODUCT_MODEL = "PRODUCT_MODEL"
        const val ORDER_CODE = "ORDER_CODE"
        const val EMAIL = "EMAIL"
        const val PROMOTION_MODEL = "PROMOTION_MODEL"

    }

    const val SHOW_PRODUCT_DETAIL = "SHOW_PRODUCT_DETAIL"
    const val SHOW_PROMOTION_DETAIL = "SHOW_PROMOTION_DETAIL"

    object MORE {
        const val CATEGORY = "CATEGORY"
        const val HIGHLIGHT = "HIGHLIGHT"
        const val NEW = "NEW"
    }

    enum class PaymentMethod {
        WALLET, MOMO, COD
    }

    object EventKey {
        const val CHANGE_BRANCH = "CHANGE_BRANCH"
        const val UPDATE_PROFILE_INFO = "UPDATE_PROFILE_INFO"
        const val UPDATE_PROFILE_AVATAR = "UPDATE_PROFILE_AVATAR"
        const val UPDATE_CART = "UPDATE_CART"
        const val DELETE_CART = "DELETE_CART"
        const val UPDATE_ADD_TO_CART_PRODUCT = "UPDATE_ADD_TO_CART_PRODUCT"
        const val UPDATE_STATUS_ORDER = "UPDATE_STATUS_ORDER"
        const val UPDATE_ORDER = "UPDATE_ORDER"
        const val UPDATE_LOCATION = "UPDATE_LOCATION"
        const val UPDATE_LOCATION_WHEN_RUN_APP = "UPDATE_LOCATION_WHEN_RUN_APP"
        const val LOGIN_SUCCESS = "LOGIN_SUCCESS"
        const val RESET_SUCCESS = "RESET_SUCCESS"
        const val CHANGE_PAYMENT_METHOD = "CHANGE_PAYMENT_METHOD"
        const val UPDATE_PROMOTION = "UPDATE_PROMOTION"
        const val RECHARGE_SUCCESS = "RECHARGE_SUCCESS"
    }

    enum class Transaction {
        RECHARGE, TRANSACTION
    }

    object ErrorCode {
        const val ERROR_1001 = 1001
        const val ERROR_1002 = 1002
        const val ERROR_1003 = 1003
        const val ERROR_1004 = 1004
        const val ERROR_1005 = 1005
        const val ERROR_1006 = 1006
        const val ERROR_1007 = 1007
        const val ERROR_1008 = 1008
        const val ERROR_1009 = 1009
        const val ERROR_1010 = 1010
        const val ERROR_1011 = 1011
        const val ERROR_1012 = 1012
        const val ERROR_1013 = 1013
        const val ERROR_1014 = 1014
        const val ERROR_1015 = 1015
    }

    enum class RequestType {
        REGISTER, RESET_PASSWORD
    }

    enum class TransactionType {
        @SerializedName("recharge")
        RECHARGE,
        @SerializedName("paid")
        PAID,
        @SerializedName("refund")
        REFUND,
    }

    object MoMoConstants {
        const val MERCHANT_NAME = "CSC"
        const val MERCHANT_CODE: String = "MOMOSCN920220707"
        const val MERCHANT_NAME_LABEL = "Nhà cung cấp"
        const val DESCRIPTION = "Thanh toán dịch vụ CSC"
    }

}