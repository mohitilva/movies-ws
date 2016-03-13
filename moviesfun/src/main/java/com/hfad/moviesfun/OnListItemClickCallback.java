package com.hfad.moviesfun;

import java.util.ArrayList;

public interface OnListItemClickCallback{
    void onListItemClick(long id, String backDropPath, String posterPath, MainActivity.fragmentTags addToBackStack);
    void updateActivityUI(String tag);
}
