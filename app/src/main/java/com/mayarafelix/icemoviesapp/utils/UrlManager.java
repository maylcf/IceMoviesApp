package com.mayarafelix.icemoviesapp.utils;

public class UrlManager
{
    public static String getMoviesByTitle(String title)
    {
        return Constants.BASE_URL + "&s=" + title + "&type=movie";
    }

    public static String getMovieDetail(String imdbId)
    {
        return Constants.BASE_URL + "&i=" + imdbId + "&plot=full";
    }
}
