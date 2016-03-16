package com.hfad.moviesfun.utilities;

import android.content.Context;

import com.hfad.moviesfun.R;
import com.hfad.moviesfun.model.WSMetaData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Utilities {

    private Context mContext;
    private static String apiKey;
    private  String TAG = getClass().getName();

    public Utilities(Context context){
        mContext = context;
        apiKey = mContext.getResources().getString(R.string.api_key_movies_db);
    }

    public  String getImageUrl(String imageRelPath, String size){
        String prefix =  mContext.getResources().getString(R.string.poster_prefix_path);

        return prefix + "/" + size + imageRelPath + "?" + apiKey;
    }

    public String getResourceUrl(String resourceRelPath){
        String prefix =  mContext.getResources().getString(R.string.movie_prefix_path);

        return prefix + "/" + resourceRelPath + "?" + apiKey;
    }

    public String getRecentReleasedMoviesUrl() {
     String url = mContext.getResources().getText(R.string.popular_movies_url)
                + "&" + apiKey;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String currentDate = f.format(today);
        url = url + "&release_date.lte=" + currentDate;
        return url;
    }

    public static String[] getGenreNamesFromIds(int[] genreIds){
        if(genreIds ==null || genreIds.length==0) return null;

        String[] genreNames= new String[genreIds.length];
        for(int i=0; i<genreIds.length;i++){
            genreNames[i] = WSMetaData.genreIdMap.get(genreIds[i]);
        }

        return genreNames;
    }

    public static String trimText(String str){

        if(str==null || "".equals(str)) return "";

        str= str.trim();
        if(str.charAt(0)==',')
            str = str.substring(1);

        if(str.charAt(str.length()-1)==',')
            str = str.substring(0, str.length()-2);

        str = str.trim();
        return str;
    }

    public static String getStringFromArrayList(ArrayList<String> list, int limit) {

        String actors = "";

        if(list==null) return "";

        for(int i=0;i<list.size()-1;i++){
            actors +=  ", " + list.get(i);
            if(i==limit-1) break;
        }

        return Utilities.trimText(actors);
    }

    public String getCreditsUrl(Long id){
        return getCreditsUrl(String.valueOf(id));
    }

    public String getCreditsUrl(String id){
        return mContext.getString(R.string.movie_base_url) +id +  "/credits?" + apiKey;
    }

    public  String getRevenueFromLong(long revenue){
        String revenueStr;
        if(revenue > 1000000000){
            float r = revenue / 1000000000f;
            String s = String.format("%.2f",r);
            revenueStr = "$" + s + "B";
        }else if(revenue > 1000000){
            float r = revenue / 1000000f;
            String s = String.format("%.2f",r);
            revenueStr = "$" + s + "M";
        }else if (revenue>100000){
            float r = revenue / 1000;
            int i = (int) r;
            revenueStr = "$" + String.valueOf(i) + "K";
        }else{
            revenueStr = mContext.getString(R.string.not_available) ;
        }
        return revenueStr;
    }


}
