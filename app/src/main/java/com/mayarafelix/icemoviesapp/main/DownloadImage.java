package com.mayarafelix.icemoviesapp.main;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mayarafelix.icemoviesapp.model.Movie;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mlcf on 2018-01-25.
 */

public class DownloadImage extends AsyncTask<String, Void, Drawable>
{
    private final WeakReference<MainActivity> activityReference;
    private int position;
    private Movie movie;

    public DownloadImage(@NonNull MainActivity appActivity, Movie movie, int position)
    {
        this.activityReference = new WeakReference<MainActivity>(appActivity);
        this.movie = movie;
        this.position = position;
    }

    @Override
    protected Drawable doInBackground(String... urls)
    {
        Drawable result = null;

        String urlString = urls != null && urls.length > 0 ? urls[0] : "";

        if (urlString == null || urlString.isEmpty())
        {
            return null;
        }

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
                InputStream inputStream = connection.getInputStream();

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
        MainActivity activity = activityReference.get();
        activity.updateMovieImageReturn(result, position);
    }
}
