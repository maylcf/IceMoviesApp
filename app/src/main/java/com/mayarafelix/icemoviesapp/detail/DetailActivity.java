package com.mayarafelix.icemoviesapp.detail;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mayarafelix.icemoviesapp.R;
import com.mayarafelix.icemoviesapp.model.Movie;
import com.mayarafelix.icemoviesapp.utils.Constants;
import com.mayarafelix.icemoviesapp.utils.UrlManager;

public class DetailActivity extends AppCompatActivity
{
    private Movie movie;
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieGenre;
    private TextView movieActors;
    private TextView movieDirector;
    private TextView moviePlot;
    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //-------------------------------------------
        // Action Bar
        //-------------------------------------------

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_movies_24dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //-------------------------------------------
        // View Elements
        //-------------------------------------------

        moviePoster   = findViewById(R.id.movieDetailPoster);
        movieTitle    = findViewById(R.id.movieDetailTitle);
        movieYear     = findViewById(R.id.movieDetailYear);
        movieGenre    = findViewById(R.id.movieDetailGenre);
        movieActors   = findViewById(R.id.movieDetailActors);
        movieDirector = findViewById(R.id.movieDetailDirector);
        moviePlot     = findViewById(R.id.movieDetailPlot);
        progressBar   = findViewById(R.id.progressbar);

        //-------------------------------------------
        // Get Movie
        //-------------------------------------------

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            movie = (Movie) (extras.getSerializable(Constants.MOVIE_TAG));

        }

        //-------------------------------------------
        // Update Screen
        //-------------------------------------------

        if (movie != null)
        {
            movieTitle.setText(movie.getTitle());
            movieYear.setText(String.valueOf(movie.getYear()));
            movieGenre.setText(movie.getGenre());
            movieActors.setText(movie.getActors());
            movieDirector.setText(movie.getDirector());
            moviePlot.setText(movie.getPlot());
        }

        //-------------------------------------------
        // Get Image
        //-------------------------------------------

        String imageUrl = movie.getUrlImage();
        DownloadImageDetail downloadImage = new DownloadImageDetail(DetailActivity.this, movie);
        downloadImage.execute(imageUrl);

        //-------------------------------------------
        // Get Information - If Empty
        //-------------------------------------------

        if (movie.getDirector().isEmpty() || movie.getGenre().isEmpty() || movie.getActors().isEmpty() || movie.getPlot().isEmpty())
        {
            String detailUrl = UrlManager.getMovieDetail(movie.getImdbId());
            DownloadMovieDetail downloadDetail = new DownloadMovieDetail(DetailActivity.this);
            downloadDetail.execute(detailUrl);
        }
    }

    public void updateDetailReturn(Movie movie)
    {
        if (movie != null)
        {
            movieGenre.setText(movie.getGenre());
            movieActors.setText(movie.getActors());
            movieDirector.setText(movie.getDirector());
            moviePlot.setText(movie.getPlot());
        }
    }

    public void updateImage(Drawable drawable)
    {
        if (drawable != null)
        {
            moviePoster.setImageDrawable(drawable);
        }
        else
        {
            moviePoster.setImageResource(R.drawable.ic_noimage);
        }

    }
}
