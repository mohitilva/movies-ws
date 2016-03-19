package com.mohitilva.moviesfun.view;


import com.mohitilva.moviesfun.model.MovieDataModel;

import java.util.ArrayList;


//Used to temporary store data. Data is cleared every time user exits the app.
public class MoviesListSingleton {


    private static MoviesListSingleton instance = null;
    private  ArrayList<MovieDataModel> networkResponse = null;
    private boolean loadMore = false;
    public void saveData(ArrayList<MovieDataModel> data) {
        networkResponse = data;
    }

    public static MoviesListSingleton getInstance() {
        if (instance == null) {
            instance = new MoviesListSingleton();
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

    public void clearData(){
        networkResponse = null;
        loadMore = false;

    }
}

