package com.mohitilva.moviesfun.model;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WSMetaData {

    public static final SimpleDateFormat wsDateFormat =  new SimpleDateFormat("yyyy-MM-dd");

    //The urls in this class has been replace. These urls do not exists. Replace with working API urls.
    //Get your own API key. The API key here is just a place holder.
    private static final String SORT_POPULARITY_DESC_FILTER = "sort_by=popularity.desc";
    private static final String REL_DATE_LTE_FILTER = "release_date.lte";
    private static final String POPULAR_MOVIES_URL = "https://api.placeholder.moviesws.com/3/discover/movie?" + SORT_POPULARITY_DESC_FILTER;
    private static final String MOVIE_BASE_URL =     "https://api.api.placeholder.moviesws.com/movie";
    private static final String POSTER_PREFIX_PATH = "http://api.placeholder.moviesws.com/p";
    private static final String API_KEY_PARAM =      "api_key=1234567890";

    //The meta data for the JSON object in the Response that we receive form the WS.
    public static final String _BACKDROP_PATH = "backdrop_path";
    public static final String ITEMS_ARRAY_NAME = "results";
    static final String _ADULT = "adult";
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


    public static final String CAST_ARRAY_NAME = "cast";
    public static final String ACTOR_STRING_NAME = "name";

    public static final HashMap<Integer, String> genreIdMap = new HashMap<Integer,String>(){
        {
            put(28,"Action");
            put(12,"Adventure");
            put(16,"Animation");
            put(35,"Comedy");
            put(80,"Crime");
            put(99,"Documentary");
            put(18,"Drama");
            put(10751,"Family");
            put(14,"Fantasy");
            put(10769,"Foreign");
            put(36,"History");
            put(27,"Horror");
            put(10402,"Music");
            put(9648,"Mystery");
            put(10749,"Romance");
            put(878,"Science Fiction");
            put(10770,"TV Movie");
            put(53,"Thriller");
            put(10752,"War");
            put(37,"Western");
        }
    };

    public static String getCreditsUrl(String id){
        return MOVIE_BASE_URL + "/" +id +  "/credits?" + API_KEY_PARAM;
    }

    public static  String getImageUrl(String imageRelPath, String size){
        return POSTER_PREFIX_PATH + "/" + size + imageRelPath + "?" +API_KEY_PARAM;
    }

    public static String getResourceUrl(String resourceRelPath){
        return MOVIE_BASE_URL + "/" + resourceRelPath + "?" + API_KEY_PARAM;
    }

    //Gets a different set of URLs. It can be combined with other results to produce comprehensive list.
    public static String getPopularMoviesUrl(){
        return POPULAR_MOVIES_URL + "&" + API_KEY_PARAM;
    }


    public static String getRecentReleasedMoviesUrl() {
        String url = POPULAR_MOVIES_URL + "&" + API_KEY_PARAM;
        SimpleDateFormat f = wsDateFormat;
        Date today = Calendar.getInstance().getTime();
        String currentDate = f.format(today);
        url = url + "&" + REL_DATE_LTE_FILTER +"=" + currentDate;
        return url;
    }

    public  enum backdropSizes {
        w300,w780,w1280,original
    }

    public  enum posterSizes{
        w45,w92,w154,w185,w342, w500,w780,original
    }

    //single movie request
    public static final class MovieDetailsJSONArray{

        public static final String ADULT = _ADULT;
        public static final String BACKDROP_PATH = _BACKDROP_PATH;
        public static final String BUDJET = _BUDGET;
        public static final String GENRE_ID_ARRAY = "genres";
        public static final String GENRE_NAME = "name";
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


}
