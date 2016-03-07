package com.hfad.moviesfun;

public interface OnListItemClickCallback{
    void onListItemClick(long id, String backDropPath, String posterPath, MainActivity.fragmentTags addToBackStack);
    //  void onLoadMoreClick();
}
