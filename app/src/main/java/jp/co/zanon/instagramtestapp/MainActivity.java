package jp.co.zanon.instagramtestapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ArrayList<InstagramItem> list;
    private MyGridAdapter gridAdapter;
    private InstagramInfo instagramInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.hello_world);


        //最初のページのURLをセットする
        instagramInfo = new InstagramInfo("https://api.instagram.com/v1/tags/iQON/media/recent?client_id=94569f2163b140d696814954f18b5987");

        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        //グリッドの個数返却処理
        ((GridLayoutManager) mRecyclerView.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return 1;

            }
        });
        list = new ArrayList<InstagramItem>();
        gridAdapter = new MyGridAdapter(this, list);
        mRecyclerView.setAdapter(gridAdapter);
        getRequest();

    }

    private void updateAdapter() {
        list.clear();
        ArrayList<InstagramItem> clist = instagramInfo.getList();
        for (InstagramItem item : clist) {
            list.add(item);
            LogUtil.d(TAG, "item.thumbnail = " + item.thumbnail);
        }

        //GridViewスタイルのAdapterを設定
        gridAdapter.notifyDataSetChanged();
        LogUtil.d(TAG, "adapter Count="+Integer.toString(gridAdapter.getItemCount()));
    }

    public void getRequest(){

        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String result = null;

                Request request = new Request.Builder()
                        .url(instagramInfo.getNextUrl())
                        .get()
                        .build();

                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                LogUtil.d(TAG, result);
                instagramInfo.loadJson(result);
                updateAdapter();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
