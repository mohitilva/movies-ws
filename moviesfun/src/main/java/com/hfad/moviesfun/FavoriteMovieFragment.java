package com.hfad.moviesfun;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hfad.moviesfun.Utilities.INTENTPARAMS;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {


    Context mContext;

    private FavoriteDataAdapter adapter;
    private View favView;
    private SharedPreferences favorites;
    private ListView favListView;
    private View favoriteItem;
    private ImageView favIcon;

    private final String TAG = getClass().getName();
    public static final String FAVORITE_PREFERENCES = "FAV_PREF";
    public static final String ITEM_CAT_MOVIES = "movies";


    private OnListItemClickCallback mCallback;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            mCallback = (OnListItemClickCallback) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnListItemClickCallback");
        }

        ((MainActivity) activity).setCurrentFragment(MainActivity.fragmentTags.FAVORITES);
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p/>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mContext = container.getContext();

        favorites = mContext.getSharedPreferences(FAVORITE_PREFERENCES, 0);
        Set<String> setFavMovieDetails =
                favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());

        if(setFavMovieDetails.size()==0){
            View emptyView = inflater.inflate(R.layout.no_items_found,null);
            TextView messageView = (TextView)emptyView.findViewById(R.id.message);
            messageView.setText("You haven't added any favorites. \n" +
                    "To add favorites tap the favorite icon in the movie description.");
            return emptyView;
        }

        favView = inflater.inflate(R.layout.fragment_favorite_movies, null);
        favListView = (ListView) favView.findViewById(R.id.favoriteMoviesListView);

        ArrayList<String> favMovieDetailsArrayList = new ArrayList<>();
        favMovieDetailsArrayList.addAll(setFavMovieDetails);

        adapter = new FavoriteDataAdapter(mContext, favMovieDetailsArrayList);
        favListView.setAdapter(adapter);

        favoriteItem = inflater.inflate(R.layout.favorite_list_item, null);
        favIcon = (ImageView) favoriteItem.findViewById(R.id.fav_icon);

        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String favItems = (String) view.getTag();
                String[] favItemsArray = favItems.split("\\|");
                String movieId = favItemsArray[0];
                //String title = favItemsArray[1];
                String poster_rel_path = favItemsArray[2];
                String backdrop_rel_path = favItemsArray[3];

                mCallback.onListItemClick(Long.parseLong(movieId),
                                            backdrop_rel_path,
                                            poster_rel_path,
                                            MainActivity.fragmentTags.FAVORITES);

            }
        });

        return favView;

    }




}
