package com.example.android.newsappdemo;

import java.util.ArrayList;

/**
 * Created by Archita on 02-09-2017.
 */

public class Source {

    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;
    private ArrayList<String>  sortBysAvailable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<String> getSortBysAvailable() {
        return sortBysAvailable;
    }

    public void setSortBysAvailable(ArrayList<String> sortBysAvailable) {
        this.sortBysAvailable = sortBysAvailable;
    }
}
