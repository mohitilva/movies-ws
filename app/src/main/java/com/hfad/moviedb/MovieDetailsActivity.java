package com.hfad.moviedb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    ImageView img;
    TextView titleView;
    TextView overviewView;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        img = (ImageView) findViewById(R.id.moviebackdrop_details);
        titleView = (TextView) findViewById(R.id.title_details);
        overviewView = (TextView) findViewById(R.id.overview_details);

        Intent receivedIntent= getIntent();
        Bundle bundle = receivedIntent.getExtras();

        String backdropPath = bundle.getString("backdropPath");
        String overview =  bundle.getString("overview");
        String title = bundle.getString("title");
        Long id = bundle.getLong("id");
        String imageUrl = getResources().getString(R.string.poster_prefix_path) + backdropPath + "?"
                + getResources().getString(R.string.api_key_movies_db);


        Picasso.with(MovieDetailsActivity.this).load(imageUrl).into(img);
        titleView.setText(title);
        overviewView.setText(overview);


       // button = (Button)findViewById(R.id.button1);



    }

    class MoviesAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            return null;
        }

        @Override
        protected void onPostExecute(Void v){

        }







    }

}
