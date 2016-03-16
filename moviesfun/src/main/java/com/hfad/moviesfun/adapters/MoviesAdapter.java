package com.hfad.moviesfun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hfad.moviesfun.R;
import com.hfad.moviesfun.model.MovieDataModel;
import com.hfad.moviesfun.model.WSMetaData;
import com.hfad.moviesfun.utilities.Utilities;
import com.hfad.moviesfun.view.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MoviesAdapter extends BaseAdapter{

    private ArrayList<MovieDataModel> moviesArrayList;
    private Context mContext;
    private  String TAG = getClass().getName();

    public MoviesAdapter(Context context, ArrayList<MovieDataModel> dataList){
        super();
        mContext = context;
        moviesArrayList =  dataList;
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

        View view;
        if(convertView==null) {
            view = inflater.inflate(R.layout.movie_list_item,null);

        }else{
            view =  convertView;
        }
        MovieDataModel currentMovieObj = moviesArrayList.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.movieIconImageView);

        Utilities utils = new Utilities(mContext);
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

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_main_page);
        TextView titleView = (TextView) view.findViewById(R.id.movie_title_textview);
        TextView releaseDateView = (TextView) view.findViewById(R.id.release_date);
        TextView genresTextView = (TextView) view.findViewById(R.id.genres_textView);
        TextView actorsTextView = (TextView) view.findViewById(R.id.actors_textView);

        //title
        String title = currentMovieObj.title;
        titleView.setText(title);

        //genres
        int[] genresIds = currentMovieObj.genres;
        String[] genreNames = Utilities.getGenreNamesFromIds(genresIds);
        String genreDisplayText = new Utilities(mContext).getStringFromStringArray(genreNames, 3);
        genresTextView.setText(genreDisplayText);

        //rating
        float rating = Float.parseFloat(String.valueOf(currentMovieObj.voteAvg));
        //Since rating is out of 10 and we have 5 stars
        ratingBar.setRating(rating / 2);

        //Release Date
        SimpleDateFormat f = WSMetaData.wsDateFormat;
        SimpleDateFormat out = MainActivity.outputDateFormat;
        Date d;
        try {
             d = f.parse(currentMovieObj.releaseDate);
             releaseDateView.setText(out.format(d));
        } catch (ParseException e) {
            releaseDateView.setText(mContext.getString(R.string.not_available));
        }

        //Actors
        String actors = Utilities.getStringFromArrayList(currentMovieObj.actors, 2);
        actorsTextView.setText(actors);

        return view;

    }

}
