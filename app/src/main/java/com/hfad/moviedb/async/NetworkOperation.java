package com.hfad.moviedb.async;

import android.content.Context;
import android.os.AsyncTask;

import com.hfad.moviedb.models.MovieDataObject;
import com.hfad.moviedb.utils.Utilities;
import com.hfad.moviedb.activities.MainActivity;
import com.hfad.moviedb.adapters.MoviesDataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mtilva on 2/23/16.
 */
public class NetworkOperation extends AsyncTask<String, Void, Void> {


    private MainActivity mainActivity;
    private Context mContext;

    public NetworkOperation(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(String... params) {

        String url = params[0];

        mainActivity.moviesArrayList = (ArrayList) getList(url);

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
            Response response = mainActivity.client.newCall(request).execute();
            String responseStr =response.body().string();

            JSONArray resultsArray = new JSONObject(responseStr).getJSONArray(mainActivity.ITEMS_ARRAY_NAME);


            for(int i=0; i<resultsArray.length();i++){

                movieObj =resultsArray.getJSONObject(i);
                Long id = movieObj.getLong(Utilities.MovieMultipleJSONArray.ID);
                String title = movieObj.getString(Utilities.MovieMultipleJSONArray.TITLE);
                String overview = movieObj.getString(Utilities.MovieMultipleJSONArray.OVERVIEW);
                String backdropUrl = movieObj.getString(Utilities.MovieMultipleJSONArray.BACKDROP_PATH);
                String posterUrl = movieObj.getString(Utilities.MovieMultipleJSONArray.POSTER_PATH);
                String release_date = movieObj.getString(Utilities.MovieMultipleJSONArray.RELEASE_DATE);
                MovieDataObject movieDataObject = new MovieDataObject(id,overview,title,posterUrl, backdropUrl);
                movieDataObject.id = id;
                movieDataObject.releaseDate = release_date;

                Double popularity      = movieObj.getDouble(Utilities.MovieMultipleJSONArray.POPULARITY);
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
        mainActivity.adapter = new MoviesDataAdapter(mainActivity, mainActivity.moviesArrayList);
        mainActivity.movieListView.setAdapter(mainActivity.adapter);

    }


}
