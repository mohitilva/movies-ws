package com.hfad.moviesfun;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MoviesAdapter extends BaseAdapter{

    private ArrayList<MovieDataModel> moviesArrayList;
    private Context mContext;
    private Utilities utils;
    private  String TAG = getClass().getName();

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
        ImageView imageView;
        View view;
        TextView titleView;
        TextView releaseDateView;
        RatingBar ratingBar;
        TextView genresTextView;

        if(convertView==null) {
            view = inflater.inflate(R.layout.movie_list_item,null);

        }else{
            view =  convertView;
        }

        imageView = (ImageView) view.findViewById(R.id.movieIconImageView);

        utils = new Utilities(mContext);
        String fullImageUrl = utils.getImageUrl(moviesArrayList.get(position).posterPath, Utilities.posterSizes.w342.toString());

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

        MovieDataModel currentMovieObj = moviesArrayList.get(position);

        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_main_page);
        titleView = (TextView) view.findViewById(R.id.movie_title_textview);
        releaseDateView = (TextView) view.findViewById(R.id.release_date);
        genresTextView = (TextView) view.findViewById(R.id.genres_textView);

        String title = currentMovieObj.title;
        int[] genresIds = currentMovieObj.genres;
        String[] genreNames = Utilities.getGenreNames(genresIds);
        String genreDisplayText = generateGenreText(genreNames);


        float rating = Float.parseFloat(String.valueOf(currentMovieObj.voteAvg));

        //Since rating is out of 10 and we have 5 stars

        ratingBar.setRating(rating / 2);

        titleView.setText(title);

        SimpleDateFormat f = new SimpleDateFormat(Utilities.INPUT_DATE_FORMAT);
        SimpleDateFormat out = new SimpleDateFormat(Utilities.OUTPUT_DATE_FORMAT);
        Date d;
        try {
             d = f.parse(currentMovieObj.releaseDate);
             releaseDateView.setText(out.format(d));
        } catch (ParseException e) {
            releaseDateView.setText("Not Available");
        }

        genresTextView.setText(genreDisplayText);
        return view;


    }

    public String generateGenreText(String[] genreNames){
        String genreText;

        if(genreNames==null || genreNames.length==0){
            return "Not Available";
        }
        else{
            if(genreNames.length==1){
                   genreText = genreNames[0];
            }else{
                 genreText = genreNames[0] + "  " + genreNames[1];
            }

            return genreText;
        }
    }
}
