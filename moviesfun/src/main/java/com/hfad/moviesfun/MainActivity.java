package com.hfad.moviesfun;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity
                        implements OnListItemClickCallback

{

    private ListView drawerlist;
    private String TAG = getClass().getName();
    private Context mContext;
    private Fragment fragment;
    private FragmentManager fragmentManager = getFragmentManager();
    private String currentFragment;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private int drawerItemSelected;



    public   enum  fragmentTags {
        MAIN,
        FAVORITES,
        DETAILS
    }

    @Override
    public void updateActivityUI(String fragmentTag) {

        setActionBarTitle(fragmentTag);
        currentFragment = fragmentTag;
        Log.d(TAG,"Current fragment set to " + currentFragment);

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
            fragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment, fragmentTags.MAIN.name())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
            currentFragment = fragmentTags.MAIN.name();

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


        super.onDestroy();
    }

    //when item is clicked on the navigation bar


    private void selectItem(int position) {

        drawerItemSelected = position;

        mDrawerLayout.closeDrawer(drawerlist);
    }




    /* Implementing the callback for movie clicked. Called from two places, Main Fragment and Favorites Fragment */
    @Override
    public void onListItemClick(long id, String backDropPath, String posterPath, fragmentTags callingFragment) {

        fragment = MovieDetailsFragment.newInstance(String.valueOf(id), backDropPath, posterPath);

       fragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment, fragmentTags.DETAILS.name())
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

    public void setActionBarTitle(String currentFragment){


        switch (currentFragment){
            case "MAIN":
                setActionBarTitle(1);
                break;
            case "FAVORITES":
                setActionBarTitle(2);
                break;
            case "DETAILS":
                Log.d(TAG, "Tag fouund was DETAILS");
                break;
        }

    }

    public void setActionBarTitle(int position)
    {
      //  String title = (String) getActionBar().getTitle();
        String title = "ABC";

        switch (position){

            case 1:
                title = "MoviesFun";
                break;
            case 2:
                title = "MY FAVORITES";

        }
        getSupportActionBar().setTitle(title);
    }



    @Override
    public void onBackPressed() {


        if(mDrawerLayout.isDrawerOpen(drawerlist)) {
            mDrawerLayout.closeDrawer(drawerlist);
            return;
        }

        if (fragmentManager.getBackStackEntryCount() > 1) {


            fragmentManager.popBackStack();

        } else {
            super.onBackPressed();
        }
    }

    private void switchFragment(){

        Log.d(TAG, "Currentfragment in switchFragment="+currentFragment);

        FragmentTransaction ft;
        ft = fragmentManager.beginTransaction();

        switch (drawerItemSelected) {
            case 0:
                return;
            case 1:

                if (currentFragment == fragmentTags.MAIN.name()) return;

                fragment = new MoviesRecentFragment();
                ft.replace(R.id.content_frame, fragment, fragmentTags.MAIN.name());
                ft.addToBackStack(null);
                break;

            case 2:
                if (currentFragment == fragmentTags.FAVORITES.name()) return;

                fragment = new FavoriteMovieFragment();
                ft.replace(R.id.content_frame, fragment, fragmentTags.FAVORITES.name());
                ft.addToBackStack(null);
                break;
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        setActionBarTitle(drawerItemSelected);
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

            switchFragment();

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
