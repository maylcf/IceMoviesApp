package com.mayarafelix.icemoviesapp.detail;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mayarafelix.icemoviesapp.model.Movie;

import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mlcf on 2018-01-25.
 */

public class DownloadMovieDetail extends AsyncTask<String, Void, Movie>
{
    private final WeakReference<DetailActivity> activityReference;

    public DownloadMovieDetail(@NonNull DetailActivity activity)
    {
        this.activityReference = new WeakReference<DetailActivity>(activity);
    }

    @Override
    protected Movie doInBackground(String... urls)
    {
        Movie movie = null;

        // read the url
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
                // get information
                InputStream inputStream = connection.getInputStream();
                String jsonString = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();

                // disconnect
                inputStream.close();
                connection.disconnect();

                // Convert to json
                JSONObject json = new JSONObject(jsonString);

                String response = json.optString("Response", "");

                if (response.trim().equals("True"))
                {
                    movie = new Movie(json);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return movie;
    }

    @Override
    protected void onPostExecute(@Nullable Movie movie)
    {
        super.onPostExecute(movie);
        DetailActivity activity = activityReference.get();
        activity.updateDetailReturn(movie);
    }
}
