package com.example.a1_ismail.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a1_ismail.models.People;
import com.example.a1_ismail.models.Movie;
import com.example.a1_ismail.models.MovieContainer;
import com.example.a1_ismail.network.RetrofitClient;

import java.security.Policy;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "MainActivityViewModel";

    private ArrayList<People> cachedPeopleList = new ArrayList<>();
    
    private MutableLiveData<ArrayList<Movie>> movieArrayListContainer = new MutableLiveData<>();
    private MutableLiveData<ArrayList<People>> peopleArrayListContainer = new MutableLiveData<>();
    private MutableLiveData<Integer[]> peopleStatsContainer = new MutableLiveData<>(new Integer[]{0, 0}); // 0 = from cache, 1 = from API

    public MainActivityViewModel() { }

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

    public void fetchPeople(ArrayList<String> idList) {

        ArrayList<People> peopleArrayList = new ArrayList<>();
        Integer[] peopleStats = new Integer[]{0, 0}; // 0 = from cache, 1 = from API

        for (String id : idList) {
            People cachedPeople = cachedPeopleList.stream().filter(people -> people.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
            if (cachedPeople != null) {
                peopleStats[0]++;
                peopleArrayList.add(cachedPeople);
                peopleArrayListContainer.setValue(peopleArrayList);
                peopleStatsContainer.setValue(peopleStats);
                continue;
            }

            Call<People> peopleCall = RetrofitClient.getInstance().getApi().fetchCharacters(id);

            Log.d(TAG, "fetchPeople: id = " + id);
            try {
                peopleCall.enqueue(new Callback<People>() {
                    @Override
                    public void onResponse(Call<People> call, Response<People> response) {
                        if (response.code() == 200) {
                            People people = response.body();
                            people.setId(id);

                            if (people == null) {
                                Log.e(TAG, "onResponse: People response is empty");
                            }
                            else {
                                peopleArrayList.add(people);
                                cachedPeopleList.add(people); // add to cache
                                peopleStats[1]++;
                                peopleArrayListContainer.setValue(peopleArrayList);
                                peopleStatsContainer.setValue(peopleStats);
                            }
                        }
                        else {
                            Log.e(TAG, "onResponse: People call response returned error code = " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<People> call, Throwable t) {
                        Log.e(TAG, "onFailure: Response returned an error " + t.getMessage());
                    }
                });
            }
            catch (Exception e) {
                Log.e(TAG, "fetchPeople: Cannot fetch the people data" + e.getLocalizedMessage());
            }
        }

//        if (!peopleArrayList.isEmpty()) {
//            peopleArrayListContainer.setValue(peopleArrayList);
//        }
//        else {
//            Log.e(TAG, "fetchPeople: there are no people to add to the container");
//        }

    }

    public MutableLiveData<ArrayList<Movie>> getMovieArrayListContainer() {
        return movieArrayListContainer;
    }

    public MutableLiveData<ArrayList<People>> getPeopleArrayListContainer() {
        return peopleArrayListContainer;
    }

    public MutableLiveData<Integer[]> getPeopleStatsContainer() {
        return peopleStatsContainer;
    }
}
