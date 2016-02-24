package com.hfad.moviedb;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mtilva on 2/24/16.
 */
public class Utilities {

    Context mContext;

    public Utilities(){

    }
    public Utilities(Context context){
        mContext = context;
    }

    public static  enum INTENTPARAMS{
        ID,
        TITLE,
        BACKDROP_REL_PATH,
        POSTER_REL_PATH,
    }

    public static enum IMAGE_TYPE{
        POSTER,
        BACKDROP
    }

    public  String getImageUrl(String imageRelPath, String size){
        String prefix =  mContext.getResources().getString(R.string.poster_prefix_path);
        String apiKey = mContext.getResources().getString(R.string.api_key_movies_db);

        return prefix + "/" + size + imageRelPath + "?" + apiKey;
    }

    public static  enum posterSizes{
        w45,w92,w154,w185,w342, w500,w780,original
    }
    public static final HashSet<String> backdropSizes = new HashSet<>(Arrays.asList("w300","w780","w1280","original"));


}
