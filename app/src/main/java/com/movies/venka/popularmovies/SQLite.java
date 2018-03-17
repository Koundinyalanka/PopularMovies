package com.movies.venka.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by venka on 1/25/2018.
 */

public class SQLite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public SQLite(Context context) {
        super(context, MovieValues.MovieEntry.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override//I did not include genre_ids
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     String CREATE_TABLE="CREATE TABLE "+ MovieValues.MovieEntry.TABLE_NAME+"("
             +MovieValues.MovieEntry.COLUMN_MOVIE_ID +" INTEGER"+","
             + MovieValues.MovieEntry.COLUMN_COUNT+ " INTEGER"+","
             + MovieValues.MovieEntry.COLUMN_VIDEO+" TEXT"
             +","+MovieValues.MovieEntry.COLUMN_RATING +" TEXT"+","
             + MovieValues.MovieEntry.COLUMN_TITLE+" TEXT"+","
             + MovieValues.MovieEntry.COLUMN_POP+" TEXT"+","
             + MovieValues.MovieEntry.COLUMN_IMAGEP+" TEXT"+","
             + MovieValues.MovieEntry.COLUMN_OL+" TEXT"+","
             +MovieValues.MovieEntry.COLUMN_OT+" TEXT"+","
             + MovieValues.MovieEntry.COLUMN_IMAGEB +" TEXT"+","
             + MovieValues.MovieEntry.COLUMN_A+" TEXT"+","
             +MovieValues.MovieEntry.COLUMN_OVERVIEW+" TEXT"+","
             +MovieValues.MovieEntry.COLUMN_DATE+" TEXT"+")";
     sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +MovieValues.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public boolean hasObject(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + MovieValues.MovieEntry.TABLE_NAME + " WHERE " + MovieValues.MovieEntry.COLUMN_TITLE + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id});
        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
    /*public void fillFavouriteMovies(JSONArray jsonArray)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i = 0; i< jsonArray.length(); i++)
        {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put("id", jsonObject.getInt("id"));
                values.put("vote_count", jsonObject.getInt("vote_count"));
                values.put("video",jsonObject.getString("video"));
                values.put("vote_average",jsonObject.getString("vote_average"));
                values.put("title",jsonObject.getString("title"));
                values.put("popularity",jsonObject.getString("popularity"));
                values.put("poster_path",jsonObject.getString("poster_path"));
                values.put("original_language",jsonObject.getString("original_language"));
                values.put("original_title",jsonObject.getString("original_title"));
                values.put("backdrop_path",jsonObject.getString("backdrop_path"));
                values.put("adult",jsonObject.getString("adult"));
                values.put("overview",jsonObject.getString("overview"));
                values.put("release_date",jsonObject.getString("release_date"));
                db.insert(MOVIE_TABLE_NAME, null, values);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        db.close();
        Log.e("Movie Data", "filled");
    }*/

}
