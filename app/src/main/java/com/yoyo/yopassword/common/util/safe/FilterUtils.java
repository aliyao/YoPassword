package com.yoyo.yopassword.common.util.safe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterUtils {
    public static String FilterNumAndLetters(String str) {
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String resultStr = m.replaceAll("").trim();
        return resultStr;
    }
}
