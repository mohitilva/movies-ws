package com.hfad.moviedb.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.hfad.moviedb.R;
import com.hfad.moviedb.utils.Utilities.INTENTPARAMS;
import com.hfad.moviedb.adapters.FavoriteDataAdapter;

import static com.hfad.moviedb.activities.MovieDetailsActivity.FAVORITE_PREFERENCES;
import static com.hfad.moviedb.activities.MovieDetailsActivity.ITEM_CAT_MOVIES;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private FavoriteDataAdapter adapter;
    private Set<String> setFavorites;
    private ListView favListView;
    private SharedPreferences favorites;

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

        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
        adapter = new FavoriteDataAdapter(this, favMovieDetailsArrayList);
        favListView.setAdapter(adapter);
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
        String poster_rel_path = favItemsArray[2];
        String backdrop_rel_path = favItemsArray[3];
        Intent detailsIntent = new Intent(FavoriteMoviesActivity.this, MovieDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(INTENTPARAMS.TITLE.toString(),title);
        bundle.putString(INTENTPARAMS.POSTER_REL_PATH.toString(), poster_rel_path);
        bundle.putString(INTENTPARAMS.BACKDROP_REL_PATH.toString(), backdrop_rel_path);
        bundle.putLong(INTENTPARAMS.ID.toString(), Long.parseLong(movieId));

        detailsIntent.putExtras(bundle);
        startActivity(detailsIntent);

    }
}
