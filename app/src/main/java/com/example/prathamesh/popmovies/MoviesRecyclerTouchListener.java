package com.example.prathamesh.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.example.prathamesh.popmovies.MoviesFragment.RecyclerViewClickListener;

/**
 * Created by prathamesh on 7/11/15.
 */
public class MoviesRecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private  GestureDetector gestureDetector;
    RecyclerViewClickListener recyclerViewClickListener;
    public MoviesRecyclerTouchListener(Context context,RecyclerView recyclerView
            ,RecyclerViewClickListener recyclerViewClickListener) {

           //Simple initialize the refrence of the Interface
            this.recyclerViewClickListener =recyclerViewClickListener;

            //Simple Gesture is used as it handles all the remaining Gesture that are not required
           gestureDetector= new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

               //Single TapUp Represents the Single Click that is from bottom to Up
               //Returning the true here is Imp as it indicate we are interested in handling the Click
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

    }


    //Code is added to this section as the result of the Single Tap Up is processed by this Function
    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        //simply getting the position in the Grid where is the item is Clicked
        View childView = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
        if(childView!=null&&recyclerViewClickListener!=null&&gestureDetector.onTouchEvent(motionEvent))
            recyclerViewClickListener.onClick(childView,recyclerView.getChildAdapterPosition(childView));
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
