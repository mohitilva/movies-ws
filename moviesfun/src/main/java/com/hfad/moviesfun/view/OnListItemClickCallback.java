package com.hfad.moviesfun.view;

import com.hfad.moviesfun.view.MainActivity;

public interface OnListItemClickCallback{
    void onListItemClick(long id, String backDropPath, String posterPath, MainActivity.fragmentTags addToBackStack);
    void updateActivityUI(String tag);
}
