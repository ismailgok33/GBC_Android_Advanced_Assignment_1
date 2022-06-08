package com.example.a1_ismail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.a1_ismail.adapters.PeopleAdapter;
import com.example.a1_ismail.databinding.ActivityMainBinding;
import com.example.a1_ismail.models.People;
import com.example.a1_ismail.models.Movie;
import com.example.a1_ismail.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, OnCharacterClickListener {

    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    // Spinner fields
    private ArrayList<String> movieTitleArrayList;
    private ArrayAdapter<String> spinnerAdapter;

    // Recycler view fields
    private ArrayList<People> peopleArrayList;
    private PeopleAdapter peopleAdapter;

    private ArrayList<Movie> movieArrayList;
    private MainActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        addMovieObserver();
    }

    private void initView() {

        // Init spinner
        this.movieTitleArrayList = new ArrayList<String>();
        this.movieTitleArrayList.add(0, getString(R.string.select_a_movie));
        this.spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.movieTitleArrayList);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.spnMovies.setAdapter(this.spinnerAdapter);
        this.binding.spnMovies.setOnItemSelectedListener(this);

        // Init recycler view
        this.peopleArrayList = new ArrayList<>();
        this.peopleAdapter = new PeopleAdapter(this, this.peopleArrayList, this::onCharacterItemClicked);
        this.binding.rvCharacters.setLayoutManager(new LinearLayoutManager(this));
        this.binding.rvCharacters.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(), DividerItemDecoration.VERTICAL));
        this.binding.rvCharacters.setAdapter(this.peopleAdapter);

        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    // Add an observer to Movie Arraylist from ViewModel
    private void addMovieObserver() {

        // Fetch the movies before adding the observer
        vm.fetchMovies();

        // Movie List Observer
        final Observer<ArrayList<Movie>> movieListObserver = new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                setMovies(movies);
            }
        };
        // attach the observer
        vm.getMovieArrayListContainer().observe(this, movieListObserver);

        this.spinnerAdapter.notifyDataSetChanged();
    }

    // Add an observer to People Arraylist from ViewModel
    private void addPeopleObserver() {
        if (!vm.getPeopleArrayListContainer().hasObservers()) {
            vm.getPeopleArrayListContainer().observe(this, new Observer<ArrayList<People>>() {
                @Override
                public void onChanged(ArrayList<People> peopleArrayList) {
                    setPeople(peopleArrayList);
                }
            });
        }
    }

    // Add an observer to People Stats Arraylist from ViewModel
    private void addPeopleStatsObserver() {
        if (!vm.getPeopleStatsContainer().hasObservers()) {
            vm.getPeopleStatsContainer().observe(this, new Observer<Integer[]>() {
                @Override
                public void onChanged(Integer[] peopleStats) {
                    setPeopleStats(peopleStats);
                }
            });
        }
    }

    // Set fetched movies to the UI elements
    private void setMovies(ArrayList<Movie> movies) {
        this.movieTitleArrayList.addAll(movies.stream().map(Movie::getTitle).collect(Collectors.toList()));
        this.movieArrayList = new ArrayList<>();
        this.movieArrayList.addAll(movies);
        this.spinnerAdapter.notifyDataSetChanged();
    }

    // Set fetched people (characters) to the UI elements
    private void setPeople(ArrayList<People> peopleArrayList) {
        this.peopleArrayList.clear();
        this.peopleArrayList.addAll(peopleArrayList);
        this.peopleAdapter.notifyDataSetChanged();
        Log.d(TAG, "setPeople: called..");
    }

    // Set people (characters) stats to the UI elements
    private void setPeopleStats(Integer[] peopleStats) {
        this.binding.tvCacheInfo.setText(getText(R.string.characters_from_cache) + ": " + peopleStats[0]);
        this.binding.tvApiInfo.setText(getText(R.string.characters_from_api) + ": " + peopleStats[1]);
    }

    @Override
    public void onCharacterItemClicked(People people) {
        Toast.makeText(this, people.getName() + " appears in " + people.getFilms().size() + " movie", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        this.peopleArrayList.clear();

        // If user selected "Select a movie" clear the stats
        if (i == 0) {
            this.binding.tvCacheInfo.setText(getText(R.string.characters_from_cache) + ": 0");
            this.binding.tvApiInfo.setText(getText(R.string.characters_from_api) + ": 0");
        }
        else {
            // Split the ID from People url and send it to the ViewModel to fetch the People (Characters)
            int moviePosition = i - 1;
            Movie movie = this.movieArrayList.get(moviePosition);
            ArrayList<String> peopleIDList = (ArrayList<String>) movie.getCharacterIDList()
                    .stream()
                    .map(url -> url.substring(url.substring(0, url.length() - 2).lastIndexOf("/") + 1, url.length() - 1))
                    .collect(Collectors.toList());
            vm.fetchPeople(peopleIDList);

            // add People Observer and Stats Observer when a movie is selected
            addPeopleObserver();
            addPeopleStatsObserver();

        }

        // display the changes on People (Character) Adapter (both People Stats and the Recycler view)
        this.peopleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}