package com.zzz.platform.utils;

import java.util.regex.Pattern;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/20
 */
public class VerificationUtil {

    /**
     * pwd regexp
     */
    public static final String PWD_REGEXP = "^[A-Za-z0-9.]{6,15}$";

    /**
     * email
     */
//    public static final String EMAIL = "^(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$\n";
//"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    //修改正则表达式
    public static final String EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String JSON_FILE_PATTERN = "^.*\\.json$";

    private static final String PDF_FILE_PATTERN = "^.*\\.pdf$";

    public static final Pattern JSON_PATTERN = Pattern.compile(JSON_FILE_PATTERN, Pattern.CASE_INSENSITIVE);
    public static final Pattern PDF_PATTERN = Pattern.compile(PDF_FILE_PATTERN, Pattern.CASE_INSENSITIVE);

    /**
     * validated by regex pattern
     * @param str str
     * @param pattern
     * @return
     */
    public static boolean match(String str, Pattern pattern) {
        if (str == null) {
            return false;
        }
        return pattern.matcher(str).matches();
    }
}
