package com.hfad.moviedb;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.hfad.moviedb.MovieDetailsActivity.FAVORITE_PREFERENCES;
import static com.hfad.moviedb.MovieDetailsActivity.ITEM_CAT_MOVIES;

public class FavoriteMovies extends AppCompatActivity {

    FavoriteDataAdapter favoriteDataAdapter;
    Set<String> setFavorites;
    ListView favListView;
    SharedPreferences favorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        favListView = (ListView)findViewById(R.id.favoriteMoviesListView);

        Set<String> setFavMovieIds;

        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavMovieIds = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
        ArrayList<String> favMovieArrayList = new ArrayList<>();
        favMovieArrayList.addAll(setFavMovieIds);

        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());

        favoriteDataAdapter = new FavoriteDataAdapter(this, favMovieArrayList);

        favListView.setAdapter(favoriteDataAdapter);

    }

    public void toggleImage(View view) {

        HashMap<Integer, String> tags = (HashMap<Integer, String>) view.getTag();

        String combinedString = tags.get(1);

        if((Boolean.valueOf(tags.get(0)))){

            //unfavoriting the item
            ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_black_heart_24dp);
            setFavorites.remove(String.valueOf(combinedString));
            tags.put(0, "false");


        }else{

            //favoriting the item
            ((ImageView) view).setImageResource(R.drawable.ic_favorite_black__heart_24dp);
            setFavorites.add(String.valueOf(combinedString));
            tags.put(0,"true");
        }
        view.setTag(tags);
        SharedPreferences.Editor editor = favorites.edit();
        editor.putStringSet("movies", setFavorites);
        editor.commit();

    }
}
