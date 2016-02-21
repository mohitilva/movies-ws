package com.hfad.moviedb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import static com.hfad.moviedb.MovieDetailsActivity.FAVORITE_PREFERENCES;
import static com.hfad.moviedb.MovieDetailsActivity.ITEM_CAT_MOVIES;

public class MainActivity extends AppCompatActivity {

    ListView movieListView;
    OkHttpClient client = new OkHttpClient();
    JSONObject jsonRespObj;
    AsyncTaskCompleteListener asyncTaskCompleteListener;
    LoadMoreTaskListener loadMoreTaskListener;
    ArrayList<MovieDataObject> moviesArrayList = new ArrayList<MovieDataObject>();
    View favoriteBar;
    MoviesDataAdapter adapter;
    int page=1;
    String url;
    ArrayList<MovieDataObject> extendedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieListView = (ListView) findViewById(R.id.moviesListView);
        url = getResources().getText(R.string.popular_movies_url) + "&" + getResources().getText(R.string.api_key_movies_db);

        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View loadMore =  inflater.inflate(R.layout.load_more,null);
        movieListView.addFooterView(loadMore, null, true);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMore loadMore1 = new LoadMore(new LoadMoreTaskListener() {
                    @Override
                    public void loadMoreTaskComplete() {
                        adapter.addItems(extendedlist);
                        adapter.notifyDataSetChanged();
                    }
                });
                loadMore1.execute(url + "&page=" + String.valueOf(++page));
            }});

        favoriteBar = inflater.inflate(R.layout.favorite_bar,null);
        movieListView.addHeaderView(favoriteBar);

        favoriteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favIntent = new Intent(MainActivity.this, FavoriteMovies.class);
                startActivity(favIntent);
            }
        });

        NetworkOperation networkOperation = new NetworkOperation(new AsyncTaskCompleteListener(){

            @Override
            public void taskComplete() {

                movieListView.setAdapter(adapter);
                movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        --position;
                        Intent movieIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("backdropPath", moviesArrayList.get(position).backdropPath);
                        bundle.putLong("id", id);
                        bundle.putString("title",moviesArrayList.get(position).title);
                        movieIntent.putExtras(bundle);
                        startActivity(movieIntent);

                    }
                });
            }
        });

        networkOperation.execute(url);

    }

    protected List<MovieDataObject> getList(String url){

        JSONObject movieObj;
        Request request = new Request.Builder()
                .url(url)
                .build();

        ArrayList<MovieDataObject> moviesList = new ArrayList<>();

        try {
            //This is synchronous call. In case you need async call use enqueue()
            Response response = client.newCall(request).execute();
            String responseStr =response.body().string();
            jsonRespObj = new JSONObject(responseStr);
            JSONArray resultsArray = new JSONObject(responseStr).getJSONArray("results");


            for(int i=0; i<resultsArray.length();i++){

                movieObj =resultsArray.getJSONObject(i);
                Long id = movieObj.getLong("id");
                String title = movieObj.getString("title");
                String overview = movieObj.getString("overview");
                String backdropUrl = movieObj.getString("backdrop_path");
                String posterUrl = movieObj.getString("poster_path");
                String release_date = movieObj.getString("release_date");
                MovieDataObject movieDataObject = new MovieDataObject(id,overview,title,posterUrl, backdropUrl);
                movieDataObject.id = id;
                movieDataObject.releaseDate = release_date;

                Double popularity      = movieObj.getDouble("popularity");
                movieDataObject.popularity = popularity.intValue();
                moviesList.add(movieDataObject);

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    class NetworkOperation extends AsyncTask<String, Void, Void>{


        public NetworkOperation(AsyncTaskCompleteListener listener) {
            asyncTaskCompleteListener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {

            String url = params[0];
            moviesArrayList = (ArrayList) getList(url);
            adapter = new MoviesDataAdapter(MainActivity.this, moviesArrayList);

        return null;
        }

        @Override
        protected void onPostExecute(Void v){
            asyncTaskCompleteListener.taskComplete();

        }


    }

    class LoadMore extends AsyncTask<String, Void, Void>{

        JSONObject movieObj;
        public LoadMore(LoadMoreTaskListener listener) {
            loadMoreTaskListener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {

            String url = params[0];

            extendedlist = (ArrayList) getList(url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            loadMoreTaskListener.loadMoreTaskComplete();

        }
    }
}
