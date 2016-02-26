package com.hfad.moviedb.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

import com.hfad.moviedb.async.LoadMore;
import com.hfad.moviedb.models.MovieDataObject;
import com.hfad.moviedb.async.NetworkOperation;
import com.hfad.moviedb.R;
import com.hfad.moviedb.utils.Utilities.*;
import com.hfad.moviedb.adapters.MoviesDataAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String ITEMS_ARRAY_NAME = "results";
    public ListView movieListView;
    View favoriteBar;
    View loadMore;

    public OkHttpClient client = new OkHttpClient();

    public ArrayList<MovieDataObject> moviesArrayList = new ArrayList<>();
    public MoviesDataAdapter adapter;

    int page=1;
    String request_url;
    public ArrayList<MovieDataObject> extendedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        request_url = getResources().getText(R.string.popular_movies_url)
                + "&" + getResources().getText(R.string.api_key_movies_db);

        NetworkOperation networkOperation = new NetworkOperation(this);
        networkOperation.execute(request_url);

        movieListView.setOnItemClickListener(new OnListItemClickListener());

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMore loadMore1 = new LoadMore(MainActivity.this);
                loadMore1.execute(request_url + "&page=" + String.valueOf(++page));
            }
        });

        favoriteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favIntent = new Intent(MainActivity.this, FavoriteMovies.class);
                startActivity(favIntent);
            }
        });
    }

    private void setViews(){
        movieListView = (ListView) findViewById(R.id.moviesListView);

        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //add footerview to listview  - load more
        loadMore =  inflater.inflate(R.layout.load_more,null);
        movieListView.addFooterView(loadMore, null, true);

        //add header view - favorite
        favoriteBar = inflater.inflate(R.layout.favorite_bar,null);
        movieListView.addHeaderView(favoriteBar);

    }

    private class OnListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            --position;
            Intent movieIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(INTENTPARAMS.BACKDROP_REL_PATH.toString(), moviesArrayList.get(position).backdropPath);
            bundle.putString(INTENTPARAMS.POSTER_REL_PATH.toString(), moviesArrayList.get(position).posterPath);
            bundle.putLong(INTENTPARAMS.ID.toString(), id);
            bundle.putString(INTENTPARAMS.TITLE.toString(), moviesArrayList.get(position).title);
            movieIntent.putExtras(bundle);
            startActivity(movieIntent);
        }
    }
}
