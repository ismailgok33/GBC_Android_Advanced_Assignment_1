package com.example.a1_ismail.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class People {
    private @SerializedName("name")
    String name;

    private @SerializedName("species")
    ArrayList<String> species;

    public String getName() {
        return name;
    }

    public ArrayList<String> getSpecies() {
        return species;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", species=" + species +
                '}';
    }
}
