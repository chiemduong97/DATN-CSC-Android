package com.example.client.app

object Constants {
    var CATEGORY_MODEL = "CATEGORY_MODEL"
    var PRODUCT_MODEL = "PRODUCT_MODEL"
    var PRODUCT_BY = "PRODUCT_BY"
    var CART_MODEL = "CART_MODEL"
    var ORDER_CODE = "ORDER_CODE"
    var EMAIL = "EMAIL"
    var SHOW_PRODUCT_DETAIL = "SHOW_PRODUCT_DETAIL"
    const val SECTION_1 = 1000000.0
    const val SECTION_2 = 2000000.0
    const val SECTION_5 = 5000000.0

    object MORE {
        const val CATEGORY = "CATEGORY"
        const val HIGHLIGHT = "HIGHLIGHT"
        const val NEW = "NEW"
    }

    object PAYMENT {
        const val WALLET = "WALLET"
        const val COD = "COD"
    }

    object EventKey {
        const val CHANGE_BRANCH = "CHANGE_BRANCH"
        const val UPDATE_PROFILE_INFO = "UPDATE_PROFILE_INFO"
        const val UPDATE_PROFILE_AVATAR = "UPDATE_PROFILE_AVATAR"
        const val UPDATE_CART = "UPDATE_CART"
        const val UPDATE_ADD_TO_CART_PRODUCT = "UPDATE_ADD_TO_CART_PRODUCT"
        const val UPDATE_STATUS_ORDER = "UPDATE_STATUS_ORDER"
        const val UPDATE_ORDER = "UPDATE_ORDER"
        const val UPDATE_LOCATION = "UPDATE_LOCATION"
        const val LOGIN_SUCCESS = "LOGIN_SUCCESS"
        const val RESET_SUCCESS = "RESET_SUCCESS"
    }

    object TRANSACTION {
        const val INPUT = "INPUT"
        const val OUPUT = "OUPUT"
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

    object Method {
        const val CATEGORY = "CATEGORY"
        const val HIGHLIGHT = "HIGHLIGHT"
        const val NEW = "NEW"
    }
}