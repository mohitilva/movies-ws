package com.hfad.moviedb.async;

import com.hfad.moviedb.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by mtilva on 2/23/16.
 */
public class LoadMore extends NetworkOperation {

    private MainActivity mainActivity;

    public LoadMore(MainActivity mainActivity) {

        super(mainActivity);
        this.mainActivity = mainActivity;

    }

    @Override
    protected Void doInBackground(String... params) {

        String url = params[0];
        mainActivity.extendedlist = (ArrayList) getList(url);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {

        mainActivity.adapter.addItems(mainActivity.extendedlist);
        mainActivity.adapter.notifyDataSetChanged();
        //mainActivity.loadMoreTaskListener.loadMoreTaskComplete();

    }
}
