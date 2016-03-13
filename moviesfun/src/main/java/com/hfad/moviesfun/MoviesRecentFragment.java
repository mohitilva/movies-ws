package com.hfad.moviesfun;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private View loadMore;
    private View fragmentView;

    private OkHttpClient client = new OkHttpClient();
    private MoviesAdapter adapter;
    private ArrayList<MovieDataModel> discover_moviesArrayList;

    private MoviesAdapter moviesAdapter;
    private Context mContext;
    private int page=1;
    private String discover_url;


    ArrayList<MovieDataModel> display2 = new ArrayList<>();

    private  String TAG = getClass().getName();
    private Utilities utils;
    OnListItemClickCallback mCallback;
    Activity hostActivity;

    String discover_responseString;

    public MoviesRecentFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        hostActivity = (MainActivity) activity;
        try{
            mCallback = (OnListItemClickCallback) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnListItemClickCallback");
        }
        //((MainActivity) activity).setCurrentFragment(MainActivity.fragmentTags.MAIN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<MovieDataModel> display1 = new ArrayList<>();

        String fragmentTag = getTag();
        Log.d(TAG, "fragmentTag in fragment="+fragmentTag);

        if(fragmentTag!=null){
            mCallback.updateActivityUI(fragmentTag);
        }else{
            Log.e(TAG, "fragment was null");
        }

        mContext = getActivity().getBaseContext();
        utils = new Utilities(mContext);



        try {
            discover_url = utils.getRecentReleasedMoviesUrl();
            discover_responseString = new ServiceResponseAsyncTask(client).execute(discover_url).get();
            Log.d(TAG, "Got response from network");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        fragmentView =  inflater.inflate(R.layout.fragment_list, null);
        movieListView = (ListView)fragmentView.findViewById(R.id.moviesListView);

        loadMore = inflater.inflate(R.layout.load_more,null);

        movieListView.addFooterView(loadMore);

        loadMore.setOnClickListener(new LoadMoreOnClickListener());

        discover_moviesArrayList = (ArrayList<MovieDataModel>) getListFromNetworkResponse(discover_responseString);
        Log.d(TAG,"Got response from getListFromNetworkResponse()");
        //Sort by date

        Collections.sort(discover_moviesArrayList, new Comparator<MovieDataModel>() {
            @Override
            public int compare(MovieDataModel lhs, MovieDataModel rhs) {
                if(lhs.releaseDate==null || rhs.releaseDate==null) return 0;
                return rhs.releaseDate.compareTo(lhs.releaseDate);
            }
        });

        for(int i=0; i<10; i++) display1.add(discover_moviesArrayList.get(i));

        display1 = addCreditsToList(display1);

        adapter = new MoviesAdapter(mContext,display1);
        movieListView.setAdapter(adapter);

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onListItemClick(id,
                        discover_moviesArrayList.get(position).backdropPath,
                        discover_moviesArrayList.get(position).posterPath,
                        MainActivity.fragmentTags.MAIN);
            }
        });

        return fragmentView;
    }

    protected ArrayList<MovieDataModel> addCreditsToList(ArrayList<MovieDataModel> list){

        String credits;
        for(int k=0; k< list.size()   ;k++){

            MovieDataModel currentMovieObj = list.get(k);
            String creditRequestString = utils.getCreditsUrl(currentMovieObj.id);

            try {

                //credits for each movie.
                credits = new ServiceResponseAsyncTask(client).execute(creditRequestString).get();
                ArrayList<String> actors = new ArrayList<>();
                JSONArray castJsonArray =  new JSONObject(credits).getJSONArray(Utilities.CAST_ARRAY_NAME);

                for(int i=0; i<castJsonArray.length();i++){
                    String cast = castJsonArray.getJSONObject(i).getString(Utilities.ACTOR_STRING_NAME);
                    actors.add(cast);
                }

                list.get(k).actors = actors;
            } catch (JSONException e) {
                e.printStackTrace();
            }  catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }
        return list;
    }

    protected List<MovieDataModel> getListFromNetworkResponse(String networkResponse){

        List<MovieDataModel> moviesList = new ArrayList<>();

        try {

            //This is synchronous call. In case you need async call use enqueue()

            JSONObject movieObj = new JSONObject(networkResponse);
            JSONArray resultsArray = movieObj.getJSONArray(ITEMS_ARRAY_NAME);


            for(int i=0; i<resultsArray.length();i++){

                movieObj =resultsArray.getJSONObject(i);
                JSONArray genresJSONArray = movieObj.getJSONArray(MovieMultipleJSONArray.GENRE_ID_Array);
                int[] genres = null;

                if(genresJSONArray.length()>0){
                    genres = new  int[genresJSONArray.length()];
                    for(int g=0; g<genresJSONArray.length(); g++){
                        genres[g]= genresJSONArray.getInt(g);

                    }
                }
                Long id = movieObj.getLong(Utilities.MovieMultipleJSONArray.ID);
                String title = movieObj.getString(MovieMultipleJSONArray.TITLE);
                String overview = movieObj.getString(MovieMultipleJSONArray.OVERVIEW);
                String backdropUrl = movieObj.getString(MovieMultipleJSONArray.BACKDROP_PATH);
                String posterUrl = movieObj.getString(MovieMultipleJSONArray.POSTER_PATH);
                String release_date = movieObj.getString(MovieMultipleJSONArray.RELEASE_DATE);
                MovieDataModel movieDataObject = new MovieDataModel(id,overview,title,posterUrl, backdropUrl);

                movieDataObject.releaseDate = release_date;
                movieDataObject.genres = genres;

                double voteAvg      =  movieObj.getDouble(MovieMultipleJSONArray.VOTE_AVERAGE);
                movieDataObject.voteAvg = voteAvg;
                moviesList.add(movieDataObject);

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return moviesList;
    }

    private class LoadMoreOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            for(int i=10; i<19; i++) display2.add(discover_moviesArrayList.get(i));

            display2 = addCreditsToList(display2);
            adapter.addItems(display2);
            movieListView.removeFooterView(loadMore);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        Log.d(TAG, "In onSaveInstanceState()");
    }

    @Override
    public void onDestroyView() {


        super.onDestroyView();
        Log.d(TAG, "In onDestroyView()");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d(TAG, "In onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "In onDetach()");
    }
}
