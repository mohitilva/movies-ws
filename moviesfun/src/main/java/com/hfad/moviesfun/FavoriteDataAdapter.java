package com.hfad.moviesfun;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FavoriteDataAdapter extends BaseAdapter{

    private static final String TAG = "com.hfad.moviesfun.FavoriteDataAdapter";
    private ArrayList<String> listFavoriteIds;
    private ArrayList<String> listTitles;
    private ArrayList<String> listIconUrls;
    private ArrayList<String> listCombined;
    private Context mContext;
    private LayoutInflater inflater;

    public static final String FAVORITE_PREFERENCES = "FAV_PREF";
    public static final String ITEM_CAT_MOVIES = "movies";
    private SharedPreferences favorites;
    private Set<String> setFavorites;


    public FavoriteDataAdapter(Context context, List<String> favStrList){

        mContext = context;
        listCombined = (ArrayList) favStrList;
        listFavoriteIds = new ArrayList<>();
        listTitles = new ArrayList<>();
        listIconUrls = new ArrayList<>();
        for(int i=0 ; i< favStrList.size(); i++){

            String[] components = favStrList.get(i).split("\\|");
            listFavoriteIds.add(components[0]);
            listTitles.add(components[1]);
            listIconUrls.add(components[2]);

        }
        Log.d(TAG,"favoritecount="+listFavoriteIds.size());

    }

    @Override
    public int getCount() {

        return listFavoriteIds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listFavoriteIds.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        ImageView posterImgView;
        ImageView favIconImgView;
        TextView titleView;

        if(convertView==null) {
            view = inflater.inflate(R.layout.favorite_list_item,null);

        }else{
            view =  convertView;
        }

        posterImgView = (ImageView) view.findViewById(R.id.fav_movie_icon_ImageView);

        favIconImgView = (ImageView)view.findViewById(R.id.fav_icon);


        HashMap<Integer, String> favIconTag = new HashMap<>();
        favIconTag.put(0, "true");
        favIconTag.put(1, listCombined.get(position));

        favIconImgView.setTag(favIconTag);
        favIconImgView.setImageResource(android.R.drawable.star_on);


        titleView = (TextView)view.findViewById(R.id.fav_movie_title_textview);

        String fullIconUrl = mContext.getResources().getString(R.string.poster_prefix_path) + "/"
                + mContext.getResources().getString(R.string.image_size_w300)
                + listIconUrls.get(position) + "?"
                + mContext.getResources().getString(R.string.api_key_movies_db);

        Picasso.with(mContext)
                .load(fullIconUrl)
                .into(posterImgView);

        titleView.setText(listTitles.get(position));

        view.setTag(listCombined.get(position));
        posterImgView.setTag(listCombined.get(position));
        titleView.setTag(listCombined.get(position));

        favorites = mContext.getSharedPreferences(FAVORITE_PREFERENCES, 0);
        setFavorites = favorites.getStringSet(ITEM_CAT_MOVIES, new HashSet<String>());

        favIconImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<Integer, String> tags = (HashMap<Integer, String>) view.getTag();

                String combinedString = tags.get(1);

                if ((Boolean.valueOf(tags.get(0)))) {

                    //unfavoriting the item
                    ((ImageView) view).setImageResource(android.R.drawable.star_off);
                    setFavorites.remove(String.valueOf(combinedString));
                    tags.put(0, "false");


                } else {

                    //favoriting the item
                    ((ImageView) view).setImageResource(android.R.drawable.star_on);
                    setFavorites.add(String.valueOf(combinedString));
                    tags.put(0, "true");
                }
                view.setTag(tags);
                SharedPreferences.Editor editor = favorites.edit();
                editor.putStringSet("movies", setFavorites);
                editor.commit();

            }
        });


        return view;

    }
}
