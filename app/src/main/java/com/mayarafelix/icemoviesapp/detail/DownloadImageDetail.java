package com.mayarafelix.icemoviesapp.detail;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.mayarafelix.icemoviesapp.model.Movie;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mlcf on 2018-01-25.
 */

public class DownloadImageDetail extends AsyncTask<String, Void, Drawable>
{
    private final WeakReference<DetailActivity> activity;
    private Movie movie;

    public DownloadImageDetail(@NonNull DetailActivity activity, Movie movie)
    {
        this.activity = new WeakReference<DetailActivity>(activity);
        this.movie = movie;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        DetailActivity detailActivity = activity.get();
        detailActivity.progressBar.setVisibility(View.VISIBLE);
        detailActivity.progressBar.setProgress(0);
    }

    @Override
    protected Drawable doInBackground(String... urls)
    {
        Drawable result = null;

        // read the url
        String urlString = urls != null && urls.length > 0 ? urls[0] : "";

        if (urlString == null || urlString.isEmpty())
        {
            return null;
        }

        InputStream inputStream = null;

        try
        {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(2000); // 2 seconds
            connection.setConnectTimeout(3000); // 3 seconds
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200)
            {
                // get stream from remote place
                inputStream = connection.getInputStream();

                // Convert image
                result = Drawable.createFromStream(inputStream, movie.getImdbId());

                // disconnect
                inputStream.close();
                connection.disconnect();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @Override
    protected void onPostExecute(@Nullable Drawable result)
    {
        super.onPostExecute(result);
        DetailActivity detailActivity = activity.get();
        detailActivity.progressBar.setVisibility(View.GONE);
        detailActivity.updateImage(result);
    }
}
