package com.koreanair.common.util;

public class StringUtil {

    public static String NVL(String str)
    {
        if(str == null || str.trim().equals("") || str.trim().equals("null"))
            return "";
        else
            return str;
    }
}
