package com.mayarafelix.icemoviesapp.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mayarafelix.icemoviesapp.detail.DetailActivity;
import com.mayarafelix.icemoviesapp.R;
import com.mayarafelix.icemoviesapp.model.Movie;
import com.mayarafelix.icemoviesapp.model.MovieAdapter;
import com.mayarafelix.icemoviesapp.utils.Constants;
import com.mayarafelix.icemoviesapp.utils.UrlManager;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private DownloadMovies downloadMovies;
    private JSONArray moviesJson;
    private EditText searchText;
    private ImageButton searchButton;
    private ListView moviesListView;
    private ArrayList<Movie> moviesList;
    private MovieAdapter adapter;
    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------------------
        // Action Bar
        //-------------------------------------------

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_movies_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //-------------------------------------------
        // Get View Elements
        //-------------------------------------------

        searchText     = findViewById(R.id.searchText);
        searchButton   = findViewById(R.id.searchButton);
        moviesListView = findViewById(R.id.moviesListView);
        progressBar    = findViewById(R.id.progressbar);

        //-------------------------------------------
        // Movies List and Adapter
        //-------------------------------------------

        moviesList = new ArrayList<Movie>();
        adapter = new MovieAdapter(getBaseContext(), moviesList);
        moviesListView.setAdapter(adapter);

        //-------------------------------------------
        // Search Button Action
        //-------------------------------------------

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moviesList.clear();
                adapter.notifyDataSetChanged();

                String searchString = searchText.getText().toString();
                hideKeyboard();

                if (!searchString.isEmpty())
                {
                    String dataUrl = UrlManager.getMoviesByTitle(searchString);
                    downloadMovies = new DownloadMovies(MainActivity.this);
                    downloadMovies.execute(dataUrl);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please, enter a movie title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //-------------------------------------------
        // List Click >> Go to Detail
        //-------------------------------------------

        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> av, View view, int i, long l)
            {
                Movie movie = (Movie) adapter.getItem(i);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.MOVIE_TAG, movie);
                startActivity(intent);
            }
        });
    }

    public void populateArrayOfMovies(@Nullable JSONArray moviesJson)
    {
        if (moviesJson == null)
        {
            String msg = getString(R.string.movie_not_found);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
        else
        {
            this.moviesList.clear();

            try
            {
                for (int i = 0; i < moviesJson.length(); i++)
                {
                    Movie movie = new Movie(moviesJson.getJSONObject(i));
                    moviesList.add(movie);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            // Refresh listView
            adapter.notifyDataSetChanged();

            // Get Image and other information about each the movie
            for (int i = 0; i < moviesList.size(); i++)
            {
                // Get information
                String dataUrl = UrlManager.getMovieDetail(moviesList.get(i).getImdbId());
                DownloadMovieInfo downloadMovies = new DownloadMovieInfo(MainActivity.this, i);
                downloadMovies.execute(dataUrl);

                // Get Image
                String imageUrl = moviesList.get(i).getUrlImage();
                DownloadImage downloadImage = new DownloadImage(MainActivity.this, moviesList.get(i), i);
                downloadImage.execute(imageUrl);
            }
        }
    }

    public void updateMovieDetailReturn(@Nullable Movie movie, int position)
    {
        if (movie != null)
        {
            // Update model
            moviesList.get(position).setActors(movie.getActors());
            moviesList.get(position).setCountry(movie.getCountry());
            moviesList.get(position).setDirector(movie.getDirector());
            moviesList.get(position).setGenre(movie.getGenre());
            moviesList.get(position).setPlot(movie.getPlot());
            moviesList.get(position).setReleased(movie.getReleased());

            // Refresh listView
            adapter.notifyDataSetChanged();
        }
    }

    public void updateMovieImageReturn(Drawable drawable, int position)
    {
        if (drawable != null)
        {
            moviesList.get(position).setImage(drawable);
            adapter.notifyDataSetChanged();
        }
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
