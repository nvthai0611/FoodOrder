package com.example.foodorder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUtils {
    private AuthUtils(){}

    public static boolean isEmailValid(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
