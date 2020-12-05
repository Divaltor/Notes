package com.divaltor.notes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class FitScrollView extends ScrollView {
    public FitScrollView(Context context) {
        super(context);
    }

    public FitScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FitScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            if (childAt.getMeasuredHeight() < MeasureSpec.getSize(heightMeasureSpec))
                childAt.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY));
        }
    }
}
