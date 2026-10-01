package com.automation.utils;

public class SessionManager {

//    private static boolean isLoggedIn = false;
    

    private static final ThreadLocal<Boolean> isLoggedIn =
            ThreadLocal.withInitial(() -> false);



    public static boolean isLoggedIn() {
        return isLoggedIn.get();
    }

    public static void setLoggedIn(boolean status) {
        isLoggedIn.set(status);
    }

    public static void reset() {
        isLoggedIn.remove();
    }

}