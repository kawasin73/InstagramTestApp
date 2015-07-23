package jp.co.zanon.instagramtestapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MyGridAdapter adapter;
    private InstagramList mList;
    private ParseInstagramJson parseInstagramJson;

    private boolean isLoading = false;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // ツールバーの設定
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.hello_world);

        //最初のページのURLをセットする
        mList = new InstagramList(Property.getFirstUrl("iQON"));
        parseInstagramJson = new ParseInstagramJson(mList);

        // RecyclerView の 初期設定
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        //グリッドの個数返却処理
        ((GridLayoutManager) mRecyclerView.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return 1;
            }
        });

        mRecyclerView.addOnScrollListener(new EndlessScrollListener((GridLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                if (!MainActivity.this.isLoading)
                    startLoading();
            }
        });

        mRecyclerView.addOnItemTouchListener(new ItemClickListener(mRecyclerView){

            @Override
            boolean performItemClick(RecyclerView parent, View view, int position, long id) {
                InstagramItem item = mList.getList().get(position);
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("url", item.standard);
                startActivity(intent);
                return false;
            }

            @Override
            boolean performItemLongClick(RecyclerView parent, View view, int position, long id) {
                return false;
            }
        });

        setAdapter();

        startLoading();

    }

    private void setAdapter(){
        // adapterを初期化してセットする。
        adapter = new MyGridAdapter(this, mList.getList());
        mRecyclerView.setAdapter(adapter);
    }

    public void startLoading(){
        getLoaderManager().restartLoader(this.count, null, this);
        LogUtil.d(TAG, "count=" + Integer.toString(this.count));
        this.count++;
    }

    private void updateAdapter() {
        // 情報を更新
        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        LogUtil.d(TAG, "onCreateLoader");
        LogUtil.d(TAG, "onCreateLoader id="+Integer.toString(id));
        this.isLoading = true;
        // Instagram API へリクエストを投げる
        HttpAsyncLoader loader = new HttpAsyncLoader(this, this.mList.getNextUrl());
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        LogUtil.d(TAG, "onLoadFinished");
        LogUtil.d(TAG, "onLoadFinished onCreateLoader count="+Integer.toString(loader.getId()));
        // Json文字列を変換してリストに保存
        parseInstagramJson.loadJson(data);
        updateAdapter();
        isLoading = false;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

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
