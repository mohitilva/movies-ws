package com.mohitilva.moviesfun.view;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


class ServiceResponseAsyncTask extends AsyncTask<String, Void, String> {

    private  String TAG = getClass().getName();
    OkHttpClient mClient;

    public ServiceResponseAsyncTask(OkHttpClient client) {
        super();
        mClient = client;
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        String responseString = null;

        try {
            response = mClient.newCall(request).execute();
            responseString = response.body().string();

        } catch (Exception e) {
            return null;
        }

        return responseString;
    }

}
