package com.cbfacademy.apiassessment.utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
//@AllArgsConstructor
public class ResponseUtils {
    public static final String USER_EXISTS_CODE = "001";
    public static final String USER_EXISTS_MESSAGE = "User with provided email already exists!";

    public static final int lengthOfAccountNumber = 10;
    public static final String SUCCESS_MESSAGE = "Successfully done";
    public static final String SUCCESS = "002";
    public static final String USER_NOT_FOUND_MESSAGE = "This user does not exists";
    public static final String USER_NOT_FOUND_CODE ="003";
    public static final String USER_REGISTERED_SUCCESS = "User has been successfully registered!";

    public static String generateAccountNumber(int len){
        String accountNumber = "";
        int x;
        char[] stringChars = new char[len];
        for (int i = 0; i<len;i++)//4
        {
            Random random = new Random();
            x = random.nextInt(9);

            stringChars[i] = Integer.toString(x).toCharArray()[0];
        }

        accountNumber = new String((stringChars));
        return accountNumber.trim();
    }
}
