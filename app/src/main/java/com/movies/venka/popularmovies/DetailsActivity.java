package com.movies.venka.popularmovies;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.AsyncTaskLoader;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.venka.popularmovies.Utilities.NetworkUtils;
import com.movies.venka.popularmovies.Utilities.UIUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String SEARCH_ = "";
    private static final String SEARCH_1 ="" ;
    private TextView title,date,rating;
   TextView overview;
   SQLite sqLite=new SQLite(this);
    private ImageView backdrop,poster,favourite;
    ListView listView;TextView review;
    String text;
    ContentValues values;
    MovieDetails movie;
    private static final int TRAILER_REVIEW_LOADER=23;
    private static final int REVIEW_LOADER=24;
    private static final int FAVOURITE_ADDER=25;
    private static final int FAVOURITE_REMOVER=26;
    private static Bundle save=new Bundle();
    boolean fav=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title=(TextView)findViewById(R.id.showTitle);
        date=(TextView)findViewById(R.id.release_date);
        rating=(TextView)findViewById(R.id.rating);
        overview=(TextView) findViewById(R.id.synopsis);
        backdrop=(ImageView)findViewById(R.id.backdrop);
        poster=(ImageView)findViewById(R.id.poster_view);
        favourite=(ImageView)findViewById(R.id.favourite);
        listView=(ListView)findViewById(R.id.list);
        review=(TextView) findViewById(R.id.review);
        Bundle extras = getIntent().getExtras();
        movie= (MovieDetails) getIntent().getParcelableExtra("details");
        fav=sqLite.hasObject(movie.getTitle());
        if(fav)
        {
            favourite.setBackgroundResource(R.drawable.favourite);
        }
        else
        {
            favourite.setBackgroundResource(R.drawable.notfavourite);
        }
        Bundle movieBundle=new Bundle();
        movieBundle.putString(SEARCH_,"videos");
        getLoaderManager().initLoader(TRAILER_REVIEW_LOADER,movieBundle,this);
        if(movie!=null)
        {
            title.setText(movie.getTitle());
            date.setText("Release Date: " + movie.getRelease_date());
            rating.setText(movie.getVote_average());
            overview.setText(movie.getOverview());
            Picasso.with(this).load(NetworkUtils.buildImageURL(movie.getBackdrop_path().substring(1))).into(backdrop);
            Picasso.with(this).load(NetworkUtils.buildImageURL(movie.getPoster_path().substring(1))).into(poster);
            try {
                Toast.makeText(this,NetworkUtils.buildReTrURL("videos",movie.getId())+"",Toast.LENGTH_SHORT).show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav)
                {
                    getLoaderManager().initLoader(FAVOURITE_REMOVER,null,DetailsActivity.this);
                    favourite.setBackgroundResource(R.drawable.notfavourite);
                    fav=false;
                }
                else
                {
                    getLoaderManager().initLoader(FAVOURITE_ADDER,null,DetailsActivity.this);
                    favourite.setBackgroundResource(R.drawable.favourite);
                    fav=true;
                }
            }
        });
        Bundle reviewBundle=new Bundle();
        reviewBundle.putString(SEARCH_1,"reviews");
        getLoaderManager().initLoader(REVIEW_LOADER,reviewBundle,this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("is_fav",fav);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        fav=savedInstanceState.getBoolean("is_fav");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<String> onCreateLoader(int i,final Bundle bundle) {
            switch (i) {
                case TRAILER_REVIEW_LOADER:
                return new AsyncTaskLoader<String>(this) {
                    @Override
                    public String loadInBackground() {
                        String query = bundle.getString(SEARCH_);
                        String s = "";
                        try {
                            s = NetworkUtils.getTheResponse(NetworkUtils.buildReTrURL(query, movie.getId()));
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                        return s;
                    }
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (bundle == null)
                            return;
                        forceLoad();
                    }
                };

                case REVIEW_LOADER:
                    return new AsyncTaskLoader<String>(this) {
                        @Override
                        public String loadInBackground() {
                            String query = bundle.getString(SEARCH_1);
                            String s = "";
                            try {
                                s = NetworkUtils.getTheResponse(NetworkUtils.buildReTrURL(query, movie.getId()));
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                            return s;
                        }

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();
                            if (bundle == null)
                                return;
                            forceLoad();
                        }
                    };
                case FAVOURITE_ADDER:
                    return new AsyncTaskLoader<String>(this) {
                        @Override
                        public String loadInBackground() {
                            values = new ContentValues();

                            values.put(MovieValues.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                            values.put(MovieValues.MovieEntry.COLUMN_TITLE, movie.getTitle());
                            values.put(MovieValues.MovieEntry.COLUMN_IMAGEP, movie.getPoster_path());
                            values.put(MovieValues.MovieEntry.COLUMN_IMAGEB, movie.getBackdrop_path());
                            values.put(MovieValues.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                            values.put(MovieValues.MovieEntry.COLUMN_RATING, movie.getVote_average());
                            values.put(MovieValues.MovieEntry.COLUMN_DATE, movie.getRelease_date());

                            values.put(MovieValues.MovieEntry.COLUMN_COUNT, movie.getVote_count());
                            values.put(MovieValues.MovieEntry.COLUMN_VIDEO, movie.getVideo());
                            values.put(MovieValues.MovieEntry.COLUMN_POP, movie.getPopularity());
                            values.put(MovieValues.MovieEntry.COLUMN_IMAGEB, movie.getBackdrop_path());
                            values.put(MovieValues.MovieEntry.COLUMN_OT, movie.getOriginal_title());
                            values.put(MovieValues.MovieEntry.COLUMN_OL, movie.getOriginal_language());
                            values.put(MovieValues.MovieEntry.COLUMN_A, movie.getAdult());
                          //  getContentResolver().insert(MovieValues.MovieEntry.CONTENT_URI, values);
                            return "Added to Favourites";
                        }

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();
                            forceLoad();
                        }
                    };
                case FAVOURITE_REMOVER:
                    return new AsyncTaskLoader<String>(this) {
                        @Override
                        public String loadInBackground() {
                            getContentResolver().delete(MovieValues.MovieEntry.CONTENT_URI,MovieValues.MovieEntry.COLUMN_MOVIE_ID+" =? ",new String[]{Integer.toString(movie.getId())});
                            return "Removed From Favourites";
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
        switch (loader.getId()) {
            case TRAILER_REVIEW_LOADER:
            try {
                    parseTrailer(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            break;
            case REVIEW_LOADER:
                try {
                    parseReviews(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case FAVOURITE_ADDER:
                Toast.makeText(this,s,Toast.LENGTH_LONG).show();
                getContentResolver().insert(MovieValues.MovieEntry.CONTENT_URI, values);
                break;
            case FAVOURITE_REMOVER:
                Toast.makeText(this,s,Toast.LENGTH_LONG).show();
                break;


        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    public void parseTrailer(String s) throws JSONException {
        JSONObject jsonObject1=new JSONObject(s);
        JSONArray results=jsonObject1.getJSONArray("results");
        final ArrayList<TrailerDetails> arrayList=new ArrayList<>();
        int c=0;
        for(int i=0;i<results.length();i++)
        {
            JSONObject jsonObject=results.getJSONObject(i);

            if(jsonObject.getString("type").contentEquals("Trailer")){
                arrayList.add(new TrailerDetails(jsonObject));
                c++;
            }
        }
        int j=0;
        String[] sample=new String[c];
        for(int i=0;i<c;i++)
        {
            if(arrayList.get(i).getType().contentEquals("Trailer"))
            sample[i]=arrayList.get(i).getName();
        }
        listView.setAdapter(new ArrayAdapter<String>(this,R.layout.listview,R.id.trailer,sample));
        UIUtils.setListViewHeightBasedOnItems(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                watchYoutubeVideo(getApplicationContext(),arrayList.get(i).getKey());
            }
        });
    }
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
    public void parseReviews(String s) throws JSONException {
        JSONObject jsonObject1=new JSONObject(s);
        JSONArray results=jsonObject1.getJSONArray("results");

        for(int i=0;i<results.length();i++)
        {
            JSONObject jsonObject=results.getJSONObject(i);
            review.append(jsonObject.getString("author")+"\n"+jsonObject.getString("content")+"\n\n");
        }
    }
}
