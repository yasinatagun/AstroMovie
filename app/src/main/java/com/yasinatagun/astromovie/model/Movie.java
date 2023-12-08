package com.yasinatagun.astromovie.model;

import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
    public int id;
    public String title;
    public Number vote_average;
    public ArrayList<Genre> genres;
    public ArrayList<Cast> casts;
    public int runtime;
    public String poster_path;
    public Video videoId;
    public String overview;
    public String original_language;

}
