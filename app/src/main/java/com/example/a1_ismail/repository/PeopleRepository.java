package com.example.a1_ismail.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.a1_ismail.models.People;
import com.example.a1_ismail.network.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleRepository {

    private static final String TAG = "PeopleRepository";

    private ArrayList<People> cachedPeopleList = new ArrayList<>();

    private MutableLiveData<ArrayList<People>> peopleArrayListContainer = new MutableLiveData<>();
    private MutableLiveData<Integer[]> peopleStatsContainer = new MutableLiveData<>(new Integer[]{0, 0}); // 0 = from cache, 1 = from API

    public void fetchPeople(ArrayList<String> idList) {

        ArrayList<People> peopleArrayList = new ArrayList<>();
        Integer[] peopleStats = new Integer[]{0, 0}; // 0 = from cache, 1 = from API

        // Check if People (Character) ID is cached before.
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

    }

    public ArrayList<People> getCachedPeopleList() {
        return cachedPeopleList;
    }

    public MutableLiveData<ArrayList<People>> getPeopleArrayListContainer() {
        return peopleArrayListContainer;
    }

    public MutableLiveData<Integer[]> getPeopleStatsContainer() {
        return peopleStatsContainer;
    }
}
