package com.faranegar.customtoolbar;

import android.os.Handler;

public class DelayHandler implements Runnable {

    private Handler mHandler;
    private String queryString;

    private OnDelayCompletedListener onDelayCompletedListener;
    private int delaySearchAction;
    private final int DELAY_SEARCH_ACTION = 1000;

    public void setOnDelayCompletedListener(OnDelayCompletedListener onDelayCompletedListener) {
        this.onDelayCompletedListener = onDelayCompletedListener;
    }

    public DelayHandler() {
        mHandler = new Handler();
    }

    public void setDelaySearchAction(int delaySearchAction){
        this.delaySearchAction = delaySearchAction;
    }

    @Override
    public void run() {
        if (onDelayCompletedListener != null)
            onDelayCompletedListener.onDelayCompleted(queryString);
    }

    public void startDelay(String queryString) {
        this.queryString = queryString;
        mHandler.removeCallbacks(this);
        if( delaySearchAction == 0 ){
            mHandler.postDelayed(this, DELAY_SEARCH_ACTION );
        } else{
            mHandler.postDelayed(this, delaySearchAction );
        }
        if(queryString.equals(""))
            mHandler.postDelayed(this, 0);

    }

    /**
     * an interface to say to the Context that the intended delay has been passed!
     */
    public interface OnDelayCompletedListener {
        /**
         * called once the delay duration has been passed!
         *
         * @param queryString
         */
        void onDelayCompleted(String queryString);
    }

}