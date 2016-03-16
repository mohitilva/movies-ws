package com.hfad.moviesfun.model;

import java.util.ArrayList;


public class MovieDataModel {

    public Long id;
    public String title;
    public String posterPath;
    public String backdropPath;
    public String releaseDate;
    public double voteAvg;
    public int[] genres;
    public ArrayList<String> actors;
    String overview;

    public MovieDataModel(Long l, String overview, String title, String posterPath, String backdrop) {
        this.overview = overview;
        this.id = l;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdrop;
    }


}
