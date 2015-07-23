package jp.co.zanon.instagramtestapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by shintaro1 on 15/07/23.
 */
public class HttpAsyncLoader extends AsyncTaskLoader<String> {

    private String url;

    public HttpAsyncLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        String result = null;

        // okHttpを利用して、APIへリクエストを投げる
        // 結果はStringで result に入る
        Request request = new Request.Builder()
                .url(this.url)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JSONを Stringのままで返す
        return result;
    }
}
