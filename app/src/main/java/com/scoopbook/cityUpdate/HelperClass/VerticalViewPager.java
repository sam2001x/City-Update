package com.scoopbook.cityUpdate.HelperClass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(@NonNull Context context) {
        super(context);
    }

    public VerticalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true,new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    private class VerticalPageTransformer implements PageTransformer{

        @Override
        public void transformPage(@NonNull View page, float position) {

            if(position<-1){
                //[-infintiy,-1]
                page.setAlpha(0);

            }else if (position<=0){
                //[-1,0]
                page.setAlpha(1);
                page.setTranslationX(page.getWidth() * -position);

                float Yposition = position * page.getHeight();
                page.setTranslationY(Yposition);
                page.setScaleX(1);
                page.setScaleY(1);

            }else if (position<=1){

                //[0,1] counteract the default slid transition.
                page.setTranslationX(page.getWidth() * -position);
                // to scale the page down.

                float scale = 0.75f + (1 - 0.75f) * (1-Math.abs(position));
                page.setScaleX(scale);
                page.setScaleY(scale);
            }else{
//                [1,+infinity] this page is way off screen to right
                page.setAlpha(0);
            }

        }
    }
    private MotionEvent swapXYCordinates(MotionEvent event){
//        swaping X and Y cordinates
        float width = getWidth();
        float height = getHeight();

        float newX = (event.getY() / height) * width;
        float newY = (event.getX() / width) * height;

        event.setLocation(newX,newY);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXYCordinates(ev));
        swapXYCordinates(ev); //returning touch cordinates to orignal refrence frame for any child.
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(swapXYCordinates(ev));
    }
}
