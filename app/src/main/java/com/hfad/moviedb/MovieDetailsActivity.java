package com.hfad.moviedb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

public class MovieDetailsActivity extends AppCompatActivity {
    ImageView img;
    TextView titleView;
    TextView overviewView;

    Button button;
    ImageView favoriteIcon;
    boolean isFav = false;
    RatingBar ratingBar;
    TextView homePage;
    Set<String> setFavorites;
    SharedPreferences favorites;
    public static final String FAVORITE_PREFERENCES = "FAV_PREF";
    public static final String ITEM_CAT_MOVIES = "movies";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        img = (ImageView) findViewById(R.id.moviebackdrop_details);
        titleView = (TextView) findViewById(R.id.title_details);
        overviewView = (TextView) findViewById(R.id.overview_details);

        Intent receivedIntent= getIntent();
        Bundle bundle = receivedIntent.getExtras();






        String backdropPath = bundle.getString("backdropPath");
        String overview =  bundle.getString("overview");
        final String title = bundle.getString("title");
        Long id = bundle.getLong("id");
        String imageUrl = getResources().getString(R.string.poster_prefix_path) + backdropPath + "?"
                + getResources().getString(R.string.api_key_movies_db);

        favoriteIcon = (ImageView) findViewById(R.id.imageView);

        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());

        if(setFavorites.contains(title)){
            isFav = true;
            favoriteIcon.setImageResource(R.drawable.ic_favorite_black__heart_24dp);

        }else{
            isFav=false;
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border_black_heart_24dp);
        }




        Picasso.with(MovieDetailsActivity.this).load(imageUrl).into(img);
        titleView.setText(title);
        overviewView.setText(overview);




        //ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        homePage = (TextView) findViewById(R.id.homepage_textbox);
        homePage.setMovementMethod(LinkMovementMethod.getInstance());
        homePage.setText("www.google.com");

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isFav)
                {
                    isFav = true;

                    favoriteIcon.setImageResource(R.drawable.ic_favorite_black__heart_24dp);
                    setFavorites.add(title);

                }
                else{

                    isFav = false;
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_border_black_heart_24dp);
                    setFavorites.remove(title);

                }

                SharedPreferences.Editor editor = favorites.edit();
                editor.putStringSet("movies", setFavorites);
                editor.commit();

                favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
                setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
                Toast.makeText(MovieDetailsActivity.this,  setFavorites.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    class MoviesAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            return null;
        }

        @Override
        protected void onPostExecute(Void v){

        }







    }

}
