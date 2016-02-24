package com.hfad.moviedb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    ImageView img;
    TextView titleView;
    TextView overviewView;
    ImageView favoriteIcon;
    TextView runTimeTextView;
    boolean isFav = false;
    TextView homePage;
    Set<String> setFavorites;
    SharedPreferences favorites;
    OkHttpClient client = new OkHttpClient();

    public static final String FAVORITE_PREFERENCES = "FAV_PREF";
    public static final String ITEM_CAT_MOVIES = "movies";
     String favStr;
    int runtime;
    float votes;
    String homepage;
    String overview;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        img = (ImageView) findViewById(R.id.moviebackdrop_details);
        titleView = (TextView) findViewById(R.id.title_details);
        overviewView = (TextView) findViewById(R.id.overview_details);
        favoriteIcon = (ImageView) findViewById(R.id.imageView);
        homePage = (TextView) findViewById(R.id.homepage_textbox);
        homePage.setMovementMethod(LinkMovementMethod.getInstance());
        runTimeTextView = (TextView) findViewById(R.id.runtime);


        Intent receivedIntent= getIntent();
        Bundle bundle = receivedIntent.getExtras();

        String backdropPath = bundle.getString("backdropPath");
        String posterPath = bundle.getString("posterPath");
        final Long id = bundle.getLong("id");
        title = bundle.getString("title");

        final String imageUrl = getResources().getString(R.string.poster_prefix_path) + backdropPath + "?"
                + getResources().getString(R.string.api_key_movies_db);


        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + String.valueOf(id) + "?api_key=4eec6698891c4b89358a3779d7f2d212")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    JSONObject responseJSONObj = new JSONObject(response.body().string());
                    homepage = responseJSONObj.getString("homepage");
                    if (homepage == null | homepage.equals("")) homepage = "Not Available";
                    runtime = responseJSONObj.getInt("runtime");
                    votes = responseJSONObj.getLong("vote_average");
                    overview =  responseJSONObj.getString("overview");
                    title = responseJSONObj.getString("title");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homePage.setText(homepage);
                        runTimeTextView.setText(String.valueOf(runtime));
                        titleView.setText(title);
                        overviewView.setText(overview);
                    }
                });

            }
        });

        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
        favStr  = String.valueOf(id) + "|" + title + "|" + posterPath;



        if(setFavorites.contains(String.valueOf(favStr))){
            isFav = true;
            favoriteIcon.setImageResource(android.R.drawable.star_on);

        }else{
            isFav=false;
            favoriteIcon.setImageResource(android.R.drawable.star_off);
        }

        Picasso.with(MovieDetailsActivity.this).load(imageUrl).into(img);

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFavIcon();
            }
        });

    }



    public void editFavIcon(){

        if(!isFav){
            isFav = true;
            favoriteIcon.setImageResource(android.R.drawable.star_on);
            setFavorites.add(String.valueOf(favStr));

        }
        else{

            isFav = false;
            favoriteIcon.setImageResource(android.R.drawable.star_off);
            setFavorites.remove(String.valueOf(favStr));

        }

        SharedPreferences.Editor editor = favorites.edit();
        editor.putStringSet("movies", setFavorites);
        editor.commit();


        favorites = getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());
        Toast.makeText(MovieDetailsActivity.this,  setFavorites.toString(), Toast.LENGTH_SHORT).show();

    }


}
