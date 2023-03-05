package com.example.util;

import java.util.regex.Pattern;

public final class PatternUtil {
    private static final Pattern ALPHA_NUMERIC = Pattern.compile("^[a-zA-Z0-9]+$");
    
    private PatternUtil() {
        throw new AssertionError();
    }
    
    public static boolean isAlphaNumeric(String s) {

        if(s == null || s.isEmpty()){
            return false;
        }

        return ALPHA_NUMERIC.matcher(s).matches();
    }
}
