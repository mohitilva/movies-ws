package com.hfad.moviesfun.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.moviesfun.R;
import com.hfad.moviesfun.utilities.Utilities;

import java.util.ArrayList;


public class DrawerAdapter extends BaseAdapter{


    Context mContext;
    Utilities utilities;
    //static ArrayList<DrawerItem> drawerItems
    public DrawerAdapter(Context c) {
        mContext = c;
        utilities = new Utilities(mContext);

    }

    public static ArrayList<DrawerItem> drawerItems = new ArrayList<>();
    {
        drawerItems.add(new DrawerItem("MoviesFun",R.drawable.icon));
        drawerItems.add(new DrawerItem("Home",R.drawable.ic_home));
        drawerItems.add(new DrawerItem("Favorites",R.drawable.ic_star));

    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view;
        ImageView icon;
        TextView textView;

        if(position==0){
            view = View.inflate(mContext, R.layout.nav_drawer_header,null);
        }else {
            view = View.inflate(mContext,R.layout.nav_drawer_item,null);

        }

        icon = (ImageView) view.findViewById(R.id.drawer_icon);
        icon.setImageResource(drawerItems.get(position).image);
        textView = (TextView) view.findViewById(R.id.drawer_item_text);

        textView.setText(drawerItems.get(position).text);
        return view;


    }
    public class DrawerItem{
        String text;
        int image;

        DrawerItem(String t, int i){
            text = t;
            image = i;
        }

    }
}
