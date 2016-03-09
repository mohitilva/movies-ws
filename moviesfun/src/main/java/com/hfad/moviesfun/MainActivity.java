package com.hfad.moviesfun;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hfad.moviesfun.dummy.DummyContent;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity
                        implements OnListItemClickCallback

{

    private ListView drawerlist;
    private String TAG = getClass().getName();
    Context mContext;
    Fragment fragment;
    FragmentManager fm  = getFragmentManager();
    Set<String> setFavorites;

    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    ArrayDeque<fragmentTags> backStack = new ArrayDeque<>();

    public  enum fragmentTags {
        MAIN,
        FAVORITES,
        DETAILS
    }
    private fragmentTags currentFragment;


    public void setCurrentFragment(fragmentTags currentFragment) {
        this.currentFragment = currentFragment;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new MyDrawerListener());

        drawerlist = (ListView) findViewById(R.id.drawer);

        DrawerAdapter drawerAdapter = new DrawerAdapter(mContext);

        drawerlist.setAdapter(drawerAdapter);

        drawerlist.setOnItemClickListener(new DrawerItemClickListener());

        //create ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer){

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();

            }
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        if(savedInstanceState==null){

            fragment = new MoviesRecentFragment();

            fm.beginTransaction()
            .replace(R.id.content_frame, fragment, fragmentTags.MAIN.name())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();


        }

        /*   getActionBar().setDisplayHomeAsUpEnabled(true);
             getActionBar().setHomeButtonEnabled(true);
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {

        Log.d(TAG,"Before Destroy Set of favorites="+setFavorites);
        super.onDestroy();
    }

    //when item is clicked on the navigation bar

    int drawerItem;
    private void selectItem(int position) {

        drawerItem = position;

        Log.d(TAG, "backStack when entering selectItem()=" + backStack);

        mDrawerLayout.closeDrawer(drawerlist);





    }




    /* Implementing the callback for movie clicked */
    @Override
    public void onListItemClick(long id, String backDropPath, String posterPath, fragmentTags addToBackStack) {

        fragment = MovieDetailsFragment.newInstance(String.valueOf(id), backDropPath, posterPath);
        if(addToBackStack != null)
            backStack.push(addToBackStack);

        Log.d(TAG, "backstack after adding " + addToBackStack + "=" + backStack.toString());

       fm.beginTransaction()
        .replace(R.id.content_frame, fragment)
        .addToBackStack(null)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .commit();

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);

        }
    }
    private void setActionBarTitle(int position)
    {
      //  String title = (String) getActionBar().getTitle();
        String title = (String) getSupportActionBar().getTitle();

        switch (position){

            case 1:
                title = "MoviesFun";
                break;
            case 2:
                title = "MY FAVORITES";

        }



     //   getActionBar().setTitle(title);
        getSupportActionBar().setTitle(title);

    }


    @Override
    public void onBackPressed() {


        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }



    private class MyDrawerListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {


            FragmentTransaction ft;
            ft = fm.beginTransaction();
            Log.d(TAG, "In onDrawerClosed(). currentFragment = "+ currentFragment.name());
            switch (drawerItem) {
                case 0:
                    Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show();
                    return;
                case 1:


                    if (currentFragment == fragmentTags.MAIN) {
                        return;
                    }
                    backStack.clear();
                    fragment = new MoviesRecentFragment();

                    ft.replace(R.id.content_frame, fragment);
                    break;
                case 2:
                    if (currentFragment == fragmentTags.FAVORITES) {
                        return;
                    }
                    fragment = new FavoriteMovieFragment();
                    ft.replace(R.id.content_frame, fragment, fragmentTags.FAVORITES.name());
                    if (backStack.peek() != fragmentTags.MAIN)
                        backStack.push(fragmentTags.MAIN);
                    Log.d(TAG, backStack.toString());
                    break;
            }

            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            setActionBarTitle(drawerItem);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
