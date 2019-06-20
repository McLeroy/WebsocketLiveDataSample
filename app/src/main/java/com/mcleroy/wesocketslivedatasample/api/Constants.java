package com.mcleroy.wesocketslivedatasample.api;

import android.net.Uri;

import com.mcleroy.wesocketslivedatasample.BuildConfig;

import okhttp3.HttpUrl;

public final class Constants {

    private static final String HTTP_SCHEME = "https", SOCKET_SCHEME = "wss";
//    private static final String HTTP_SCHEME = "http", SOCKET_SCHEME = "ws";
//
//    private static final String HOST = "10.3.1.160";     //<<LOCAL_HOST
    private static final String HOST = "websocketbot.herokuapp.com";     //<<PROD
    private static final String HTTP_VERSION = "v1";


    public static Uri getSocketUrl() {
        return new Uri.Builder()
                .scheme(getSocketScheme())
                .authority(getHost())
                .build();
    }

    public static String getHttpUrl() {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(getHttpScheme())
                .host(getHost())
                .addPathSegment(HTTP_VERSION)
                .addPathSegment("users")
                .build();
        return httpUrl.toString() + "/";
    }

    public static String buildHttpUrlWithAppendPaths(String paths) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(HTTP_SCHEME)
                .host(getHost())
                .addPathSegment(HTTP_VERSION)
                .addPathSegment("users")
                .addPathSegments(paths).build();
        return httpUrl.toString();
    }

    private static String getSocketScheme() {
        return BuildConfig.DEBUG ? SOCKET_SCHEME : "wss";
    }

    private static String getHttpScheme() {
        return BuildConfig.DEBUG ? HTTP_SCHEME : "https";
    }

    private static String getHost() {
        return BuildConfig.DEBUG ? HOST : "websocketbot.herokuapp.com";
    }
}
