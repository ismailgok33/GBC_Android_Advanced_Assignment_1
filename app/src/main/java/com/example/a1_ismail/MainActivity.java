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
        initViewModel();
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
    }

    private void initViewModel() {
        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // observer
        final Observer<ArrayList<Movie>> movieListObserver = new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                getMovies(movies);
            }
        };
        // attach the observer
        vm.getMovieArrayListContainer().observe(this, movieListObserver);

        // observer
        final Observer<ArrayList<People>> peopleListObserver = new Observer<ArrayList<People>>() {
            @Override
            public void onChanged(ArrayList<People> peopleArrayList) {
                setPeople(peopleArrayList);
            }
        };
        // attach the observer
        vm.getPeopleArrayListContainer().observe(this, peopleListObserver);

        // observer
        final Observer<Integer[]> peopleStatObserver = new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] peopleStats) {
                setPeopleStats(peopleStats);
            }
        };
        // attach the observer
        vm.getPeopleStatsContainer().observe(this, peopleStatObserver);

        vm.fetchMovies();
        this.spinnerAdapter.notifyDataSetChanged();
    }

    private void getMovies(ArrayList<Movie> movies) {
        // TODO: Call from ViewModel and load this.movieList via Observers

        this.movieTitleArrayList.addAll(movies.stream().map(Movie::getTitle).collect(Collectors.toList()));
        this.movieArrayList = new ArrayList<>();
        this.movieArrayList.addAll(movies);
//        this.movieList.add(0, getString(R.string.select_a_movie));

        this.spinnerAdapter.notifyDataSetChanged();
    }

    private void setPeople(ArrayList<People> peopleArrayList) {
        this.peopleArrayList.clear();
        this.peopleArrayList.addAll(peopleArrayList);
        this.peopleAdapter.notifyDataSetChanged();
        Log.d(TAG, "setPeople: called..");
    }

    private void setPeopleStats(Integer[] peopleStats) {
        this.binding.tvCacheInfo.setText(getText(R.string.characters_from_cache) + ": " + peopleStats[0]);
        this.binding.tvApiInfo.setText(getText(R.string.characters_from_api) + ": " + peopleStats[1]);
    }

    @Override
    public void onCharacterItemClicked(People people) {
        Log.d(TAG, "onCharacterItemClicked: Character is clicked " + people.toString());

        // TODO: Call from ViewModel. VievModel should track how many times the character appears in the movies
        Toast.makeText(this, people.getName() + " appears in " + people.getFilms().size() + " movie", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemClick: Selected movie : " + this.movieTitleArrayList.get(i));

        this.peopleArrayList.clear();
        if (i == 0) {
            // TODO: remove recycler view
        }
        else {
            // TODO: Load recycler view with the specific data
            int moviePosition = i - 1;
            Movie movie = this.movieArrayList.get(moviePosition);
            ArrayList<String> peopleIDList = (ArrayList<String>) movie.getCharacterIDList().stream().map(url -> url.substring(url.substring(0, url.length() - 2).lastIndexOf("/") + 1, url.length() - 1)).collect(Collectors.toList());
            for (String item : peopleIDList) {
                Log.d(TAG, "onItemSelected: peopleIDList item = " + item);
            }
            vm.fetchPeople(peopleIDList);
        }

        this.peopleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}