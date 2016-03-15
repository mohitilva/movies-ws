package com.hfad.moviesfun;


import java.util.ArrayList;

public class MySingleton {

    private static MySingleton instance = null;
    private  ArrayList<MovieDataModel> networkResponse = null;
    private boolean loadMore = false;
    void saveData(ArrayList<MovieDataModel> data) {
        networkResponse = data;
    }

    public static MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }

        return instance;
    }

    public ArrayList<MovieDataModel> getData() {
        return networkResponse;
    }

    public void setLoadMore(boolean loadMore){
        this.loadMore = loadMore;
    }

    public boolean getLoadMore(){
        return this.loadMore;
    }
}

