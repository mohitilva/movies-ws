package com.hfad.moviedb;

/**
 * Created by mtilva on 2/13/16.
 */
public class MovieDataObject {

    String overview;
    Long id;
    String title;
    String posterPath;
    String backdropPath;
    String releaseDate;
    int popularity;

    public  MovieDataObject(Long l, String overview, String title, String posterPath, String backdrop){
        this.overview = overview;
        this.id = l;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdrop;
    }



}
