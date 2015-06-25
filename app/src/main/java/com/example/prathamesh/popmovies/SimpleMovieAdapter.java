package com.example.prathamesh.popmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by prathamesh on 6/18/15.
 */
public class SimpleMovieAdapter extends ArrayAdapter<String>{
    Context mContext;
    LayoutInflater mLayoutInflater;
    Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatio=new SparseArray<Double>();
    public SimpleMovieAdapter(Context context, int resource) {
        super(context, resource);
        mContext= context;
        mLayoutInflater= LayoutInflater.from(context);
        mRandom =new Random();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if(convertView==null){
                convertView= mLayoutInflater.inflate(R.layout.single_staggered_view_row,parent,false);
                vh= new ViewHolder();
                vh.mImageView= (DynamicHeightImageView) convertView.findViewById(R.id.imgView);
                vh.mTextView = (TextView) convertView.findViewById(R.id.footer_text_view);
                convertView.setTag(vh);
            }
            else{
                vh = (ViewHolder) convertView.getTag();
            }
            double heightRatio = getPositionRatio(position);
        vh.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


        //to Remove the abrupt force stop c
        if(MoviesList.get(mContext).getMoviesLists().size()>0) {
            vh.mTextView.setText(MoviesList.get(mContext).getAtPostion(position).getOrignalTitle());
            vh.mImageView.setHeightRatio(heightRatio);

            //the textView is a dummy textView as I can't use the Actual footer
            final TextView textView = vh.mTextView;
            final ImageView imageView = vh.mImageView;
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + getItem(position)).into(vh.mImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bmap =((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    if(bmap!=null){
                        Palette.Builder builder = new Palette.Builder(bmap);
                        Palette  palette= builder.generate();
                        int vibrantColor = palette.getVibrantColor(android.R.color.transparent);
                        textView.setBackgroundColor(vibrantColor);
                    }
                }

                @Override
                public void onError() {

                }
            });
            vh.mTextView=textView;
        }

        return convertView;
    }
    private double getPositionRatio(int position) {
        double ratio = sPositionHeightRatio.get(position, 0.0);
        if(ratio==0){
            ratio= getRandomHeightRatios();
            sPositionHeightRatio.append(position,ratio);

        }
        return ratio;
    }

    private double getRandomHeightRatios() {

        return  (mRandom.nextDouble()/2.0)+1.0;
    }
    static class ViewHolder{
        //Actual Image Poster
        DynamicHeightImageView mImageView;

        //the TextView View Represents The Footer Of the Image
        TextView mTextView;

    }
}
