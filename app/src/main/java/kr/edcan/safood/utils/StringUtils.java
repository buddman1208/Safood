package kr.edcan.safood.utils;

import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Junseok on 2016-10-04.
 */

public class StringUtils {
    public static boolean validEmail(EditText editText) {
        return editText.getText().toString().trim().contains("@");
    }

    public static boolean checkPassword(EditText password, EditText repassword) {
        return password.getText().toString().trim().equals(repassword.getText().toString().trim());
    }

    public static boolean fullFilled(EditText... e) {
        for (EditText editText : e) {
            if (editText.getText().toString().trim().equals("")) return false;
        }
        return true;
    }

    public static String getFullImageUrl(String thumbnailUrl) {
        return "http://iwin247.kr:4000" + thumbnailUrl;
    }

    public static String convertArraytoString(ArrayList<String> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result += (arrayList.get(i) + ((i == arrayList.size() - 1) ? "" : ","));
        }
        return result;
    }

    public static String convertExceptionArray(ArrayList<Boolean> allergicException, ArrayList<Boolean> religiousException) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < allergicException.size(); i++) {
            builder.append(allergicException.get(i));
            if (i != allergicException.size() - 1) builder.append(",");
        }
        builder.append("+");
        for (int i = 0; i < religiousException.size(); i++) {
            builder.append(religiousException.get(i));
            if (i != religiousException.size() - 1) builder.append(",");
        }
        return builder.toString();
    }

}