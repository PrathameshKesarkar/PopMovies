package com.example.prathamesh.popmovies;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prathamesh.popmovies.model.MovieDataList;
import com.example.prathamesh.popmovies.model.MoviePOJO;
import com.example.prathamesh.popmovies.network_interface.MoviesInterface;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    final static String INTENT_ITEM_POSTION = "postion";
    ArrayList<String> mImageStrings = new ArrayList<String>();
    SimpleMovieAdapter mSimpleMovieAdapter;

    MoviesCallback mMoviesCallback;
    public static final String BASE_URL = "http://api.themoviedb.org";
    //Storing the default Value for Shared Pref so that if the User Pref Changes it can be Updated in the UI
    // and We can clear the List that contains the previous Results
    private String previousPref ="popularity.desc";
    RecyclerView recyclerView;

    public MoviesFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof MoviesCallback){
            mMoviesCallback = (MoviesCallback) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //Checking for Connection so we can inflater the Layout and Handling the force Stop caused by no Internet Connection
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInterface = connectivityManager.getActiveNetworkInfo();
        return networkInterface != null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isConnected()) {
            SharedPreferences sortPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort_type = sortPref.getString(getResources().getString(R.string.pref_key), "popularity.desc");
            retroNetworkInitializer(sort_type);

        } else {
            Toast.makeText(getActivity(), "Check for network Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pop_movies, menu);
    }


    private void retroNetworkInitializer(final String sortingType) {
        final String API_KEY_VALUE = "b2a58f9c5024d33c3441440f14f5dd2f";

        //Calling the retrofit RestAdapter to initalize Movie in Pojo
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();
        MoviesInterface moviesInterface = restAdapter.create(MoviesInterface.class);
        moviesInterface.getResponse(sortingType, API_KEY_VALUE, new Callback<MoviePOJO>() {
            @Override
            public void success(MoviePOJO moviePojo, Response response) {
                Toast.makeText(getActivity(), "Hitting Sucess", Toast.LENGTH_LONG).show();

                SimpleMovieAdapter adapter = new SimpleMovieAdapter(getActivity());

                //Condtion to check if the Previously selected Type is equal to Current Selected
                if (previousPref.equals(sortingType)) {
                    MovieDataList.get(getActivity()).getResultsArrayList().clear();
                    adapter.notifyDataSetChanged();
                    previousPref = sortingType;
                }
                MovieDataList.get(getActivity()).setResultsArrayList(moviePojo.getResults());
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                recyclerView.addOnItemTouchListener(new MoviesRecyclerTouchListener(getActivity(), recyclerView, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int postion) {
                        mMoviesCallback.getCurrentItem(postion);
                    }
                }));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                Log.e("ErrorTag", error.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), Setting.class);
            startActivity(intent);
            return true;
        }

        return false;
    }



    //Interface to add the data back to Activity so it can be passed to DetailActivity
    //this removes the tight coupling between the fragment even if tablet Config is used
    interface  MoviesCallback{
        void getCurrentItem(int position);
    }

   //Interface is used for handling recyclerView item Click
    interface RecyclerViewClickListener{
            void onClick(View view,int postion);
    }
}