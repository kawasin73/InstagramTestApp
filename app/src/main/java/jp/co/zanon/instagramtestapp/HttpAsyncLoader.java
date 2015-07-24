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

    private final String TAG = getClass().getSimpleName();
    private String url;

    public HttpAsyncLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        // okHttpを利用して、APIへリクエストを投げる
        // 結果はStringで result に入る
        LogUtil.d(TAG, "url="+this.url);
        Request request = new Request.Builder()
                .url(this.url)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            // JSONを Stringのままで返す
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //エラーの時は、 null を返す。
        return null;
    }
}
