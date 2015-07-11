package com.example.prathamesh.popmovies.network_interface;

import com.example.prathamesh.popmovies.model.MoviePOJO;
import com.example.prathamesh.popmovies.model.Results;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Prathamesh on 07-07-2015.
 */
public interface MoviesInterface {
    @GET("/3/discover/movie")
    public void getResponse(@Query("sort_by") String sort_Type,@Query("api_key") String keyVal, Callback<MoviePOJO> pojo);
}
