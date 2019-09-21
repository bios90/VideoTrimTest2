package com.dimfcompany.videotrimtest2.custom_views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;

public class CustomSeek2 extends AppCompatSeekBar
{
    public Boolean isDragging = false;
    private Drawable mThumb;

    public CustomSeek2(Context context)
    {
        super(context);

    }

    public CustomSeek2(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    @Override
    public void setThumb(Drawable thumb)
    {
        super.setThumb(thumb);
        mThumb = thumb;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {

            if (event.getX() >= mThumb.getBounds().left-100
                    && event.getX() <= mThumb.getBounds().right+100
                    && event.getY() <= mThumb.getBounds().bottom
                    && event.getY() >= mThumb.getBounds().top)
            {

                isDragging = true;
                return false;
//                super.onTouchEvent(event);
            }
            else
            {
                return false;
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            isDragging = false;
            return false;
        }
        else
        {
            super.onTouchEvent(event);
        }

        return true;
    }
}
