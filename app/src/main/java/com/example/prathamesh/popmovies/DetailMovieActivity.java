package com.example.prathamesh.popmovies;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailMovieActivity extends AppCompatActivity {


    public static  final  String BUNDLE_ARGS="com.example.prathamesh.popmovies.BUNDLETAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        //Fragment Declaration
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.detailMovie, createFragment()).commit();


    }

    DetailMovieFragment createFragment() {
        Intent intent = getIntent();
        int data_position = intent.getIntExtra(PopMoviesActivity.INTENT_EXTRA, 0);
        DetailMovieFragment fragment = new DetailMovieFragment();
        Bundle positionBundle = new Bundle();
        positionBundle.putInt(BUNDLE_ARGS,data_position);
        fragment.setArguments(positionBundle);
        return fragment;
    }
}
