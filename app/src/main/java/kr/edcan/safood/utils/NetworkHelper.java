/*
 * Created by Junseok Oh on 2016.
 * Copyright by Good-Reserve Project by Sunrin Internet High School EDCAN / IWOP / ZEROPEN
 * All rights reversed.
 */

package kr.edcan.safood.utils;

import android.content.Context;
import android.net.ConnectivityManager;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 29..
 */
public class NetworkHelper {
    private Context context;
    final public static String url = "http://iwin247.kr";
    final public static int port = 4000;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public static Retrofit retrofit;

    public static NetworkInterface getNetworkInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url + ":" + port)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(NetworkInterface.class);
    }

    public static boolean returnNetworkState(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
