package com.hfad.moviesfun;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;

import com.hfad.moviesfun.Utilities.MovieMultipleJSONArray;
/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class MoviesRecentFragment extends Fragment {

    public static final String ITEMS_ARRAY_NAME = "results";
    private ListView movieListView;
    private View headerView;
    private View loadMore;
    private View fragmentView;
    private View favoriteView;
    private OkHttpClient client = new OkHttpClient();
    private MoviesAdapter adapter;
    private ArrayList<MovieDataModel> moviesArrayList;
    private MoviesAdapter moviesAdapter;
    private Context mContext;
    private int page=1;
    private String request_url;
    private ArrayList<MovieDataModel> extendedlist;
    private String networkResponse;
    private static String TAG = "MainActivity";
    private Utilities utils;
    OnListItemClickCallback mCallback;
    public MoviesRecentFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnListItemClickCallback) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnListItemClickCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getBaseContext();
        utils = new Utilities(mContext);
        request_url = getResources().getText(R.string.popular_movies_url)
                + "&" + getResources().getText(R.string.api_key_movies_db);


        fragmentView =  inflater.inflate(R.layout.fragment_list,null);
        movieListView = (ListView)fragmentView.findViewById(R.id.moviesListView);

        loadMore =  inflater.inflate(R.layout.load_more,null);
        movieListView.addFooterView(loadMore, null, true);

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loadMoreUrl = request_url +  "&page=" + String.valueOf(++page);
                String  responseString = null;
                try {
                    responseString = new ServiceResponseAsyncTask(client).execute(loadMoreUrl).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                extendedlist = (ArrayList<MovieDataModel>) getListFromNetworkResponse(responseString);
                adapter.addItems(extendedlist);
                adapter.notifyDataSetChanged();
            }
        });

        String responseString = null;

        try {
            responseString = new ServiceResponseAsyncTask(client).execute(request_url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        moviesArrayList = (ArrayList<MovieDataModel>) getListFromNetworkResponse(responseString);
        adapter = new MoviesAdapter(mContext, moviesArrayList);
        movieListView.setAdapter(adapter);
        Log.d(TAG,"Adapter set.");

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onListItemClick(id,
                        moviesArrayList.get(position).backdropPath,
                        moviesArrayList.get(position).posterPath);
            }
        });

        return fragmentView;

    }


    protected List<MovieDataModel> getListFromNetworkResponse(String networkResponse){

        List<MovieDataModel> moviesList = new ArrayList<>();

        try {

            //This is synchronous call. In case you need async call use enqueue()

            JSONObject movieObj = new JSONObject(networkResponse);
            JSONArray resultsArray = movieObj.getJSONArray(ITEMS_ARRAY_NAME);


            for(int i=0; i<resultsArray.length();i++){

                movieObj =resultsArray.getJSONObject(i);
                Long id = movieObj.getLong(Utilities.MovieMultipleJSONArray.ID);
                String title = movieObj.getString(MovieMultipleJSONArray.TITLE);
                String overview = movieObj.getString(MovieMultipleJSONArray.OVERVIEW);
                String backdropUrl = movieObj.getString(MovieMultipleJSONArray.BACKDROP_PATH);
                String posterUrl = movieObj.getString(MovieMultipleJSONArray.POSTER_PATH);
                String release_date = movieObj.getString(MovieMultipleJSONArray.RELEASE_DATE);
                MovieDataModel movieDataObject = new MovieDataModel(id,overview,title,posterUrl, backdropUrl);

                movieDataObject.releaseDate = release_date;

                Double popularity      = movieObj.getDouble(MovieMultipleJSONArray.POPULARITY);
                movieDataObject.popularity = popularity.intValue();
                moviesList.add(movieDataObject);

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return moviesList;
    }


}
