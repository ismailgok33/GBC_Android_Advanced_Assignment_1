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
    private ArrayList<Movie> movieArrayList = new ArrayList<>();

    public void fetchMovies() {

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
                            movieArrayList.addAll(movieContainer.getMovieList());
                            movieArrayListContainer.postValue(movieArrayList);
                        }
                    }
                    else {
                        Log.e(TAG, "onResponse: Movie call response returned error code = " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<MovieContainer> call, Throwable t) {
                    Log.e(TAG, "onFailure: movie call failed: " + t.getMessage());
//                    call.cancel();
                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, "fetchMovies: Cannot fetch the movie data" + e.getLocalizedMessage());
        }

    }

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public MutableLiveData<ArrayList<Movie>> getMovieArrayListContainer() {
        return movieArrayListContainer;
    }
}
