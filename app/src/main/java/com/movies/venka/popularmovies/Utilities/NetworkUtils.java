package com.movies.venka.popularmovies.Utilities;

/**
 * Created by venka on 1/17/2018.
 */

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This Class contains API KEY and URLS Required
 * */
public final class NetworkUtils {
    private static final String API_KEY="d066c3682196e40a533e3972bc5b915e";
    public static final String BASE_URL="http://api.themoviedb.org/3";
    private static final String IMG_URL="http://image.tmdb.org/t/p/w185";
    public static URL movie=null;
    public static URL image=null;
    public static URL buildURL(String choice)//build url based on choice dude
    {
        Uri movieUri=Uri.parse(BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(choice)
                .appendQueryParameter("api_key",API_KEY)
                .build();
        try {
            movie=new URL(movieUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return movie;
    }
    public static Uri buildImageURL(String path)
    {
        Uri imageUri=Uri.parse(IMG_URL).buildUpon()
                .appendPath(path)
                .build();
        //System.out.println(imageUri+"");
        try {
            image=new URL(imageUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return imageUri;
    }
    public static URL buildReTrURL(String choice,int id) throws MalformedURLException {
        Uri uri=Uri.parse(BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(id+"")
                .appendPath(choice)
                .appendQueryParameter("api_key",API_KEY)
                .build();
        URL url=new URL(uri.toString());
        return url;
    }
    public static String getTheResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

}
