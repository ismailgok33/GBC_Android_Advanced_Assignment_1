package com.example.a1_ismail.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a1_ismail.models.People;
import com.example.a1_ismail.models.Movie;
import com.example.a1_ismail.models.MovieContainer;
import com.example.a1_ismail.network.RetrofitClient;
import com.example.a1_ismail.repository.MovieRepository;
import com.example.a1_ismail.repository.PeopleRepository;

import java.security.Policy;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "MainActivityViewModel";

    private final MovieRepository movieRepository = new MovieRepository();
    private final PeopleRepository peopleRepository = new PeopleRepository();

    private MutableLiveData<ArrayList<Movie>> movieArrayListContainer;
    private MutableLiveData<ArrayList<People>> peopleArrayListContainer;
    private MutableLiveData<Integer[]> peopleStatsContainer; // 0 = from cache, 1 = from API

    public MainActivityViewModel() { }

    public void fetchMovies() {
        this.movieRepository.fetchMovies();
        this.movieArrayListContainer = this.movieRepository.getMovieArrayListContainer();
    }

    public void fetchPeople(ArrayList<String> idList) {
        this.peopleRepository.fetchPeople(idList);
        this.peopleArrayListContainer = this.peopleRepository.getPeopleArrayListContainer();
        this.peopleStatsContainer = this.peopleRepository.getPeopleStatsContainer();
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
