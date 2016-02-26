package com.hfad.moviedb.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.hfad.moviedb.models.MovieDataObject;
import com.hfad.moviedb.R;
import com.hfad.moviedb.utils.Utilities.*;
import com.hfad.moviedb.adapters.MoviesDataAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String ITEMS_ARRAY_NAME = "results";
    private ListView movieListView;
    private View favoriteBar;
    private View loadMore;

    private OkHttpClient client = new OkHttpClient();

    private ArrayList<MovieDataObject> moviesArrayList = new ArrayList<>();
    private MoviesDataAdapter adapter;

    private int page=1;
    private String request_url;
    private ArrayList<MovieDataObject> extendedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        request_url = getResources().getText(R.string.popular_movies_url)
                + "&" + getResources().getText(R.string.api_key_movies_db);

        NetworkOperation networkOperation = new NetworkOperation();
        networkOperation.execute(request_url);

        movieListView.setOnItemClickListener(new OnListItemClickListener());

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMore loadMore1 = new LoadMore();
                loadMore1.execute(request_url + "&page=" + String.valueOf(++page));
            }
        });

        favoriteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favIntent = new Intent(MainActivity.this, FavoriteMoviesActivity.class);
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

    private class NetworkOperation extends AsyncTask<String, Void, Void> {

        public NetworkOperation() {

        }

        @Override
        protected Void doInBackground(String... params) {

            String url = params[0];

            moviesArrayList = (ArrayList) getList(url);

            return null;
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

                JSONArray resultsArray = new JSONObject(responseStr).getJSONArray(ITEMS_ARRAY_NAME);


                for(int i=0; i<resultsArray.length();i++){

                    movieObj =resultsArray.getJSONObject(i);
                    Long id = movieObj.getLong(MovieMultipleJSONArray.ID);
                    String title = movieObj.getString(MovieMultipleJSONArray.TITLE);
                    String overview = movieObj.getString(MovieMultipleJSONArray.OVERVIEW);
                    String backdropUrl = movieObj.getString(MovieMultipleJSONArray.BACKDROP_PATH);
                    String posterUrl = movieObj.getString(MovieMultipleJSONArray.POSTER_PATH);
                    String release_date = movieObj.getString(MovieMultipleJSONArray.RELEASE_DATE);
                    MovieDataObject movieDataObject = new MovieDataObject(id,overview,title,posterUrl, backdropUrl);

                    movieDataObject.releaseDate = release_date;

                    Double popularity      = movieObj.getDouble(MovieMultipleJSONArray.POPULARITY);
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

        @Override
        protected void onPostExecute(Void voids) {
            adapter = new MoviesDataAdapter(MainActivity.this, moviesArrayList);
            movieListView.setAdapter(adapter);
            Log.d("MainActivity","Adapter set.");

        }


    }

    private  class LoadMore extends NetworkOperation {



        public LoadMore() {

            super();
        }

        @Override
        protected Void doInBackground(String... params) {

            String url = params[0];
            extendedlist = (ArrayList) getList(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            adapter.addItems(extendedlist);
            adapter.notifyDataSetChanged();
            //mainActivity.loadMoreTaskListener.loadMoreTaskComplete();

        }
    }
}
