package com.example.a1_ismail.network;

import com.example.a1_ismail.models.MovieContainer;
import com.example.a1_ismail.models.People;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API {

    String BASE_URL = "https://swapi.dev/api/";

    // https://swapi.dev/api/films
    @GET("./films")
    Call<MovieContainer> fetchMovies();

    // https://swapi.dev/api/films
    @GET("people/{id}")
    Call<People> fetchCharacters(@Path("id") String id);
}
