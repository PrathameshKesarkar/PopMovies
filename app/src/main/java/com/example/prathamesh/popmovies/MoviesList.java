package com.example.prathamesh.popmovies;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by prathamesh on 6/18/15.
 */
public class MoviesList {
    private ArrayList<MoviesDataModel> mMoviesLists = new ArrayList<MoviesDataModel>();
    private static MoviesList sMovieList ;
    Context mContext;
    //Making class Constructor so no other Activity can Create the Object
    private  MoviesList(Context context){
        mContext=context;
    }
    //returns the Single MovieList\ Object
    public static  MoviesList get(Context context){
        if(sMovieList==null){
            sMovieList = new MoviesList(context);
        }
        return sMovieList;
    }
    public ArrayList<MoviesDataModel> getMoviesLists(){
        return mMoviesLists;
    }

    public MoviesDataModel getAtPostion(int position){
        return mMoviesLists.get(position);
    }
    //add Data Individually
    public void setMoviesLists(MoviesDataModel dataModel){
        mMoviesLists.add(dataModel);
    }
    public void setMoviesLists(ArrayList<MoviesDataModel> array){
        for(MoviesDataModel model :array){
            mMoviesLists.add(model);
        }
    }
    //Used this Function to avoid repeatedly adding data to arrayList
    public void clearList(){
        mMoviesLists.clear();
    }

}
