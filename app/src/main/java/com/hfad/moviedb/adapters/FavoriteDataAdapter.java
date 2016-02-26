package com.hfad.moviedb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.moviedb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FavoriteDataAdapter extends BaseAdapter{
    
    private ArrayList<String> listFavoriteIds;
    private ArrayList<String> listTitles;
    private ArrayList<String> listIconUrls;
    private ArrayList<String> listCombined;
    private Context mContext;
    private LayoutInflater inflater;
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

        System.out.println(listFavoriteIds);
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
        ImageView iconView;
        ImageView favView;
        TextView titleView;

        if(convertView==null) {
            view = inflater.inflate(R.layout.favorite_list_item,null);

        }else{
            view =  convertView;
        }

        iconView = (ImageView) view.findViewById(R.id.fav_movie_icon_ImageView);

        favView = (ImageView)view.findViewById(R.id.fav_icon);


        HashMap<Integer, String> favIconTag = new HashMap<>();
        favIconTag.put(0, "true");
        favIconTag.put(1, listCombined.get(position));

        favView.setTag(favIconTag);
        favView.setImageResource(android.R.drawable.star_on);


        titleView = (TextView)view.findViewById(R.id.fav_movie_title_textview);

        String fullIconUrl = mContext.getResources().getString(R.string.poster_prefix_path) + "/"
                + mContext.getResources().getString(R.string.image_size_w300)
                + listIconUrls.get(position) + "?"
                + mContext.getResources().getString(R.string.api_key_movies_db);

        Picasso.with(mContext)
                .load(fullIconUrl)
                .into(iconView);

        titleView.setText(listTitles.get(position));

        iconView.setTag(listCombined.get(position));
        titleView.setTag(listCombined.get(position));

        System.out.println("Tags set:" + listCombined.get(position));
        return view;

    }
}
