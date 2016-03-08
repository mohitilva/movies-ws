package com.hfad.moviesfun;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;


public class FavoriteManager {

    Context mContext;
    private SharedPreferences favorites;

    SharedPreferences.Editor editor;
    private static final String FAVORITE_PREFERENCES = "FAV_PREF";
    private static final String ITEM_CAT_MOVIES = "movies";
    private static final String favorite_delimiter = "|";

    //private Set
    public FavoriteManager(Context c){
        mContext = c;
    }

    private Set<String> getFavoritesSet(){

        favorites = mContext.getSharedPreferences(FAVORITE_PREFERENCES, 0);
        Set<String> setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
        return setFavorites;
    }

    public void addToFavorites(String favoriteItem){


        Set<String> setFavorites =  getFavoritesSet();
        setFavorites.add(String.valueOf(favoriteItem));
        editor = favorites.edit();
        editor.putStringSet(ITEM_CAT_MOVIES, setFavorites);
        editor.clear();
        editor.apply();
    }

    public void removeFromFavorites(String favoriteItemToRemove){

        Set<String> setFavorites =  getFavoritesSet();
        setFavorites.remove(String.valueOf(favoriteItemToRemove));
        editor = favorites.edit();
        editor.putStringSet(ITEM_CAT_MOVIES, setFavorites);
        editor.clear();
        editor.apply();

    }

    public boolean isItemFavorite(String item){
        Set<String> setFavorites =  getFavoritesSet();
        return setFavorites.contains(item);

    }

    public static String makeFavString(String id, String title, String posterPath, String backDropPath){
       return (id + favorite_delimiter +
               title + favorite_delimiter +
               posterPath + favorite_delimiter +
               backDropPath);
    }

    public static String getFavoriteDelimiter(){
        return "\\" + favorite_delimiter;
    }

}
