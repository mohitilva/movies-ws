package com.hfad.moviesfun;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


class ServiceResponseAsyncTask extends AsyncTask<String, Void, String> {

    private  static String TAG="ServiceResponseAsyncTask";
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
        Log.d(TAG, "Trying to get response for:" + url);
        try {
            response = mClient.newCall(request).execute();
            responseString = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

}
