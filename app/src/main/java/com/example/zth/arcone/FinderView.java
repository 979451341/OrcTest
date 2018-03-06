package com.example.zth.arcone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by ZTH on 2018/3/5.
 */

public class FinderView  extends RelativeLayout {

    private static Point ScrRes;

    private Paint mPaint;
    private Rect mFrameRect; //绘制的Rect
    private Rect mRect; //返回的Rect
    private int PMwidth,PMheight,left,top,right,bottom;
    private int mMaskColor;

    public FinderView(Context context) {
        this(context, null);
    }

    public FinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Resources resources = getResources();
        mMaskColor = resources.getColor(R.color.finder_mask);


        init(context);
    }


    private void init(Context context) {
        // 需要调用下面的方法才会执行onDraw方法
        setWillNotDraw(false);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        PMwidth = display.getWidth();
        PMheight = display.getHeight();

        left = PMwidth/10;
        top = PMheight/3;
        right = PMwidth*9/10;
        bottom = PMheight*4/9;
        mFrameRect = new Rect(left,top,right,bottom);
    }



    @Override
    public void onDraw(Canvas canvas) {
        int width = PMwidth;
        int height = PMheight;
        Rect frame = mFrameRect;

        // 绘制焦点框外边的暗色背景
        mPaint.setColor(mMaskColor);
        canvas.drawRect(0, 0, width, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, mPaint);
        canvas.drawRect(0, frame.bottom + 1, width, height, mPaint);

    }

}
