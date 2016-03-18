package com.hfad.moviesfun.utilities;

import android.content.Context;
import android.widget.Toast;

import com.hfad.moviesfun.R;
import com.hfad.moviesfun.model.WSMetaData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Utilities {


    private Context mContext;

    private  String TAG = getClass().getName();

    public static final long BILLION = 1000000000;
    public static final long MILLION = 1000000;
    public static final long HUNDRED_THOUSAND = 100000;
    private static final int THOUSAND = 1000;

    public Utilities(Context context){
        mContext = context;
    }

    public  String getStringFromStringArray(String[] genreNames, int limit){

        String genreText = "";

        if(genreNames==null || genreNames.length==0){
            return mContext.getString(R.string.not_available);
        }

        for(int i=0;i<genreNames.length;i++){
            genreText += ", " + genreNames[i];
            if(i==limit-1) break;
        }

        return trimText(genreText);
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


    public   String getRevenueFromLong(long revenue){
        String revenueStr;
        float r;
        if(revenue >= BILLION){
             r = (float) revenue / BILLION;
            String s = String.format("%.2f",r);
            revenueStr = "$" + s + "B";
        }else if(revenue >= MILLION){
             r = (float) revenue / MILLION;
            String s = String.format("%.2f",r);
            revenueStr = "$" + s + "M";
        }else if (revenue>=HUNDRED_THOUSAND){
             r = (float) revenue / THOUSAND;
            int i = (int) r;
            revenueStr = "$" + String.valueOf(i) + "K";
        }else{
            //Do not display if less than 100K
            revenueStr = mContext.getString(R.string.not_available) ;
        }
        return revenueStr;
    }

    public static void showGeneralErrorToast(Context c){
        Toast.makeText(c, c.getString(R.string.general_error_message), Toast.LENGTH_SHORT).show();
    }


}
