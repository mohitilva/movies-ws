package com.hfad.moviesfun.utilities;

import android.content.Context;
import android.net.NetworkInfo;

/**
 * Created by mtilva on 3/18/16.
 */
public class ConnectivityManager {

    private Context mContext;


    public ConnectivityManager(Context context) {
        mContext = context;
    }

    public  boolean checkConnectivity() {

        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        return isConnected;
    }
}
