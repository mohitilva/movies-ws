package com.hfad.moviesfun;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mtilva on 3/5/16.
 */
public class DrawerAdapter extends BaseAdapter{


    Context mContext;
    Utilities utilities;
    ArrayList<Utilities.DrawerItem> drawerItems;
    public DrawerAdapter(Context c) {
        mContext = c;
        utilities = new Utilities(mContext);
        drawerItems = utilities.drawerItems;
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
            view = View.inflate(mContext,R.layout.nav_drawer_header,null);
        }else {
            view = View.inflate(mContext,R.layout.nav_drawer_item,null);

        }

        icon = (ImageView) view.findViewById(R.id.drawer_icon);
        icon.setImageResource(drawerItems.get(position).image);
        textView = (TextView) view.findViewById(R.id.drawer_item_text);

        textView.setText(drawerItems.get(position).text);
        return view;


    }
}
