package com.tb.mvvm_library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;

import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.tb.mvvm_library.R;

/**
 * @author: MrTangBo
 * @decribe:
 */
public class ArcView extends View {
    private int mWidth;
    private int mHeight;
    /**
     * 弧形高度
     */
    private int mArcHeight;
    /**
     * 背景颜色
     */
    private int mBgColor;

    private String arcOrientation = "top";


    private Paint mPaint;
    private Context mContext;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcView_arcHeight, 0);
        mBgColor = typedArray.getColor(R.styleable.ArcView_bgColor, Color.parseColor("#303F9F"));
        arcOrientation = typedArray.getString(R.styleable.ArcView_arcOrientation);
        mContext = context;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);
        if (arcOrientation.endsWith("top")) {
            Rect rect = new Rect(0, 0, mWidth, mHeight);
            canvas.drawRect(rect, mPaint);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.tb_white));
            Path path = new Path();
            path.moveTo(0, mHeight);
            path.quadTo(mWidth / 2, mHeight - mArcHeight, mWidth, mHeight);
            canvas.drawPath(path, mPaint);
        } else {
            //矩形部分
            Rect rect = new Rect(0, 0, mWidth, mHeight - mArcHeight);
            canvas.drawRect(rect, mPaint);
            //弧形部分
            Path path = new Path();
            path.moveTo(0, mHeight - mArcHeight);
            path.quadTo(mWidth / 2, mHeight, mWidth, mHeight - mArcHeight);
            canvas.drawPath(path, mPaint);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }
}
