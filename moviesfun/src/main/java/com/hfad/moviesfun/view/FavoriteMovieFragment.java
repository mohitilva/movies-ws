package com.hfad.moviesfun.view;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.moviesfun.R;
import com.hfad.moviesfun.adapters.FavoriteDataAdapter;
import com.hfad.moviesfun.utilities.FavoriteManager;
import com.hfad.moviesfun.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {


    private final String TAG = getClass().getName();
    private OnListItemClickCallback mCallback;

    public FavoriteMovieFragment() { }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

            mCallback = (OnListItemClickCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnListItemClickCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (getTag() != null) {
            mCallback.updateActivityUI(getTag());
        }

        Context mContext = container.getContext();

        FavoriteManager favManager = new FavoriteManager(mContext);
        Set<String> setFavMovieDetails = favManager.getFavoritesSet();

        if (setFavMovieDetails.size() == 0) {
            View emptyView = inflater.inflate(R.layout.no_items_found, null);
            TextView messageView = (TextView) emptyView.findViewById(R.id.message);
            messageView.setText(mContext.getString(R.string.no_favorites_msg));
            return emptyView;
        }

        View favView = inflater.inflate(R.layout.fragment_favorite_movies, null);
        ListView favListView = (ListView) favView.findViewById(R.id.favoriteMoviesListView);

        ArrayList<String> favMovieDetailsArrayList = new ArrayList<>();
        favMovieDetailsArrayList.addAll(setFavMovieDetails);

        FavoriteDataAdapter adapter = new FavoriteDataAdapter(mContext, favMovieDetailsArrayList);
        favListView.setAdapter(adapter);

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
