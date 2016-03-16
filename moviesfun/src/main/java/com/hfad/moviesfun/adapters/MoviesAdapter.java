package com.hfad.moviesfun.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hfad.moviesfun.model.MovieDataModel;
import com.hfad.moviesfun.R;
import com.hfad.moviesfun.model.WSMetaData;
import com.hfad.moviesfun.utilities.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesAdapter extends BaseAdapter{

    private ArrayList<MovieDataModel> moviesArrayList;
    private Context mContext;
    private Utilities utils;
    private  String TAG = getClass().getName();
    private OkHttpClient client = new OkHttpClient();

    ImageView imageView;
    View view;
    TextView titleView;
    TextView releaseDateView;
    RatingBar ratingBar;
    TextView genresTextView;
    TextView actorsTextView;

    public MoviesAdapter(Context context, ArrayList<MovieDataModel> dataList){
        super();
        mContext = context;
        moviesArrayList = (ArrayList) dataList;
    }

    public void addItems(ArrayList<MovieDataModel> newList){
        moviesArrayList.addAll(newList);
    }

    @Override
    public int getCount() {
        return moviesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return moviesArrayList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(convertView==null) {
            view = inflater.inflate(R.layout.movie_list_item,null);

        }else{
            view =  convertView;
        }
        MovieDataModel currentMovieObj = moviesArrayList.get(position);

        imageView = (ImageView) view.findViewById(R.id.movieIconImageView);

        utils = new Utilities(mContext);
        String fullImageUrl = utils.getImageUrl(moviesArrayList.get(position).posterPath, WSMetaData.posterSizes.w342.toString());

        Picasso.with(mContext)
                .load(fullImageUrl)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });


        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_main_page);
        titleView = (TextView) view.findViewById(R.id.movie_title_textview);
        releaseDateView = (TextView) view.findViewById(R.id.release_date);
        genresTextView = (TextView) view.findViewById(R.id.genres_textView);
        actorsTextView = (TextView) view.findViewById(R.id.actors_textView);

        //title
        String title = currentMovieObj.title;
        titleView.setText(title);

        //genres
        int[] genresIds = currentMovieObj.genres;
        String[] genreNames = Utilities.getGenreNamesFromIds(genresIds);
        String genreDisplayText = getStringFromStringArray(genreNames,3);
        genresTextView.setText(genreDisplayText);

        //rating
        float rating = Float.parseFloat(String.valueOf(currentMovieObj.voteAvg));
        //Since rating is out of 10 and we have 5 stars
        ratingBar.setRating(rating / 2);

        //Release Date
        SimpleDateFormat f = new SimpleDateFormat(mContext.getString(R.string.input_date_format));
        SimpleDateFormat out = new SimpleDateFormat(mContext.getString(R.string.output_date_format));
        Date d;
        try {
             d = f.parse(currentMovieObj.releaseDate);
             releaseDateView.setText(out.format(d));
        } catch (ParseException e) {
            releaseDateView.setText("Not Available");
        }

        //Actors
        String actors = Utilities.getStringFromArrayList(currentMovieObj.actors, 2);
        actorsTextView.setText(actors);

        return view;

    }



    public static String getStringFromStringArray(String[] genreNames, int limit){
        String genreText = "";

        if(genreNames==null || genreNames.length==0){
            return "Not Available";
        }

        for(int i=0;i<genreNames.length;i++){
            genreText += ", " + genreNames[i];
            if(i==limit-1) break;
        }

        return Utilities.trimText(genreText);

    }

    class GetActors extends AsyncTask<MovieDataModel, Void, String>{

        @Override
        protected String doInBackground(MovieDataModel... params) {
            MovieDataModel movieObj = params[0];
            Request request;
            String creditRequestString = "https://api.themoviedb.org/3/movie/" +movieObj.id +  "/credits?api_key=4eec6698891c4b89358a3779d7f2d212";


            request  = new Request.Builder()
                    .url(creditRequestString)
                    .build();
            Response response;
            String credits;
            String actorsStr = "";
            try {
                response =  client.newCall(request).execute();
                credits = response.body().string();

                ArrayList<String> actors = new ArrayList<>();
                Log.d(TAG, "credits=" + credits);
                JSONObject responseObject;

                responseObject = new JSONObject(credits);
                JSONArray castJsonArray = responseObject.getJSONArray("cast");

                for (int i = 0; i < castJsonArray.length(); i++) {
                    String cast = castJsonArray.getJSONObject(i).getString("name");
                    actors.add(cast);
                    actorsStr +=  cast + " ";
                    if (i == 1) break;
                }
                movieObj.actors = actors;
                //  Log.d(TAG, "Actors=" + actors);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "actors="+actorsStr);
            return actorsStr;

        }

        @Override
        protected void onPostExecute(String actorsString) {

            super.onPostExecute(actorsString);
            actorsTextView.setText(actorsString);
        }
    }
}
