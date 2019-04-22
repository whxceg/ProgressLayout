package com.sam.lib.progresslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ProgressLayout extends RelativeLayout {

    private Paint mStrokePaint = new Paint();
    private Paint mBackgroundPaint = new Paint();
    private RectF mBackgroundStoke = new RectF();


    private int mStrokeColor;
    private float mStrokeWidth = 1f;

    private int mBackgroundColor;
    private int mProgressColor;

    private float mCornerRadius;

    private float mProgressPercent = 0f;

    private long lastTime = 0;
    private long delay = 100;

    public ProgressLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressLayout);

        mStrokeColor = ta.getColor(R.styleable.ProgressLayout_strokeColor, Color.parseColor("#3262DE"));
        mBackgroundColor = ta.getColor(R.styleable.ProgressLayout_backgroundColor, Color.parseColor("#F1FAFF"));
        mProgressColor = ta.getColor(R.styleable.ProgressLayout_progressColor, Color.parseColor("#E5F2FA"));
        mStrokeWidth = ta.getDimension(R.styleable.ProgressLayout_stroke, 1);
        mCornerRadius = ta.getDimension(R.styleable.ProgressLayout_cornerRadius, 5);

        ta.recycle();

        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setStrokeWidth(mStrokeWidth);
        mBackgroundPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBackgroundPaint.setShader(ss());
        mBackgroundStoke.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundStoke, mCornerRadius, mCornerRadius, mBackgroundPaint);
        mBackgroundStoke.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundStoke, mCornerRadius, mCornerRadius, mStrokePaint);
    }

    private LinearGradient ss() {
        return new LinearGradient(mStrokeWidth, 0, getMeasuredWidth() - mStrokeWidth, 0,
                new int[]{mProgressColor, mBackgroundColor},
                new float[]{mProgressPercent, mProgressPercent + 1f / getMeasuredWidth()},
                Shader.TileMode.CLAMP);
    }

    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        mProgressPercent = progress / 100f;
        if (System.currentTimeMillis() - lastTime > delay) {
            lastTime = System.currentTimeMillis();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    postInvalidate();
                }
            }, delay);
        }

    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return new ProgressSaveState(super.onSaveInstanceState(), mProgressPercent);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof ProgressSaveState) {
            ProgressSaveState parcelable = (ProgressSaveState) state;
            super.onRestoreInstanceState(parcelable.getSuperState());
            mProgressPercent = parcelable.progress;
            postInvalidate();
        }

    }

    static class ProgressSaveState extends BaseSavedState {
        private float progress;

        ProgressSaveState(Parcel in) {
            super(in);
            progress = in.readFloat();
        }

        ProgressSaveState(Parcelable superState, float progress) {
            super(superState);
            this.progress = progress;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(progress);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProgressSaveState> CREATOR = new Creator<ProgressSaveState>() {
            @Override
            public ProgressSaveState createFromParcel(Parcel in) {
                return new ProgressSaveState(in);
            }

            @Override
            public ProgressSaveState[] newArray(int size) {
                return new ProgressSaveState[size];
            }
        };
    }

}
