package com.hfad.moviedb.utils;

import android.content.Context;

import com.hfad.moviedb.R;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by mtilva on 2/24/16.
 */
public class Utilities {

    Context mContext;
    static String apiKey;

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
        apiKey = mContext.getResources().getString(R.string.api_key_movies_db);
        return prefix + "/" + size + imageRelPath + "?" + apiKey;
    }

    public String getResourceUrl(String resourceRelPath){
        String prefix =  mContext.getResources().getString(R.string.movie_prefix_path);
        apiKey = mContext.getResources().getString(R.string.api_key_movies_db);
        return prefix + "/" + resourceRelPath + "?" + apiKey;
    }

    public static  enum posterSizes{
        w45,w92,w154,w185,w342, w500,w780,original
    }

    public static enum backdropSizes {
        w300,w780,w1280,original
    }


    static final String _ADULT = "adult";
    static final String _BACKDROP_PATH = "backdrop_path";
    static final String _ID = "id";
    static final String _ORIGINAL_TITLE = "original_title";
    static final String _OVERVIEW = "overview";
    static final String _POPULARITY = "popularity";
    static final String _POSTER_PATH = "poster_path";
    static final String _RELEASE_DATE = "release_date";
    static final String _TITLE = "title";
    static final String _VOTE_AVERAGE = "vote_average";
    static final String _VOTE_COUNT = "vote_count";
    static final String _BUDGET = "budget";
    static final String _HOMEPAGE = "homepage";
    static final String _IMDB_ID = "imdb_id";
    static final String _RUNTIME = "runtime";
    static final String _STATUS = "status";
    static final String _TAGLINE= "tagline";
    static final String _REVENUE = "revenue";
    static final String _GENRE_ID_JSONArray = "genre_ids";

    public static final class  MovieMultipleJSONArray {

        public  static final String ADULT = _ADULT;
        public static final String BACKDROP_PATH = _BACKDROP_PATH;
        public static  final String ID = _ID;
        public  static final String ORIGINAL_TITLE = _ORIGINAL_TITLE;
        public  static final String OVERVIEW = _OVERVIEW;
        public  static final String POPULARITY = _POPULARITY;
        public  static  final String POSTER_PATH = _POSTER_PATH;
        public  static  final String RELEASE_DATE = _RELEASE_DATE;
        public  static final String TITLE = _TITLE;
        public static final String VOTE_AVERAGE = _VOTE_AVERAGE;
        public static final String VOTE_COUNT = _VOTE_COUNT;
        public  static final String GENRE_ID_Array = _GENRE_ID_JSONArray;
    }

    //single movie request
    public static final class MovieDetailsJSONArray{

        public static final String ADULT = _ADULT;
        public static final String BACKDROP_PATH = _BACKDROP_PATH;
        public static final String BUDJET = _BUDGET;
        public static final String GENRE_ID_ARRAY = _GENRE_ID_JSONArray;
        public static final String HOMEPAGE = _HOMEPAGE;
        public static final String ID = _ID;
        public static final String IMDB_ID = _IMDB_ID;
        public static final String ORIGINAL_TITLE = _ORIGINAL_TITLE;
        public static final String POPULARITY = _POPULARITY;
        public static final String POSTER_PATH = _POSTER_PATH;
        public static final String RELEASE_DATE = _RELEASE_DATE;
        public static final String REVENUE = _REVENUE;
        public static final String RUNTIME  = _RUNTIME;
        public static final String STATUS = _STATUS;
        public static final String TAGLINE = _TAGLINE;
        public static final String TITLE = _TITLE;
        public static final String VOTE_AVERAGE = _VOTE_AVERAGE;
        public static final String VOTE_COUNT = _VOTE_COUNT;
        public static final String OVERVIEW = _OVERVIEW;
    }

}