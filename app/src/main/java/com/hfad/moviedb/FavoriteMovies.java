package com.hfad.moviedb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

        Set<String> setFavMovieDetails;

        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavMovieDetails = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
        ArrayList<String> favMovieDetailsArrayList = new ArrayList<>();
        favMovieDetailsArrayList.addAll(setFavMovieDetails);

        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());

        favoriteDataAdapter = new FavoriteDataAdapter(this, favMovieDetailsArrayList);
        favListView.setAdapter(favoriteDataAdapter);


    }

    public void toggleImage(View view) {

        HashMap<Integer, String> tags = (HashMap<Integer, String>) view.getTag();

        String combinedString = tags.get(1);

        if((Boolean.valueOf(tags.get(0)))){

            //unfavoriting the item
            ((ImageView) view).setImageResource(android.R.drawable.star_off);
            setFavorites.remove(String.valueOf(combinedString));
            tags.put(0, "false");


        }else{

            //favoriting the item
            ((ImageView) view).setImageResource(android.R.drawable.star_on);
            setFavorites.add(String.valueOf(combinedString));
            tags.put(0,"true");
        }
        view.setTag(tags);
        SharedPreferences.Editor editor = favorites.edit();
        editor.putStringSet("movies", setFavorites);
        editor.commit();

    }

    public void onFavItemClick(View view) {

        String favItems = (String) view.getTag();
        String[] favItemsArray = favItems.split("\\|");
        String movieId = favItemsArray[0];
        String title = favItemsArray[1];
        String picUrl = favItemsArray[2];
        Intent detailsIntent = new Intent(FavoriteMovies.this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("backdropPath",picUrl);
        bundle.putLong("id",Long.parseLong(movieId));
        bundle.putBoolean("isFav",true);
        detailsIntent.putExtras(bundle);
        startActivity(detailsIntent);

    }
}
