package com.hfad.moviesfun.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hfad.moviesfun.R;
import com.hfad.moviesfun.model.WSMetaData;
import com.hfad.moviesfun.utilities.FavoriteManager;
import com.hfad.moviesfun.utilities.Utilities;

import static com.hfad.moviesfun.utilities.FavoriteManager.getFavoriteIcon;
import static com.hfad.moviesfun.utilities.FavoriteManager.getUnfavoriteIconId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;


public class MovieDetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "ID";
    private static final String ARG_PARAM2 = "BACKDROP_PATH";
    private static final String ARG_PARAM3 = "POSTER_PATH";

    private final String TAG = getClass().getName();
    private static final int MAX_ACTORS = 3;
    boolean isFav = false;
    private OkHttpClient client = new OkHttpClient();
    private String favStr;
    private String homepage;
    private String overview;
    private String title;
    private int runtime;
    private String posterPath;
    private long revenue;
    private String movieId;
    private String backdropPath;
    private FavoriteManager favoriteManager;
    private ImageView favoriteIcon;


    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public MovieDetailsFragment(String id, String backDropPath, String posterPath) {
        this.movieId = id;
        this.backdropPath = backDropPath;
        this.posterPath = posterPath;

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, movieId);
        args.putString(ARG_PARAM2, this.backdropPath);
        args.putString(ARG_PARAM3, this.posterPath);
        this.setArguments(args);
    }

    //Get the genre names from the movie JSON object. The name of the genre array is different from the discover object.
    // The object has genre IDs and name. We do not need ID.
    public static String[] getGenreNamesArray(JSONObject movieJSONObj) throws JSONException {


        JSONArray genresJSONArray = movieJSONObj.getJSONArray(WSMetaData.MovieDetailsJSONArray.GENRE_ID_ARRAY);
        String[] genreNameArray = new String[genresJSONArray.length()];
        for (int i = 0; i < genresJSONArray.length(); i++) {

            genreNameArray[i] = genresJSONArray.getJSONObject(i).getString(WSMetaData.MovieDetailsJSONArray.GENRE_NAME);
        }
        return genreNameArray;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getString(ARG_PARAM1);
            backdropPath = getArguments().getString(ARG_PARAM2);
            posterPath = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Context mContext = container.getContext();
        favoriteManager = new FavoriteManager(mContext);
        Utilities utils = new Utilities(mContext);

        ImageView img;
        TextView titleView;
        TextView overviewView;
        TextView runTimeTextView;
        RatingBar ratingBar;
        TextView homePage;
        TextView actorsTextView;
        TextView genreTextView;
        TextView revenueTextView;

        //Set the views
        View fragmentView = inflater.inflate(R.layout.movie_details_layout, null);
        img = (ImageView) fragmentView.findViewById(R.id.moviebackdrop_details);
        titleView = (TextView) fragmentView.findViewById(R.id.title_details);
        overviewView = (TextView) fragmentView.findViewById(R.id.overview_details);
        ratingBar = (RatingBar) fragmentView.findViewById(R.id.rating_bar);
        overviewView.setMovementMethod(new ScrollingMovementMethod());
        favoriteIcon = (ImageView) fragmentView.findViewById(R.id.imageView);
        homePage = (TextView) fragmentView.findViewById(R.id.homepage_textbox);
        homePage.setMovementMethod(LinkMovementMethod.getInstance());
        runTimeTextView = (TextView) fragmentView.findViewById(R.id.runtime);
        actorsTextView = (TextView) fragmentView.findViewById(R.id.actors_textView);
        revenueTextView = (TextView) fragmentView.findViewById(R.id.revenueTextView);
        genreTextView = (TextView) fragmentView.findViewById(R.id.genres_textView);

        String requestUrl = utils.getResourceUrl(String.valueOf(movieId));
        String backdropUrl = utils.getImageUrl(backdropPath, WSMetaData.backdropSizes.w780.toString());
        Picasso.with(mContext).load(backdropUrl)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        String serviceResponse = null;
        String credits = null;
        String creditRequestString = utils.getCreditsUrl(movieId);
        String actors = "";
        String genresText = "";

        try {
            serviceResponse = new ServiceResponseAsyncTask(client).execute(requestUrl).get();
            credits = new ServiceResponseAsyncTask(client).execute(creditRequestString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Parse the network response
        try {
            JSONObject responseJSONObj = new JSONObject(serviceResponse);
            homepage = responseJSONObj.getString(WSMetaData.MovieDetailsJSONArray.HOMEPAGE);
            if (homepage == null | homepage.equals(""))
                homepage = getString(R.string.not_available);
            runtime = responseJSONObj.getInt(WSMetaData.MovieDetailsJSONArray.RUNTIME);
            overview = responseJSONObj.getString(WSMetaData.MovieDetailsJSONArray.OVERVIEW);
            title = responseJSONObj.getString(WSMetaData.MovieDetailsJSONArray.TITLE);
            revenue = responseJSONObj.getLong(WSMetaData.MovieDetailsJSONArray.REVENUE);

            String[] genresArray = getGenreNamesArray(responseJSONObj);
            genresText = utils.getStringFromStringArray(genresArray, 3);

            float ratings = Float.parseFloat(String.valueOf(responseJSONObj.getDouble(WSMetaData.MovieDetailsJSONArray.VOTE_AVERAGE)));
            ratingBar.setRating(ratings / 2);

            //get actors from response
            JSONArray castJsonArray = new JSONObject(credits).getJSONArray(WSMetaData.CAST_ARRAY_NAME);

            for (int i = 0; i < castJsonArray.length(); i++) {
                String cast = castJsonArray.getJSONObject(i).getString(WSMetaData.ACTOR_STRING_NAME);
                actors += ", " + cast;
                if (i == MAX_ACTORS - 1) break;
            }
            actors = Utilities.trimText(actors);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set values for the views
        titleView.setText(title);
        overviewView.setText(overview);
        homePage.setText(homepage);
        actorsTextView.setText(actors);
        genreTextView.setText(genresText);

        int hours = runtime / 60;
        int minutes = runtime % 60;

        if (runtime != 0) {
            if (minutes != 0) runTimeTextView.setText(hours + " hr. " + minutes + " min.");
            else runTimeTextView.setText(hours + " hr.");
        } else runTimeTextView.setText(getString(R.string.not_available));

        setFavoriteIcon();
        String revenueStr = utils.getRevenueFromLong(revenue);

        revenueTextView.setText(revenueStr);
        favoriteIcon.setOnClickListener(new FavoriteIconOnClickListener());
        return fragmentView;
    }

    private void setFavoriteIcon() {

        favStr = FavoriteManager.makeFavString(movieId, title, posterPath, backdropPath);
        if (favoriteManager.isItemFavorite(favStr)) {
            isFav = true;
            favoriteIcon.setImageResource(getFavoriteIcon());

        } else {
            isFav = false;
            favoriteIcon.setImageResource(getUnfavoriteIconId());
        }
    }


    private class FavoriteIconOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (!isFav) {
                isFav = true;
                favoriteIcon.setImageResource(getFavoriteIcon());
                favoriteManager.addToFavorites(favStr);
            } else {
                isFav = false;
                favoriteIcon.setImageResource(getUnfavoriteIconId());
                favoriteManager.removeFromFavorites(favStr);
            }
        }
    }

}
