package ua.goit.http.server.utils;


import ua.goit.http.server.entity.ApiResponse;

public class Utils {
    public static boolean isLong(String str) {
        char[] toCheck = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : toCheck) {
            if (Character.isDigit(c)) {
                sb.append(c);
                if (Long.parseLong(String.valueOf(sb)) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInt(String str) {
        char[] toCheck = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : toCheck) {
            if (Character.isDigit(c)) {
                sb.append(c);
                if (Integer.parseInt(String.valueOf(sb)) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printApiResponse(ApiResponse apiResponse) {
        if (apiResponse != null) {
            System.out.println(apiResponse);
        } else {
            System.out.println("Entity can not be deleted because it does not exist");
        }
    }

}
