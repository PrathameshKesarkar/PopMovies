package com.example.prathamesh.popmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.etsy.android.grid.StaggeredGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    final static String INTENT_ITEM_POSTION = "postion";
    ArrayList<String> mImageStrings = new ArrayList<String>();
    StaggeredGridView mGridView;
    SimpleMovieAdapter mSimpleMovieAdapter;
    private View view;

    public MoviesFragment() {

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
            MoviesList.get(getActivity()).clearList();
            SharedPreferences sortPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort_type = sortPref.getString(getResources().getString(R.string.pref_key), "popularity.desc");
            new MovieFetcher().execute(getMoviesURI(sort_type));
        } else {
            Toast.makeText(getActivity(),"Check for network Connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_movies, container, false);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pop_movies, menu);
    }

    private URL getMoviesURI(String SortingType) {

        //Creates URl for getting JSon data
        final String BASEMOVIEURL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY_PARAM = "sort_by";
        final String API_KEY = "api_key";
        final String API_KEY_VALUE = "b2a58f9c5024d33c3441440f14f5dd2f";
        try {
            Uri.Builder builder = Uri.parse(BASEMOVIEURL).buildUpon();
            builder.appendQueryParameter(SORT_BY_PARAM, SortingType);
            builder.appendQueryParameter(API_KEY, API_KEY_VALUE);
            String movieUrlString = builder.toString();
            URL url = new URL(movieUrlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
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

    public void movieDataInitializer(String s) {
        final String RESULTS_IDENTIFIER = "results";
        final String BACKDROP_PATH = "backdrop_path";
        final String ORIGNAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String USERRATING = "vote_average";

        try {
            JSONObject movieJsonObject = new JSONObject(s);

            //Result in an array in Json which Contains Data
            JSONArray resultsJSON = movieJsonObject.getJSONArray(RESULTS_IDENTIFIER);
            JSONObject generalizeObj = new JSONObject();
            String genearalizeData;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            for (int i = 0; i < resultsJSON.length(); i++) {
                //Reusing the Same Json Object and String Object to Increase Performance
                //Note :I have to create MovieDataModel Cause the Data was getting Overwritten
                MoviesDataModel moviesDataModel = new MoviesDataModel();
                generalizeObj = resultsJSON.getJSONObject(i);

                //BackdropData
                genearalizeData = generalizeObj.getString(BACKDROP_PATH);
                moviesDataModel.setBackDrop(genearalizeData);

                //setting Orignal title
                genearalizeData = generalizeObj.getString(ORIGNAL_TITLE);
                moviesDataModel.setOrignalTitle(genearalizeData);

                //Setting OverView
                genearalizeData = generalizeObj.getString(OVERVIEW);
                moviesDataModel.setPlot(genearalizeData);

                //setting Release Date
                genearalizeData = generalizeObj.getString(RELEASE_DATE);
                moviesDataModel.setReleaseDate(simpleDateFormat.parse(genearalizeData));

                //Poster Path
                genearalizeData = generalizeObj.getString(POSTER_PATH);
                moviesDataModel.setMoviePosterString(genearalizeData);

                //User Rating
                genearalizeData = generalizeObj.getString(USERRATING);
                moviesDataModel.setUserRating(genearalizeData);

                //Setting the Data in a the Single data Model
                MoviesList.get(getActivity()).setMoviesLists(moviesDataModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getMoviesArryList() {
        //Gives the List of Movie Poster used for populating GridView
        ArrayList<String> listData = new ArrayList<String>();
        for (MoviesDataModel data : MoviesList.get(getActivity()).getMoviesLists()) {
            listData.add(data.getMoviePosterString());
        }
        return listData;
    }

    class MovieFetcher extends AsyncTask<URL, Void, String> {
        HttpURLConnection mHttpURLConnection;
        BufferedReader mReader;
        String mMoviesStringData;

        //Simply Fetching data and passing it to PostExecution for setting the Data on the Screen
        @Override
        protected String doInBackground(URL... params) {
            URL url = null;
            try {
                url = params[0];
                mHttpURLConnection = (HttpURLConnection) url.openConnection();
                mHttpURLConnection.setRequestMethod("GET");
                mHttpURLConnection.connect();

                InputStream inputStream = mHttpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                mReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = mReader.readLine()) != null) {
                    builder = builder.append(line + "\n");
                }
                if (builder.length() == 0) {
                    return null;
                }
                mMoviesStringData = builder.toString();
                return mMoviesStringData;
            } catch (MalformedURLException e1) {

                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (mHttpURLConnection != null) {
                    mHttpURLConnection.disconnect();
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            movieDataInitializer(s);

            mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
            if (getActivity()!= null)
                mSimpleMovieAdapter = new SimpleMovieAdapter(getActivity(), android.R.layout.simple_list_item_1);
            mImageStrings = getMoviesArryList();

            //Checking if there is data no point in adding no Data
            if (mImageStrings.size() != 0&&mImageStrings!=null) {
                if(mSimpleMovieAdapter!=null)
                    mSimpleMovieAdapter.clear();
                mSimpleMovieAdapter.addAll(mImageStrings);
            }
            mGridView.setAdapter(mSimpleMovieAdapter);

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                    intent.putExtra(INTENT_ITEM_POSTION, position);
                    startActivity(intent);
                }
            });
        }

    }
}
