package com.mayarafelix.icemoviesapp.model;

import android.graphics.drawable.Drawable;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by mlcf on 2018-01-24.
 */

public class Movie implements Serializable
{
    private String imdbId;
    private String title;
    private String director;
    private String urlImage;
    private String released;
    private String plot;
    private String actors;
    private String country;
    private String genre;
    private transient Drawable image;
    private int year;

    public Movie (JSONObject json)
    {
        this.imdbId   = json.optString("imdbID");
        this.title    = json.optString("Title");
        this.director = json.optString("Director");
        this.urlImage = json.optString("Poster");
        this.released = json.optString("Released");
        this.plot     = json.optString("Plot");
        this.actors   = json.optString("Actors");
        this.country  = json.optString("Country");
        this.genre    = json.optString("Genre");
        this.year     = json.optInt("Year");
        this.image    = null;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return this.year + " - " + this.title;
    }
}
