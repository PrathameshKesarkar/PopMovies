package com.example.prathamesh.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prathamesh.popmovies.model.MovieDataList;
import com.example.prathamesh.popmovies.model.Results;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prathamesh on 6/18/15.
 */
public class SimpleMovieAdapter extends  RecyclerView.Adapter<SimpleMovieAdapter.MovieViewHolder>{
    Context mContext;
    LayoutInflater mLayoutInflater;
    public  static  final  String  BASE_IMG_URL= "http://image.tmdb.org/t/p/w185/";
    SimpleMovieAdapter(Context context){
        mContext= context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.single_grid_view_row,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        //Setting Tile for Movie
        holder.mTextView.setText(MovieDataList.get(mContext)
                .getResultsArrayList()
                .get(position)
                .getOriginal_title());

        Picasso.with(mContext).load(BASE_IMG_URL+MovieDataList.get(mContext).getResultsArrayList().get(position).getPoster_path()).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return MovieDataList.get(mContext).getResultsArrayList().size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView ;
        TextView mTextView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            mTextView = (TextView) itemView.findViewById(R.id.movie_title);
        }
    }
}
