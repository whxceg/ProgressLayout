package com.sam.lib.progresslayout;

import android.view.View;

public class SimpleProgressClickListener implements DownloadProgressButton.OnProgressClickListener {

    private View.OnClickListener mStartListener;
    private View.OnClickListener mDownloadListener;
    private View.OnClickListener mFinishListener;

    public SimpleProgressClickListener(View.OnClickListener mStartListener, View.OnClickListener mDownloadListener, View.OnClickListener mFinishListener) {
        this.mStartListener = mStartListener;
        this.mDownloadListener = mDownloadListener;
        this.mFinishListener = mFinishListener;
    }

    public SimpleProgressClickListener() {
    }

    public SimpleProgressClickListener setStartListener(View.OnClickListener mStartListener){
        this.mStartListener = mStartListener;
        return this;
    }
    public SimpleProgressClickListener setDownloadListener(View.OnClickListener mDownloadListener){
        this.mDownloadListener = mDownloadListener;
        return this;
    }
    public SimpleProgressClickListener setFinishListener(View.OnClickListener mFinishListener){
        this.mFinishListener = mFinishListener;
        return this;
    }

    @Override
    public void onStartClick(View view) {
        if (mStartListener != null) mStartListener.onClick(view);
    }

    @Override
    public void onDownloadClick(View view) {
        if (mDownloadListener != null) mDownloadListener.onClick(view);
    }

    @Override
    public void onFinishClick(View view) {
        if (mFinishListener != null) mFinishListener.onClick(view);
    }
}
