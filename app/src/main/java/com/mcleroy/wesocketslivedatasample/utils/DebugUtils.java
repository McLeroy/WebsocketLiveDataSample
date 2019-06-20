package com.mcleroy.wesocketslivedatasample.utils;

import android.util.Log;

public final class DebugUtils {

    public static void debug(Class caller, String message) {
        Log.d(caller.getSimpleName(), message);
    }

    public static void warn(Class caller, String message) {
        Log.w(caller.getSimpleName(), message);
    }
}
