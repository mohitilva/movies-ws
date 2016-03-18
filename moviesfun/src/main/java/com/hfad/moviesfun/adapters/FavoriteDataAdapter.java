package com.hfad.moviesfun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.moviesfun.model.WSMetaData;
import com.hfad.moviesfun.utilities.FavoriteManager;
import com.hfad.moviesfun.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FavoriteDataAdapter extends BaseAdapter{

    private  final String TAG = getClass().getName();
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

            String[] components = favStrList.get(i).split(FavoriteManager.getFavoriteDelimiter());
            listFavoriteIds.add(components[0]);
            listTitles.add(components[1]);
            listIconUrls.add(components[2]);

        }
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
        favIconImgView.setImageResource(R.drawable.ic_star_18pt_3x);


        titleView = (TextView)view.findViewById(R.id.fav_movie_title_textview);



        // WSMetaData.getImageUrl(moviesArrayList.get(position).posterPath, WSMetaData.posterSizes.w342.toString());
        String fullIconUrl = WSMetaData.getImageUrl(listIconUrls.get(position), WSMetaData.posterSizes.w185.toString());

        Picasso.with(mContext)
                .load(fullIconUrl)
                .into(posterImgView);

        titleView.setText(listTitles.get(position));

        view.setTag(listCombined.get(position));
        posterImgView.setTag(listCombined.get(position));
        titleView.setTag(listCombined.get(position));


        favIconImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<Integer, String> tags = (HashMap<Integer, String>) view.getTag();
                FavoriteManager favoriteManager = new FavoriteManager(mContext);


                String combinedString = tags.get(1);

                if ((Boolean.valueOf(tags.get(0)))) {

                    //unfavoriting the item
                    ((ImageView) view).setImageResource(FavoriteManager.getUnfavoriteIconId());
                    favoriteManager.removeFromFavorites(combinedString);
                    tags.put(0, "false");


                } else {

                    //favoriting the item
                    ((ImageView) view).setImageResource(FavoriteManager.getFavoriteIcon());
                    favoriteManager.addToFavorites(combinedString);
                    tags.put(0, "true");
                }
                view.setTag(tags);

            }
        });

        return view;

    }
}
