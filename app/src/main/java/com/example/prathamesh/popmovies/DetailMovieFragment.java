package com.example.prathamesh.popmovies;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prathamesh.popmovies.model.MovieDataList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment {

    private final String BASE_URl = "http://image.tmdb.org/t/p/w500/";
    private static final String BUNDLE_INT = "DetailMovie";
    private int mItemPosition;
    private Format mFormatter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemPosition = getArguments().getInt(DetailMovieActivity.BUNDLE_ARGS);

    }
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
        collapsingToolbarLayout.setTitle(MovieDataList.get(getActivity()).getSingleResultByPosition(mItemPosition).getOriginal_title());
        mFormatter = new SimpleDateFormat("yyyy-MM-dd");
        final Window window = getActivity().getWindow();

        //backDrop Poster
        final ImageView imageView = (ImageView) view.findViewById(R.id.detail_header_imageview);
        String header_image = MovieDataList.get(getActivity()).getSingleResultByPosition(mItemPosition).getBackdrop_path();
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

        rating.setText(String.valueOf(MovieDataList.get(getActivity()).getSingleResultByPosition(mItemPosition).getVote_average()));
       //using MVC Pattern to get Poster Url and then Loading in ImageView
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185"+MovieDataList.get(getActivity()).getSingleResultByPosition(mItemPosition).getPoster_path()).into(poster);

        releaseDate.setText(MovieDataList.get(getActivity()).getSingleResultByPosition(mItemPosition).getRelease_date());

        plot.setText(MovieDataList.get(getActivity()).getSingleResultByPosition(mItemPosition).getOverview());

        return view;
    }



}
