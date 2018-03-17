package com.movies.venka.popularmovies;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.movies.venka.popularmovies.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String SEARCH = "1";
    private RecyclerAdapter mAdapter;
    private RecyclerView imglist;
    public ProgressDialog pd;
    public SQLite sqLite;
    private static final int POPULARMOVIES_SEARCH_LOADER=22;
    private static final int FAVOURITE_MOVIES_LOADER=23;
    ArrayList<MovieDetails> a=new ArrayList<>();
    private static final String[] movies={MovieValues.MovieEntry.COLUMN_MOVIE_ID,
            MovieValues.MovieEntry.COLUMN_COUNT,
            MovieValues.MovieEntry.COLUMN_VIDEO,
            MovieValues.MovieEntry.COLUMN_RATING,
            MovieValues.MovieEntry.COLUMN_TITLE,
            MovieValues.MovieEntry.COLUMN_POP,
            MovieValues.MovieEntry.COLUMN_IMAGEP,
            MovieValues.MovieEntry.COLUMN_OL,
            MovieValues.MovieEntry.COLUMN_OT,
            MovieValues.MovieEntry.COLUMN_IMAGEB,
            MovieValues.MovieEntry.COLUMN_A,
            MovieValues.MovieEntry.COLUMN_OVERVIEW,
            MovieValues.MovieEntry.COLUMN_DATE
            };
    int numberOfCols=2,i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLite=new SQLite(this);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage(getString(R.string.wait));
        pd.setCancelable(false);
        pd.show();
        Bundle movieBundle=new Bundle();
        movieBundle.putString(SEARCH,"popular");
        getLoaderManager().initLoader(POPULARMOVIES_SEARCH_LOADER,movieBundle,this);
        onSaveInstanceState(movieBundle);
        imglist=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,numberOfCols);
        imglist.setLayoutManager(gridLayoutManager);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menulist,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//handle items selected
        int itemthat=item.getItemId();
        if(itemthat==R.id.most_popular)
        {   /*Sort by most popular*/
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage(getString(R.string.wait));
            pd.setCancelable(false);
            pd.show();
            Bundle movieBundle=new Bundle();
            movieBundle.putString(SEARCH,getString(R.string.popularmovies));
            getLoaderManager().restartLoader(POPULARMOVIES_SEARCH_LOADER,movieBundle,this);
            return true;
        }
        if(itemthat==R.id.highest_rated)
        {/*sort by highest rated*/
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage(getString(R.string.wait));
            pd.setCancelable(false);
            pd.show();
            Bundle movieBundle=new Bundle();
            movieBundle.putString(SEARCH,getString(R.string.ratedmovies));

            getLoaderManager().restartLoader(POPULARMOVIES_SEARCH_LOADER,movieBundle,this);
            return true;
        }
        if(itemthat==R.id.favourite_view)
        {
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage(getString(R.string.wait));
            pd.setCancelable(false);
            pd.show();
            Bundle movieBundle=new Bundle();
            movieBundle.putString(SEARCH,"favourite");
            getLoaderManager().restartLoader(FAVOURITE_MOVIES_LOADER,movieBundle,this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int i,final Bundle bundle) {
        switch (i)
        {
            case POPULARMOVIES_SEARCH_LOADER:
                return new AsyncTaskLoader<String>(this) {
                 @Override
                public String loadInBackground() {
                String query=bundle.getString(SEARCH);
                String s="";
                try {
                    s=NetworkUtils.getTheResponse(NetworkUtils.buildURL(query));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return s;
            }

            @Override
            protected void onStartLoading(){
                super.onStartLoading();
                if(bundle==null)
                    return;
                forceLoad();
            }
        };
            case FAVOURITE_MOVIES_LOADER:
                return new AsyncTaskLoader<String>(this) {
                    @Override
                    public String loadInBackground() {

                        Cursor cursor=this.getContext().getContentResolver().query(MovieValues.MovieEntry.CONTENT_URI,movies,null,null,null);
                        a=getFavoriteMoviesDataFromCursor(cursor);
                        return "";
                    }

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        forceLoad();
                    }
                };
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        switch (loader.getId())
        {
            case POPULARMOVIES_SEARCH_LOADER:
        parse(s);
        if (pd.isShowing()){
            pd.dismiss();
        }
        break;
        case FAVOURITE_MOVIES_LOADER:
                if(!a.isEmpty())
                {
                    mAdapter=new RecyclerAdapter(this,a);
                    imglist.setAdapter(mAdapter);
                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                }
                else
                {
                    if (pd.isShowing()){
                        pd.dismiss();
                    }
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this);
                    }
                    builder.setTitle("No Favourites")
                            .setMessage("Select your Favourite movie by tapping on heart symbol")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
                break;
        }
    }
    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    private ArrayList<MovieDetails> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        ArrayList<MovieDetails> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MovieDetails movie = new MovieDetails(cursor);
                results.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }
    public void parse(String s)
    {
        ArrayList<MovieDetails> al=new ArrayList<>();
        try {
            JSONObject jsonObject1=new JSONObject(s);
            JSONArray results=jsonObject1.getJSONArray(getString(R.string.result));
            for(i=0;i<results.length();i++)
            {
                JSONObject jsonObject=results.getJSONObject(i);
                al.add(new MovieDetails(jsonObject.getInt("id"),jsonObject.getInt("vote_count"),
                        jsonObject.getString("video"),jsonObject.getString("vote_average"),jsonObject.getString("title")
                        ,jsonObject.getString("popularity"),
                        jsonObject.getString("poster_path"),jsonObject.getString("original_language"),
                        jsonObject.getString("original_title"),jsonObject.getString("backdrop_path"),
                        jsonObject.getString("adult"),jsonObject.getString("overview"),jsonObject.getString("release_date")));
            }
            mAdapter=new RecyclerAdapter(this,al);
            imglist.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
