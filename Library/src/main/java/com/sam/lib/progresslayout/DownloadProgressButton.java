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
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.sam.lib.progresslayout.DownloadProgressButton.State.STATE_DOWN;
import static com.sam.lib.progresslayout.DownloadProgressButton.State.STATE_FINISH;
import static com.sam.lib.progresslayout.DownloadProgressButton.State.STATE_INIT;

public class DownloadProgressButton extends RelativeLayout implements View.OnClickListener {

    private Paint mTextPaint = new Paint();
    private Paint mStrokePaint = new Paint();
    private Paint mBackgroundPaint = new Paint();
    private RectF mBackgroundRect = new RectF();

    private int mInitTextColor;
    private int mInitStrokeColor;
    private int mInitBackgroundColor;

    private int mFinishTextColor;
    private int mFinishStrokeColor;
    private int mFinishBackgroundColor;

    private int mDownloadStrokeColor;
    private int mDownloadBackgroundColor;
    private int mDownloadTextColor;
    private int mDownloadProgressColor;

    private float mStrokeWidth;
    private float mCornerRadius;

    private float mProgressPercent = 0f;


    private String mInitText;
    private String mFinishText;
    private String mDownloadText;

    private int mInitTextSize;
    private int mFinishTextSize;
    private int mDownloadTextSize;

    private @State
    int mState = STATE_INIT;

    private OnProgressClickListener mListener;
    private long lastTime;
    private long delay = 200;


    public DownloadProgressButton(Context context) {
        super(context);
        init(context, null);
    }

