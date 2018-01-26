package com.mayarafelix.icemoviesapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayarafelix.icemoviesapp.R;

import java.util.ArrayList;


public class MovieAdapter extends ArrayAdapter<Movie>
{
    private Context context;
    private ArrayList<Movie> movies = null;
    private View view;
    private int position;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieDirector;
    private ImageView movieImage;

    public MovieAdapter(@NonNull Context context, ArrayList<Movie> movies)
    {
        super(context, 0, movies);
        this.context  = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        view = convertView;
        this.position = position;

        //-------------------------------------------
        // Get Current Movie
        //-------------------------------------------

        Movie movie = movies.get(position);

        //-------------------------------------------
        // Check Context
        //-------------------------------------------

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_list_item, parent, false);
        }

        //-------------------------------------------
        // View Elements
        //-------------------------------------------

        movieTitle     = (TextView) convertView.findViewById(R.id.movieTitle);
        movieYear      = (TextView) convertView.findViewById(R.id.movieYear);
        movieDirector  = (TextView) convertView.findViewById(R.id.movieDirector);
        movieImage     = (ImageView) convertView.findViewById(R.id.movieThumbImage);

        //-------------------------------------------
        // Update Value
        //-------------------------------------------

        movieTitle.setText(movie.getTitle());
        movieYear.setText(String.valueOf(movie.getYear()));
        movieDirector.setText(movie.getDirector());

        if (movie.getImage() != null)
        {
            movieImage.setImageDrawable(movie.getImage());
        }

        //-------------------------------------------
        // Return
        //-------------------------------------------

        return convertView;
    }
}
