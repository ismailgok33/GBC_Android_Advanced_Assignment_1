package com.example.a1_ismail.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.a1_ismail.models.Movie;
import com.example.a1_ismail.models.MovieContainer;
import com.example.a1_ismail.network.RetrofitClient;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private MutableLiveData<ArrayList<Movie>> movieArrayListContainer = new MutableLiveData<>();

    public void fetchMovies() {

        ArrayList<Movie> movieArrayList = new ArrayList<>();

        Call<MovieContainer> movieContainerCall = RetrofitClient.getInstance().getApi().fetchMovies();

        try {
            movieContainerCall.enqueue(new Callback<MovieContainer>() {
                @Override
                public void onResponse(Call<MovieContainer> call, Response<MovieContainer> response) {
                    if (response.code() == 200) {
                        MovieContainer movieContainer = response.body();
                        
                        if (movieContainer.getMovieList().isEmpty()) {
                            Log.e(TAG, "onResponse: No movies are fetched");
                        }
                        else {
                            for (Movie movie : movieContainer.getMovieList()) {
                                movieArrayList.add(movie);
                            }
                            movieArrayListContainer.setValue(movieArrayList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieContainer> call, Throwable t) {

                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, "fetchMovies: Cannot fetch the movie data" + e.getLocalizedMessage());
        }

    }

    public MutableLiveData<ArrayList<Movie>> getMovieArrayListContainer() {
        return movieArrayListContainer;
    }
}
