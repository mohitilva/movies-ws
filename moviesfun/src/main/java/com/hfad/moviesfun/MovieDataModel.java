package com.hfad.moviesfun;

/**
 * Created by mtilva on 2/13/16.
 */
public class MovieDataModel {

    String overview;
    public Long id;
    public String title;
    public String posterPath;
    public String backdropPath;
    public String releaseDate;
    public int popularity;

    public MovieDataModel(Long l, String overview, String title, String posterPath, String backdrop){
        this.overview = overview;
        this.id = l;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdrop;
    }



}