package com.hfad.moviesfun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends BaseAdapter{

    private ArrayList<MovieDataModel> moviesArrayList;
    private Context mContext;
    private Utilities utils;

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

        if(convertView==null) {
            view = inflater.inflate(R.layout.movie_list_item,null);

        }else{
            view =  convertView;
        }

        imageView = (ImageView) view.findViewById(R.id.movieIconImageView);

        utils = new Utilities(mContext);
        String fullImageUrl = utils.getImageUrl(moviesArrayList.get(position).posterPath, Utilities.posterSizes.w342.toString() );

        Picasso.with(mContext)
                .load(fullImageUrl)
                .into(imageView);


        MovieDataModel currentMovieObj = moviesArrayList.get(position);

        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_main_page);
        titleView = (TextView) view.findViewById(R.id.movie_title_textview);
        releaseDateView = (TextView) view.findViewById(R.id.release_date);

        String title = currentMovieObj.title;
        int popularity = currentMovieObj.popularity;
        popularity = popularity / 20;
        ratingBar.setRating(popularity);

        titleView.setText(title);
        releaseDateView.setText(currentMovieObj.releaseDate);

        return view;


    }
}
