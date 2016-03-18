package com.mohitilva.moviesfun.view;

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
import android.widget.Toast;

import com.mohitilva.moviesfun.R;
import com.mohitilva.moviesfun.adapters.MoviesAdapter;
import com.mohitilva.moviesfun.model.MovieDataModel;
import com.mohitilva.moviesfun.model.WSMetaData;
import com.mohitilva.moviesfun.utilities.ConnectivityManager;
import com.mohitilva.moviesfun.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;


public class MoviesListFragment extends Fragment {

    private ArrayList<MovieDataModel> display2 = new ArrayList<>();
    private OnListItemClickCallback mCallback;
    private String discover_responseString;
    private ListView movieListView;
    private View loadMore;
    private OkHttpClient client = new OkHttpClient();
    private MoviesAdapter adapter;
    private ArrayList<MovieDataModel> discover_moviesArrayList;
    private  String TAG = getClass().getName();
    private Context mContext;

    public MoviesListFragment() {
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

        mContext = container.getContext();


        if (getTag() != null) {
            mCallback.updateActivityUI(getTag());
        }
        mContext = container.getContext();

        View fragmentView = inflater.inflate(R.layout.fragment_list, null);
        movieListView = (ListView) fragmentView.findViewById(R.id.moviesListView);
        loadMore = inflater.inflate(R.layout.load_more, null);
        loadMore.setOnClickListener(new LoadMoreOnClickListener());

        //Check if we have response saved in singleton.
        MoviesListSingleton singleton = MoviesListSingleton.getInstance();
        ArrayList<MovieDataModel> savedList = singleton.getData();
        ArrayList<MovieDataModel> display1 = new ArrayList<>();

        try {

        if (savedList == null) {
            //Log.d(TAG, "Getting response from network");

                String discover_url = WSMetaData.getRecentReleasedMoviesUrl();
                discover_responseString = new ServiceResponseAsyncTask(client).execute(discover_url).get();


                discover_moviesArrayList = (ArrayList<MovieDataModel>) getListFromNetworkResponse(discover_responseString);

                //Sort by date
                Collections.sort(discover_moviesArrayList, new Comparator<MovieDataModel>() {
                    @Override
                    public int compare(MovieDataModel lhs, MovieDataModel rhs) {
                        if (lhs.releaseDate == null || rhs.releaseDate == null) return 0;
                        return rhs.releaseDate.compareTo(lhs.releaseDate);
                    }
                });

                //Get the actors name and add to list.
                discover_moviesArrayList = addCreditsToList(discover_moviesArrayList);


                movieListView.addFooterView(loadMore);

                for (int i = 0; i < 10; i++) display1.add(discover_moviesArrayList.get(i));


                adapter = new MoviesAdapter(mContext, display1);
                movieListView.setAdapter(adapter);

            } else {
                //Log.d(TAG, "Using saved response");
                discover_moviesArrayList = savedList;

                if (singleton.getLoadMore()) {
                    adapter = new MoviesAdapter(mContext, discover_moviesArrayList);
                } else {
                    for (int i = 0; i < 10; i++) display1.add(discover_moviesArrayList.get(i));
                    //  display1 = addCreditsToList(display1);
                    movieListView.addFooterView(loadMore);
                    adapter = new MoviesAdapter(mContext, display1);
                }
                movieListView.setAdapter(adapter);
            }
        }
        //Here we are just catching all exceptions and throwables and displaying message. Individual exceptions should be handled.
        catch (Throwable t){
            Utilities.showGeneralErrorToast(mContext);
        }

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
        try{
            for(int k=0; k< list.size()   ;k++){

                MovieDataModel currentMovieObj = list.get(k);
                String creditRequestString = WSMetaData.getCreditsUrl(String.valueOf(currentMovieObj.id));


                    //credits for each movie.
                    credits = new ServiceResponseAsyncTask(client).execute(creditRequestString).get();

                    ArrayList<String> actors = new ArrayList<>();
                    JSONArray castJsonArray =  new JSONObject(credits).getJSONArray(WSMetaData.CAST_ARRAY_NAME);

                    for(int i=0; i<castJsonArray.length();i++){
                        String cast = castJsonArray.getJSONObject(i).getString(WSMetaData.ACTOR_STRING_NAME);
                        actors.add(cast);
                    }

                    list.get(k).actors = actors;
                }


        }
        //Here we are just catching all exceptions and throwables and displaying message. Individual exceptions should be handled.
        catch (Throwable t){
            Utilities.showGeneralErrorToast(mContext);

        }

        return list;
    }

    //return array of genreIds from single movieJSONObj
    public static int[] getGenreArray(JSONObject movieJSONObj) throws JSONException {

        int[] genres = null;
        JSONArray genresJSONArray = movieJSONObj.getJSONArray(WSMetaData.MovieMultipleJSONArray.GENRE_ID_Array);

        if (genresJSONArray.length() > 0) {
            genres = new int[genresJSONArray.length()];
            for (int g = 0; g < genresJSONArray.length(); g++) {
                genres[g] = genresJSONArray.getInt(g);
            }
        }
        return genres;
    }


    protected List<MovieDataModel> getListFromNetworkResponse(String networkResponse){

        List<MovieDataModel> moviesList = new ArrayList<>();

        try {

            JSONObject movieObj = new JSONObject(networkResponse);
            JSONArray resultsArray = movieObj.getJSONArray(WSMetaData.ITEMS_ARRAY_NAME);

            //for each movie
            for(int i=0; i<resultsArray.length();i++){

                movieObj =resultsArray.getJSONObject(i);
                int[] genres = getGenreArray(movieObj);
                Long id = movieObj.getLong(WSMetaData.MovieMultipleJSONArray.ID);
                String title = movieObj.getString(WSMetaData.MovieMultipleJSONArray.TITLE);
                String overview = movieObj.getString(WSMetaData.MovieMultipleJSONArray.OVERVIEW);
                String backdropUrl = movieObj.getString(WSMetaData.MovieMultipleJSONArray.BACKDROP_PATH);
                String posterUrl = movieObj.getString(WSMetaData.MovieMultipleJSONArray.POSTER_PATH);
                String release_date = movieObj.getString(WSMetaData.MovieMultipleJSONArray.RELEASE_DATE);
                MovieDataModel movieDataObject = new MovieDataModel(id,overview,title,posterUrl, backdropUrl);

                movieDataObject.releaseDate = release_date;
                movieDataObject.genres = genres;

                double voteAvg      =  movieObj.getDouble(WSMetaData.MovieMultipleJSONArray.VOTE_AVERAGE);
                movieDataObject.voteAvg = voteAvg;
                moviesList.add(movieDataObject);

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Throwable t){
            Utilities.showGeneralErrorToast(mContext);
        }
        return moviesList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

     class LoadMoreOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ConnectivityManager connectivityManager = new ConnectivityManager(mContext);
            if(!connectivityManager.checkConnectivity()){
                Toast.makeText(mContext,mContext.getString(R.string.no_internet_msg_short),Toast.LENGTH_SHORT).show();
                return;
            }


            for(int i=10; i<20; i++) display2.add(discover_moviesArrayList.get(i));

            display2 = addCreditsToList(display2);
            adapter.addItems(display2);
            movieListView.removeFooterView(loadMore);
            adapter.notifyDataSetChanged();
            MoviesListSingleton mySingleton = MoviesListSingleton.getInstance();
            mySingleton.setLoadMore(true);
        }
    }

    @Override
    public void onPause() {
        MoviesListSingleton singleton = MoviesListSingleton.getInstance();
        singleton.saveData(discover_moviesArrayList);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