    public DownloadProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        setWillNotDraw(false);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DownloadProgressButton);

        mInitTextColor = ta.getColor(R.styleable.DownloadProgressButton_psInitTextColor, Color.parseColor("#3262DE"));
        mInitStrokeColor = ta.getColor(R.styleable.DownloadProgressButton_psInitStrokeColor, Color.parseColor("#3262DE"));
        mInitBackgroundColor = ta.getColor(R.styleable.DownloadProgressButton_psInitBackgroundColor, Color.parseColor("#F1FAFF"));

        mDownloadTextColor = ta.getColor(R.styleable.DownloadProgressButton_psDownloadTextColor, Color.parseColor("#3262DE"));
        mDownloadStrokeColor = ta.getColor(R.styleable.DownloadProgressButton_psDownloadStrokeColor, Color.parseColor("#3262DE"));
        mDownloadBackgroundColor = ta.getColor(R.styleable.DownloadProgressButton_psDownloadBackgroundColor, Color.parseColor("#F1FAFF"));
        mDownloadProgressColor = ta.getColor(R.styleable.DownloadProgressButton_psDownloadProgressColor, Color.parseColor("#E5F2FA"));

        mFinishTextColor = ta.getColor(R.styleable.DownloadProgressButton_psFinishTextColor, Color.parseColor("#FFFFFF"));
        mFinishStrokeColor = ta.getColor(R.styleable.DownloadProgressButton_psFinishStrokeColor, Color.parseColor("#3262DE"));
        mFinishBackgroundColor = ta.getColor(R.styleable.DownloadProgressButton_psFinishBackgroundColor, Color.parseColor("#3262DE"));

        mStrokeWidth = ta.getDimension(R.styleable.DownloadProgressButton_psStroke, 1);
        mCornerRadius = ta.getDimension(R.styleable.DownloadProgressButton_psCornerRadius, 5);


        mInitText = ta.getString(R.styleable.DownloadProgressButton_psInitText);
        mDownloadText = ta.getString(R.styleable.DownloadProgressButton_psDownloadText);
        mFinishText = ta.getString(R.styleable.DownloadProgressButton_psFinishText);

        mInitTextSize = ta.getDimensionPixelSize(R.styleable.DownloadProgressButton_psInitTextSize, 28);
        mDownloadTextSize = ta.getDimensionPixelSize(R.styleable.DownloadProgressButton_psDownloadTextSize, 28);
        mFinishTextSize = ta.getDimensionPixelSize(R.styleable.DownloadProgressButton_psFinishTextSize, 28);

        ta.recycle();

        if (mInitText == null) mInitText = "下载";
        if (mFinishText == null) mFinishText = "打开";
        if (mDownloadText == null) mDownloadText = "%d%%";

        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setStrokeWidth(mStrokeWidth);
        mBackgroundPaint.setAntiAlias(true);

        mTextPaint.setTextSize(20);
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mState) {
            case STATE_INIT:
                drawInit(canvas);
                break;
            case STATE_DOWN:
                drawDownload(canvas);
                break;
            case STATE_FINISH:
                drawFinish(canvas);
                break;
        }
    }

    private void drawInit(Canvas canvas) {
        mBackgroundPaint.setShader(null);
        mBackgroundPaint.setColor(mInitBackgroundColor);
        mBackgroundRect.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mBackgroundPaint);

        mStrokePaint.setColor(mInitStrokeColor);
        mBackgroundRect.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mStrokePaint);

        mTextPaint.setColor(mInitTextColor);
        mTextPaint.setTextSize(mInitTextSize);
        float width = mTextPaint.measureText(mInitText);
        canvas.drawText(mInitText, getMeasuredWidth() / 2 - width / 2, getMeasuredHeight() / 2 - (mTextPaint.descent() / 2 + mTextPaint.ascent() / 2), mTextPaint);
    }

    private void drawDownload(Canvas canvas) {

        mBackgroundPaint.setShader(shader());
        mBackgroundRect.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mBackgroundPaint);

        mStrokePaint.setColor(mDownloadStrokeColor);
        mBackgroundRect.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mStrokePaint);

        mTextPaint.setColor(mDownloadTextColor);
        mTextPaint.setTextSize(mDownloadTextSize);
        int progress = (int) (mProgressPercent * 100);

        String progressText = String.format(mDownloadText, progress);
        float width = mTextPaint.measureText(progressText);
        canvas.drawText(progressText, getMeasuredWidth() / 2 - width / 2, getMeasuredHeight() / 2 - (mTextPaint.descent() / 2 + mTextPaint.ascent() / 2), mTextPaint);
    }

    private LinearGradient shader() {
        return new LinearGradient(mStrokeWidth, 0, getMeasuredWidth() - mStrokeWidth, 0,
                new int[]{mDownloadProgressColor, mDownloadBackgroundColor},
                new float[]{mProgressPercent, mProgressPercent + 1f / getMeasuredWidth()},
                Shader.TileMode.CLAMP);
    }

    private void drawFinish(Canvas canvas) {
        mBackgroundPaint.setShader(null);
        mBackgroundPaint.setColor(mFinishBackgroundColor);
        mBackgroundRect.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mBackgroundPaint);

        mStrokePaint.setColor(mFinishStrokeColor);
        mBackgroundRect.set(mStrokeWidth, mStrokeWidth, getMeasuredWidth() - mStrokeWidth, getMeasuredHeight() - mStrokeWidth);
        canvas.drawRoundRect(mBackgroundRect, mCornerRadius, mCornerRadius, mStrokePaint);

        mTextPaint.setColor(mFinishTextColor);
        mTextPaint.setTextSize(mFinishTextSize);
        float width = mTextPaint.measureText(mFinishText);
        canvas.drawText(mFinishText, getMeasuredWidth() / 2 - width / 2, getMeasuredHeight() / 2 - (mTextPaint.descent() / 2 + mTextPaint.ascent() / 2), mTextPaint);
    }

    @Override
    public void onClick(View v) {
        if (mState == STATE_INIT) {
            if (mListener != null) mListener.onStartClick(this);
        } else if (mState == STATE_FINISH) {
            if (mListener != null) mListener.onFinishClick(this);
        } else if (mState == STATE_DOWN) {
            if (mListener != null) mListener.onDownloadClick(this);
        }

    }

    public void setState(@State int state) {
        if (this.mState != state) {
            this.mState = state;
            postInvalidate();
        }
    }

    public int getState() {
        return mState;
    }

    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        mProgressPercent = progress / 100f;
        if (mState == STATE_DOWN && System.currentTimeMillis() - lastTime > delay) {
            lastTime = System.currentTimeMillis();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    postInvalidate();
                }
            }, delay);
        }
    }

    @IntDef({STATE_INIT, STATE_DOWN, STATE_FINISH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int STATE_INIT = 0;
        int STATE_DOWN = 1;
        int STATE_FINISH = 2;
    }

    public void setOnProgressClickListener(OnProgressClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnProgressClickListener {
        void onStartClick(View view);

        void onDownloadClick(View view);

        void onFinishClick(View view);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        StateSave parcelable = new StateSave();
        parcelable.mInitTextColor = mInitTextColor;
        parcelable.mInitStrokeColor = mInitStrokeColor;
        parcelable.mInitBackgroundColor = mInitBackgroundColor;
        parcelable.mFinishTextColor = mFinishTextColor;
        parcelable.mFinishStrokeColor = mFinishStrokeColor;
        parcelable.mFinishBackgroundColor = mFinishBackgroundColor;

        parcelable.mDownloadStrokeColor = mDownloadStrokeColor;
        parcelable.mDownloadBackgroundColor = mDownloadBackgroundColor;
        parcelable.mDownloadTextColor = mDownloadTextColor;
        parcelable.mDownloadProgressColor = mDownloadProgressColor;

        parcelable.mStrokeWidth = mStrokeWidth;
        parcelable.mCornerRadius = mCornerRadius;
        parcelable.mProgressPercent = mProgressPercent;
        parcelable.mInitText = mInitText;
        parcelable.mFinishText = mFinishText;
        parcelable.mDownloadText = mDownloadText;
        parcelable.mInitTextSize = mInitTextSize;
        parcelable.mFinishTextSize = mFinishTextSize;
        parcelable.mDownloadTextSize = mDownloadTextSize;
        parcelable.mState = mState;
        return parcelable;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof StateSave) {
            StateSave parcelable = (StateSave) state;
            mInitTextColor = parcelable.mInitTextColor;
            mInitStrokeColor = parcelable.mInitStrokeColor;
            mInitBackgroundColor = parcelable.mInitBackgroundColor;
            mFinishTextColor = parcelable.mFinishTextColor;
            mFinishStrokeColor = parcelable.mFinishStrokeColor;
            mFinishBackgroundColor = parcelable.mFinishBackgroundColor;

            mDownloadStrokeColor = parcelable.mDownloadStrokeColor;
            mDownloadBackgroundColor = parcelable.mDownloadBackgroundColor;
            mDownloadTextColor = parcelable.mDownloadTextColor;
            mDownloadProgressColor = parcelable.mDownloadProgressColor;

            mStrokeWidth = parcelable.mStrokeWidth;
            mCornerRadius = parcelable.mCornerRadius;
            mProgressPercent = parcelable.mProgressPercent;
            mInitText = parcelable.mInitText;
            mFinishText = parcelable.mFinishText;
            mDownloadText = parcelable.mDownloadText;

            mInitTextSize = parcelable.mInitTextSize;
            mFinishTextSize = parcelable.mFinishTextSize;
            mDownloadTextSize = parcelable.mDownloadTextSize;
            mState = parcelable.mState;
            postInvalidate();
        }

    }

    private static class StateSave implements Parcelable {
        private int mInitTextColor;
        private int mInitStrokeColor;
        private int mInitBackgroundColor;
        private int mFinishTextColor;
        private int mFinishStrokeColor;
        private int mFinishBackgroundColor;
        private int mDownloadStrokeColor;
        private int mDownloadBackgroundColor;
        private int mDownloadTextColor;
        private int mDownloadProgressColor;
        private float mStrokeWidth;
        private float mCornerRadius;
        private float mProgressPercent;
        private String mInitText;
        private String mFinishText;
        private String mDownloadText;
        private int mInitTextSize;
        private int mFinishTextSize;
        private int mDownloadTextSize;
        private int mState;

        StateSave() {
        }

        StateSave(Parcel in) {
            mInitTextColor = in.readInt();
            mInitStrokeColor = in.readInt();
            mInitBackgroundColor = in.readInt();
            mFinishTextColor = in.readInt();
            mFinishStrokeColor = in.readInt();
            mFinishBackgroundColor = in.readInt();
            mDownloadStrokeColor = in.readInt();
            mDownloadBackgroundColor = in.readInt();
            mDownloadTextColor = in.readInt();
            mDownloadProgressColor = in.readInt();
            mStrokeWidth = in.readFloat();
            mCornerRadius = in.readFloat();
            mProgressPercent = in.readFloat();
            mInitText = in.readString();
            mFinishText = in.readString();
            mDownloadText = in.readString();
            mInitTextSize = in.readInt();
            mFinishTextSize = in.readInt();
            mDownloadTextSize = in.readInt();
            mState = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mInitTextColor);
            dest.writeInt(mInitStrokeColor);
            dest.writeInt(mInitBackgroundColor);
            dest.writeInt(mFinishTextColor);
            dest.writeInt(mFinishStrokeColor);
            dest.writeInt(mFinishBackgroundColor);
            dest.writeInt(mDownloadStrokeColor);
            dest.writeInt(mDownloadBackgroundColor);
            dest.writeInt(mDownloadTextColor);
            dest.writeInt(mDownloadProgressColor);
            dest.writeFloat(mStrokeWidth);
            dest.writeFloat(mCornerRadius);
            dest.writeFloat(mProgressPercent);
            dest.writeString(mInitText);
            dest.writeString(mFinishText);
            dest.writeString(mDownloadText);
            dest.writeInt(mInitTextSize);
            dest.writeInt(mFinishTextSize);
            dest.writeInt(mDownloadTextSize);
            dest.writeInt(mState);
        }

        public static final Creator<StateSave> CREATOR = new Creator<StateSave>() {
            @Override
            public StateSave createFromParcel(Parcel in) {
                return new StateSave(in);
            }

            @Override
            public StateSave[] newArray(int size) {
                return new StateSave[size];
            }
        };
    }

}
