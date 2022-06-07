package com.example.a1_ismail.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieContainer {
    private @SerializedName("results")
    ArrayList<Movie> movieList;

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    @Override
    public String toString() {
        return "MovieContainer{" +
                "movieList=" + movieList +
                '}';
    }
}
