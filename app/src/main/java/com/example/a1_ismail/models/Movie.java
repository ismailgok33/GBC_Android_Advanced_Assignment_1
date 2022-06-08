package com.example.a1_ismail.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie {

    private @SerializedName("title")
    String title;

    private @SerializedName("characters")
    ArrayList<String> characterIDList;

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getCharacterIDList() {
        return characterIDList;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", characterIDList=" + characterIDList +
                '}';
    }
}
