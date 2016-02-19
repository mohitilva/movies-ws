package com.hfad.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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

        Intent receivedIntent= getIntent();
        Bundle bundle = receivedIntent.getExtras();

        String posterpath = bundle.getString("posterpath");
        String overview =  bundle.getString("overview");
        String title = bundle.getString("title");
        Long id = bundle.getLong("id");

        img = (ImageView) findViewById(R.id.movieposter_details);
        titleView = (TextView) findViewById(R.id.title_details);
        overviewView = (TextView) findViewById(R.id.overview_details);
        titleView.setText(title);
        overviewView.setText(overview);

        String imageUrl = getResources().getString(R.string.poster_prefix_path) + posterpath + "?"
                + getResources().getString(R.string.api_key_movies_db);

        Picasso.with(MovieDetailsActivity.this).load(imageUrl).into(img);
        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


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
