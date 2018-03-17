package com.movies.venka.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.movies.venka.popularmovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by venka on 1/23/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MovieHolder>  {
    ArrayList<MovieDetails> al;
    Context context;
    public RecyclerAdapter(Context context,ArrayList<MovieDetails> al)
    {
        this.al=al;
        this.context=context;
    }
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        context=parent.getContext();
        boolean shouldAttachToParent=false;
        context=parent.getContext();
        int layoutid=R.layout.number_image;
        View view=layoutInflater.inflate(layoutid,parent,shouldAttachToParent);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, final int position) {
        holder.bind(position);
        context=holder.imageView.getContext();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetails movieDetails=al.get(position);
                Intent i=new Intent(context,DetailsActivity.class);
                i.putExtra("details", movieDetails);
                view.getContext().startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return al.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        public MovieHolder(View view)
        {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.poster_view);
        }
        public void bind(final int index)
        {
            Picasso.with(context).load(NetworkUtils.buildImageURL(al.get(index).getPoster_path().substring(1))).into(imageView);
        }
    }
}
