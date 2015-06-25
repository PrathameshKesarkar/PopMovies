package com.example.prathamesh.popmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Target;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment {

    private final String BASE_URl = "http://image.tmdb.org/t/p/w500/";
    private static final String BUNDLE_INT = "DetailMovie";
    private int mItemPostion;
    private Format mFormatter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //TextView represents the Synopsis of the Movie
        TextView plot = (TextView) view.findViewById(R.id.moviedescription);

        //poster
        ImageView poster = (ImageView) view.findViewById(R.id.poster_image_view);

        TextView releaseDate= (TextView) view.findViewById(R.id.releaseDate);
        TextView rating = (TextView) view.findViewById(R.id.rating);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        final CollapsingToolbarLayout templayout = collapsingToolbarLayout;
        collapsingToolbarLayout.setTitle(MoviesList.get(getActivity()).getAtPostion(mItemPostion).getOrignalTitle());
        mFormatter = new SimpleDateFormat("yyyy-MM-dd");
        final Window window = getActivity().getWindow();

        //backDrop Poster
        final ImageView imageView = (ImageView) view.findViewById(R.id.detail_header_imageview);
        String header_image = MoviesList.get(getActivity()).getAtPostion(mItemPostion).getBackDrop();
        Picasso.with(getActivity()).load(BASE_URl + header_image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                Palette.Builder builder = new Palette.Builder(bmap);
                Palette palette = builder.generate();

                //Extracting Colors from the Image to set The ToolBar Color and  status bar Color
                int darkVibrantColor = palette.getDarkVibrantColor(R.color.darkPrimaryColor);
                int vibrantColor = palette.getVibrantColor(R.color.primaryColor);
                templayout.setContentScrimColor(vibrantColor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(darkVibrantColor);
                    window.setNavigationBarColor(vibrantColor);
                }
            }
            @Override
            public void onError() {

            }
        });
        collapsingToolbarLayout = templayout;

        rating.setText(MoviesList.get(getActivity()).getAtPostion(mItemPostion).getUserRating());
       //using MVC Pattern to get Poster Url and then Loading in ImageView
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185"+MoviesList.get(getActivity()).getAtPostion(mItemPostion).getMoviePosterString()).into(poster);

        releaseDate.setText(mFormatter.format(MoviesList.get(getActivity()).getAtPostion(mItemPostion).getReleaseDate()));

        plot.setText(MoviesList.get(getActivity()).getAtPostion(mItemPostion).getPlot());

        return view;
    }

    public static DetailMovieFragment newInstance(int postion_data) {

        Bundle args = new Bundle();
        args.putInt(BUNDLE_INT, postion_data);
        DetailMovieFragment fragment = new DetailMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemPostion = getArguments().getInt(BUNDLE_INT);

    }
}
