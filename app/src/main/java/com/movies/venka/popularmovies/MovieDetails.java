package com.movies.venka.popularmovies;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by venka on 1/25/2018.
 */

public class MovieDetails implements Parcelable{
     private int id,vote_count;
    private String video,vote_average,title,popularity,poster_path,original_language,original_title,backdrop_path,adult,overview,release_date;
    public MovieDetails(int id,int vote_count,String video,String vote_average,String title,String popularity,String poster_path,String original_language,String original_title,String backdrop_path,String adult,String overview,String release_date)
    {
        this.id=id;
        this.vote_count=vote_count;
        this.video=video;
        this.vote_average=vote_average;
        this.title=title;
        this.popularity=popularity;
        this.poster_path=poster_path;
        this.original_language=original_language;
        this.original_title=original_title;
        this.backdrop_path=backdrop_path;
        this.adult=adult;
        this.overview=overview;
        this.release_date=release_date;
    }

    protected MovieDetails(Parcel in) {
        id = in.readInt();
        vote_count = in.readInt();
        video = in.readString();
        vote_average = in.readString();
        title = in.readString();
        popularity = in.readString();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        backdrop_path = in.readString();
        adult = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }
    protected MovieDetails(Cursor cursor)
    {
        this.id=cursor.getInt(0);
        this.vote_count=cursor.getInt(1);
        this.video=cursor.getString(2);
        this.vote_average=cursor.getString(3);
        this.title=cursor.getString(4);
        this.popularity=cursor.getString(5);
        this.poster_path=cursor.getString(6);
        this.original_language=cursor.getString(7);
        this.original_title=cursor.getString(8);
        this.backdrop_path=cursor.getString(9);
        this.adult=cursor.getString(10);
        this.overview=cursor.getString(11);
        this.release_date=cursor.getString(12);

    }
    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    public void setId(int id)
    {
        this.id=id;
    }
    public int getId()
    {
        return id;
    }
    public void setVote_count(int id)
    {
        this.vote_count=id;
    }
    public int getVote_count()
    {
        return vote_count;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getAdult() {
        return adult;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRelease_date() {
        return release_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    /*int id,int vote_count,String video,String vote_average,String title,String popularity,String poster_path,String original_language,
    String original_title,String backdrop_path,String adult,String overview,String release_date)*/
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(vote_count);
        parcel.writeString(video);
        parcel.writeString(vote_average);
        parcel.writeString(title);
        parcel.writeString(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeString(backdrop_path);
        parcel.writeString(adult);
        parcel.writeString(overview);
        parcel.writeString(release_date);

    }
}
