package com.hfad.moviedb;

import android.content.Context;

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

    public static enum movieMultipleJsonArrayItems{
        //JSON array "results"
        adult,
        backdrop_path

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

    static final class  MovieMultipleJSONArray {

        static final String ADULT = _ADULT;
        static final String BACKDROP_PATH = _BACKDROP_PATH;
        static  final String ID = _ID;
        static final String ORIGINAL_TITLE = _ORIGINAL_TITLE;
        static final String OVERVIEW = _OVERVIEW;
        static final String POPULARITY = _POPULARITY;
        static  final String POSTER_PATH = _POSTER_PATH;
        static  final String RELEASE_DATE = _RELEASE_DATE;
        static final String TITLE = _TITLE;
        static final String VOTE_AVERAGE = _VOTE_AVERAGE;
        static final String VOTE_COUNT = _VOTE_COUNT;
        static final String GENRE_ID_Array = _GENRE_ID_JSONArray;
    }

    //single movie request
    static final class MovieDetailsJSONArray{

        static final String ADULT = _ADULT;
        static final String BACKDROP_PATH = _BACKDROP_PATH;
        static final String BUDJET = _BUDGET;
        static final String GENRE_ID_ARRAY = _GENRE_ID_JSONArray;
        static final String HOMEPAGE = _HOMEPAGE;
        static final String ID = _ID;
        static final String IMDB_ID = _IMDB_ID;
        static final String ORIGINAL_TITLE = _ORIGINAL_TITLE;
        static final String POPULARITY = _POPULARITY;
        static final String POSTER_PATH = _POSTER_PATH;
        static final String RELEASE_DATE = _RELEASE_DATE;
        static final String REVENUE = _REVENUE;
        static final String RUNTIME  = _RUNTIME;
        static final String STATUS = _STATUS;
        static final String TAGLINE = _TAGLINE;
        static final String TITLE = _TITLE;
        static final String VOTE_AVERAGE = _VOTE_AVERAGE;
        static final String VOTE_COUNT = _VOTE_COUNT;
        static final String OVERVIEW = _OVERVIEW;
    }

}
