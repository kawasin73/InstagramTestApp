package jp.co.zanon.instagramtestapp;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;

/**
 * Created by shintaro1 on 15/07/24.
 */
public class ImageLoadedCallback implements Callback {

    Context context;
    ProgressBar mProgressBar;

    public ImageLoadedCallback (Context context){
        this.context = context;
    }
    public ImageLoadedCallback (Context context, ProgressBar progressBar){
        // contextをセット
        // contextの有無で　Toast を出すかどうかを切り分ける
        this.context = context;
        this.mProgressBar = progressBar;
    }

    @Override
    public void onSuccess() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (this.context != null) {
            Toast.makeText(this.context, "ネットワークエラー", Toast.LENGTH_SHORT).show();
        }
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(View.GONE);
        }
    }
}
