package com.hfad.moviesfun;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

    private String[] titles;
    private ListView drawerlist;
    private String TAG = getClass().getName();
    Context mContext;


    Set<String> setFavorites;

    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    java.util.ArrayDeque<fragmentTags> backStack = new ArrayDeque();

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

        titles = DummyContent.STRING_ITEMS;
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
            Fragment fragment = new MoviesRecentFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, fragment, fragmentTags.MAIN.name());
         //   backStack.push(fragmentTags.MAIN);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

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
    private void selectItem(int position){
        Fragment fragment;

        Log.d(TAG, "backStack when entering selectItem()="+backStack);
        //mDrawerToggle.syncState();
        //Close the drawer
        mDrawerLayout.closeDrawer(drawerlist);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (position){
            case 0:
                Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
                return;
            case 1:

                /*if(backStack.size()==0) {
                    mDrawerLayout.closeDrawer(drawerlist);

                    return;
                }*/
                if(currentFragment == fragmentTags.MAIN){
            //        mDrawerLayout.closeDrawer(drawerlist);
                    return;
                }
                backStack.clear();

                fragment = new MoviesRecentFragment();
                ft.replace(R.id.content_frame, fragment);
                break;
            case 2:

                if(currentFragment == fragmentTags.FAVORITES){
            //        mDrawerLayout.closeDrawer(drawerlist);
                    return;
                }

                fragment = new FavoriteMovieFragment();
                ft.replace(R.id.content_frame, fragment, fragmentTags.FAVORITES.name());
                if(backStack.peek()!=fragmentTags.MAIN)
                    backStack.push(fragmentTags.MAIN);
                Log.d(TAG, backStack.toString());
                break;


        }


        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }



    /* Implementing the callback for movie clicked */
    @Override
    public void onListItemClick(long id, String backDropPath, String posterPath, fragmentTags addToBackStack) {

        Fragment movieDetailsFragment = MovieDetailsFragment.newInstance(String.valueOf(id), backDropPath, posterPath);
        if(addToBackStack != null)
            backStack.push(addToBackStack);
        Log.d(TAG, "backstack=" + backStack.toString());

       getFragmentManager().beginTransaction()
        .replace(R.id.content_frame, movieDetailsFragment)
        .addToBackStack(null)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .commit();

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);
            setActionBarTitle(position);
        }
    }
    private void setActionBarTitle(int position)
    {
      //  String title = (String) getActionBar().getTitle();
        String title = (String) getSupportActionBar().getTitle();
        if(position==0)
            title = "RECENT MOVIES";
        else if(position==1)
            title = "MY FAVORITES";

     //   getActionBar().setTitle(title);
        getSupportActionBar().setTitle(title);

    }

    @Override
    public void onBackPressed() {


        if(backStack.peekLast()==null)
            super.onBackPressed();
        else
        {
            fragmentTags tag = backStack.pop();

           if(tag.equals(fragmentTags.MAIN)){

               selectItem(1);
           } else if(tag.equals(fragmentTags.FAVORITES)){
               selectItem(2);
           } else if(tag.equals(fragmentTags.DETAILS)){
               selectItem(3);
           } else {
               super.onBackPressed();
           }
        }

    }


}
