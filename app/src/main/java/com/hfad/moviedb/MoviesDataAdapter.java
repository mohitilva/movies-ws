package com.hfad.moviedb;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtilva on 2/16/16.
 */
public class MoviesDataAdapter extends BaseAdapter{

    ArrayList<MovieDataObject> moviesArrayList;
    Context mContext;

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */

    public MoviesDataAdapter(Context context, ArrayList<MovieDataObject> dataList){
        super();
        mContext = context;
        moviesArrayList = (ArrayList) dataList;
    }

    public void addItem(MovieDataObject newItem){
        moviesArrayList.add(newItem);

    }

    public void addItems(ArrayList<MovieDataObject> newList){
        moviesArrayList.addAll(newList);
    }

    @Override
    public int getCount() {
        return moviesArrayList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return moviesArrayList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return moviesArrayList.get(position).id;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView imageView;
        View view;
        TextView titleView;
        TextView releaseDateView;
        RatingBar ratingBar;

        if(convertView==null) {
            view = inflater.inflate(R.layout.movie_list_iem,null);

        }else{
            view =  convertView;
        }

        imageView = (ImageView) view.findViewById(R.id.movieIconImageView);

        String fullImageUrl =  mContext.getResources().getString(R.string.image_path)
                + mContext.getResources().getString(R.string.image_size_w300)
                + moviesArrayList.get(position).posterPath
                + "&" + mContext.getResources().getText(R.string.api_key_movies_db);
        Picasso.with(mContext)
                .load(fullImageUrl)

                .into(imageView);


        MovieDataObject currentMovieObj = moviesArrayList.get(position);

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
