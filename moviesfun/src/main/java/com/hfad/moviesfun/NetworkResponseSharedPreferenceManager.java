package com.hfad.moviesfun;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


public class NetworkResponseSharedPreferenceManager {

    private Context mContext;
    private SharedPreferences favorites;
    private String TAG = getClass().getName();
    private SharedPreferences.Editor editor;
    private static final String MOVIE_LIST_NETWORK_RESPONSE_PREFERENCES = "MOVIE_LIST_NETWORK_RESPONSE_PREFERENCES";
    private static final String ITEM_CAT = "MAIN";

    public NetworkResponseSharedPreferenceManager(Context c){
        mContext = c;
        favorites = mContext.getSharedPreferences(MOVIE_LIST_NETWORK_RESPONSE_PREFERENCES, 0);
    }

    public String getNetworkString(){


        String networkResponse = favorites.getString(ITEM_CAT, null);
        return networkResponse;
    }



    public void putNetworkString(String favoriteItemToRemove){

        editor = favorites.edit();
        editor.putString(ITEM_CAT, favoriteItemToRemove);
        editor.clear();
        editor.commit();
        Log.d(TAG,"Network Response commited to Shared Preferences" );

    }







}
