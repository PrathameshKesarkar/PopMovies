package com.example.prathamesh.popmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

public class PopMoviesActivity extends AppCompatActivity implements MoviesFragment.MoviesCallback{

    public  static final  String INTENT_EXTRA="com.example.prathamesh.popmovies>INTNETTAG";
    android.support.v7.widget.Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_movies);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container,new MoviesFragment()).commit();

    }

    @Override
    public void getCurrentItem(int position) {
        Toast.makeText(this,"Called "+position,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,DetailMovieActivity.class);
        intent.putExtra(INTENT_EXTRA,position);
        startActivity(intent);
    }
}
