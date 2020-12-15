package com.example.a8laboratorinis;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeAndTouch implements View.OnTouchListener
{
    private final GestureDetector gestureDetector;

    public SwipeAndTouch(Context context)
    {
        gestureDetector = new GestureDetector(context, new Gesture());
    }

    public void SwipeRight(){};
    public void SwipeLeft(){};
    public void SingleTouch(){};
    public void SecondTouchEntry(double distance, double x, double y){};
    public void SecondTouchEnd(){};

    public boolean onTouch(View v, MotionEvent e)
    {
        if(e.getAction() == MotionEvent.ACTION_DOWN)
        {
            SingleTouch();
        }
        if(e.getAction() == MotionEvent.ACTION_MOVE && e.getPointerCount() > 1)
        {
            double x = (e.getX(1) + e.getX(0)) / 2;
            double y = (e.getY(1) + e.getY(0)) / 2;
            double distance = Math.sqrt(Math.pow(e.getX(1) - e.getX(0), 2) + Math.pow(e.getY(1) - e.getY(0), 2));
            SecondTouchEntry(distance, x, y);
        }
        else if (e.getAction() == MotionEvent.ACTION_POINTER_UP)
        {
            SecondTouchEnd();
        }
        return gestureDetector.onTouchEvent(e);
    }

    private final class Gesture extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e2.getY();

            if(Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
            {
                if(distanceX > 0)
                {
                    SwipeRight();
                }
                else
                {
                    SwipeLeft();
                }
                return true;
            }
            return false;
        }
    }
}
