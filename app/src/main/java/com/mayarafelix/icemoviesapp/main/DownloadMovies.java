package com.mayarafelix.icemoviesapp.main;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.mayarafelix.icemoviesapp.utils.Constants;
import com.mayarafelix.icemoviesapp.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mlcf on 2018-01-24.
 */

public class DownloadMovies extends AsyncTask<String, Void, JSONArray>
{
    private final WeakReference<MainActivity> activityReference;

    public DownloadMovies(@NonNull MainActivity appActivity)
    {
        activityReference = new WeakReference<MainActivity>(appActivity);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        MainActivity appActivity = activityReference.get();
        appActivity.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected JSONArray doInBackground(String... urls)
    {
        JSONArray result = null;

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

            int statusCode = connection.getResponseCode();

            if (statusCode == 200)
            {
                // Get information
                InputStream inputStream = connection.getInputStream();
                String jsonString = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();

                // disconnect
                inputStream.close();
                connection.disconnect();

                // Convert info to json
                JSONObject json = new JSONObject(jsonString);

                String response = json.optString("Response", "");

                if (response.trim().equals("True"))
                {
                    result = json.getJSONArray("Search");
                    Log.d(Constants.PROJECT_TAG, "Array Size: " + result.length());
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }


    protected void onPostExecute(JSONArray result)
    {
        MainActivity activity = activityReference.get();
        activity.progressBar.setVisibility(View.GONE);
        activity.populateArrayOfMovies(result);
    }
}
