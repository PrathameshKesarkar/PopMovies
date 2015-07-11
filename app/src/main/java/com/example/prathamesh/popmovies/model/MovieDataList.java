package com.example.prathamesh.popmovies.model;

import android.content.Context;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prathamesh on 08-07-2015.
 */
public class MovieDataList  {
    private List<Results> mResultsArrayList = new ArrayList<Results>();
    static  MovieDataList sMovieDataList;
    Context mContext;
    MovieDataList(Context context){
          mContext= context;
    }
    public static MovieDataList get(Context context){
        if(sMovieDataList==null){
            sMovieDataList = new MovieDataList(context);
        }
        return  sMovieDataList;
    }
    public List<Results>  getResultsArrayList(){
        return mResultsArrayList;
    }
    public  void  setResultsArrayList(List<Results> resultsList){
        for (Results Results:resultsList)
            mResultsArrayList.add(Results);
    }
    public void setSingleResult(Results result){
        mResultsArrayList.add(result);
    }
    public Results getSingleResultByPostion(int pos){
        return mResultsArrayList.get(pos);
    }
    public void  clearList(){
        mResultsArrayList.clear();
    }
}
