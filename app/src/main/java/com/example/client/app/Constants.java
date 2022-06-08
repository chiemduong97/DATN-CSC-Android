package com.example.client.app;

import org.jetbrains.annotations.NotNull;

public class Constants {
    public static String CATEGORY_MODEL = "CATEGORY_MODEL";
    public static String PRODUCT_MODEL = "PRODUCT_MODEL";
    public static String PRODUCT_BY = "PRODUCT_BY";
    public static String CART_MODEL = "CART_MODEL";
    public static String ORDERCODE = "ORDERCODE";
    public static String EMAIL = "EMAIL";


    public static class MORE{
        public static final String CATEGORY = "CATEGORY";
        public static final String HIGHLIGHT = "HIGHLIGHT";
        public static final String NEW = "NEW";
    }

    public static class PAYMENT{
        public static final String WALLET = "WALLET";
        public static final String CASH = "CASH";
    }

    public static class EventKey{
        public static final String CHANGE_BRANCH = "CHANGE_BRANCH";
        public static final String UPDATE_PROFILE_INFO = "UPDATE_PROFILE_INFO";
        public static final String UPDATE_PROFILE_AVATAR = "UPDATE_PROFILE_AVATAR";
        public static final String UPDATE_CART = "UPDATE_CART";
        public static final String UPDATE_STATUS_ORDER = "UPDATE_STATUS_ORDER";
        public static final String UPDATE_LOCATION = "UPDATE_LOCATION";
        public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";


    }

    public static class TRANSACTION{
        public static final String INPUT = "INPUT";
        public static final String OUPUT = "OUPUT";
    }

    public static class ErrorCode{
        public static final int ERROR_1001 = 1001;
        public static final int ERROR_1002 = 1002;
        public static final int ERROR_1003 = 1003;
        public static final int ERROR_1004 = 1004;
        public static final int ERROR_1005 = 1005;
        public static final int ERROR_1006 = 1006;
        public static final int ERROR_1007 = 1007;
        public static final int ERROR_1008 = 1008;
        public static final int ERROR_1009 = 1009;
        public static final int ERROR_1010 = 1010;
        public static final int ERROR_1011 = 1011;
        public static final int ERROR_1012 = 1012;
        public static final int ERROR_1013 = 1013;
        public static final int ERROR_1014 = 1014;
    }

    public static final Double SECTION_1 = 1000000.0;
    public static final Double SECTION_2 = 2000000.0;
    public static final Double SECTION_5 = 5000000.0;

    public enum RequestType{
        REGISTER,
        RESET_PASSWORD
    }

    public static class Method {
        public static final String CATEGORY = "CATEGORY";
        public static final String HIGHLIGHT = "HIGHLIGHT";
        public static final String NEW = "NEW";
    }
}
