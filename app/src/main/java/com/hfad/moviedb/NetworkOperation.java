package com.hfad.moviedb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by mtilva on 2/23/16.
 */
class NetworkOperation extends AsyncTask<String, Void, Void> {


    private MainActivity mainActivity;

    public NetworkOperation(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(String... params) {

        String url = params[0];
        mainActivity.moviesArrayList = (ArrayList) mainActivity.getList(url);
        mainActivity.adapter = new MoviesDataAdapter(mainActivity, mainActivity.moviesArrayList);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mainActivity.movieListView.setAdapter(mainActivity.adapter);

    }


}
