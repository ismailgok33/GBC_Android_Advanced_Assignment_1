package com.example.a1_ismail.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class People {
    private @SerializedName("name")
    String name;

    private @SerializedName("species")
    ArrayList<String> species;

    private @SerializedName("films")
    ArrayList<String> films;

    public String getName() {
        return name;
    }

    public ArrayList<String> getSpecies() {
        return species;
    }

    public ArrayList<String> getFilms() {
        return films;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", species=" + species +
                ", films=" + films +
                '}';
    }
}
