package jp.co.zanon.instagramtestapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private final String TAG = getClass().getSimpleName();
    public static final String KEY_QUERY = "key_query";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    String query;

    private MyGridAdapter adapter;
    private InstagramList mList;
    private ParseInstagramJson parseInstagramJson;

    private EndlessScrollListener mScrollListener;

    private boolean isLoading = false;
    private boolean noMoreLoading = false;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.query = "iQON";

        if (savedInstanceState != null) {
            // 保存されていたタグ名をセット
            this.query = savedInstanceState.getString(MainActivity.KEY_QUERY, "iQON");
        }

        // ツールバーの設定
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_name);

        //最初のページのURLをセットする
        mList = new InstagramList(Property.getFirstUrl(query));

        //Json解析クラスを初期化
        parseInstagramJson = new ParseInstagramJson(mList);

        // RecyclerView の 初期設定
        initRecyclerView();

        // Adapter をセットする
        setAdapter();

        // SwipeRefreshLayout の初期設定
        initSwipeRefreshLayout();

        //最初のデータローディングを開始
        setNewQuery(query);

    }

    private void initRecyclerView() {
        // GridLayoutManagerをセット
        // Gridは３列で表示
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        // スクロールリスナーを保管する。
        // スクロールリスナーのメンバー変数 previous を変更するため
        mScrollListener = new EndlessScrollListener((GridLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                LogUtil.d(TAG, "come to bottom");
                startLoading();
            }
        };
        // 画面下までのスクロールを検出
        mRecyclerView.addOnScrollListener(mScrollListener);


        // 各アイテムに対するクリックリスナーを登録
        mRecyclerView.addOnItemTouchListener(new ItemClickListener(mRecyclerView) {

            @Override
            boolean performItemClick(RecyclerView parent, View view, int position, long id) {
                LogUtil.d(TAG, "position=" + position);
                LogUtil.d(TAG, "mList.getList().size()=" + mList.getList().size());

                if (position < 0 || position + 1 > mList.getList().size()) {
                    // スワイプリフレッシュを行い、ローディング中にアイテムをクリックされた場合、
                    // リストにないアイテムのpositionに渡す可能性がある。
                    return false;
                }
                // クリックされたら、SubActivityで大きな画像を表示する
                InstagramItem item = mList.getList().get(position);
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("url", item.standard);
                intent.putExtra("msg", item.msg);
                startActivity(intent);
                return false;
            }

            @Override
            boolean performItemLongClick(RecyclerView parent, View view, int position, long id) {
                // 長押しの場合
                return false;
            }
        });


    }

    private void setAdapter(){
        // adapterを初期化してセットする。
        adapter = new MyGridAdapter(this, mList.getList());
        mRecyclerView.setAdapter(adapter);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 検索を始めからやり直す
                refresh();
            }
        });
    }

    /**
     * 検索を始めからやり直す
     */
    public void refresh(){
        //実行中の一覧取得アクセスを破棄
        //常に実行されている Loader は１つだけ
        getLoaderManager().destroyLoader(count);

        // リストの中身、ローダーのカウント、スクロールリスナーをリセット
        mList.refresh();
        adapter.notifyDataSetChanged();

        MainActivity.this.count = 0;
        MainActivity.this.noMoreLoading = false;
        mScrollListener.refresh();

        // 一覧を取得し直す
        startLoading();
    }

    private void setNewQuery(String query) {
        // 検索URLをセット
        mList.setFirseUrl(Property.getFirstUrl(query));
        // サブタイトルにタグを表示
        getSupportActionBar().setSubtitle("#"+query);
        // savedInstanseState に保存するために query を保管
        this.query = query;

        // 検索を始めからやり直す
        refresh();
    }

    public void startLoading(){

        if (this.noMoreLoading || this.isLoading)
            return;
        if (this.mList.getNextUrl() == null || this.mList.getNextUrl().length() == 0) {
            // next_url がセットされていない場合これ以上の画像は見つからない
            this.noMoreLoading = true;
            Toast.makeText(this, "これ以上画像は見つかりません。", Toast.LENGTH_SHORT).show();
            return;
        }

        // データ取得中でなければ、データを取得
        // 非同期でデータを取得
        this.count++;
        getLoaderManager().restartLoader(this.count, null, this);
        LogUtil.d(TAG, "restartLoader id=" + Integer.toString(this.count));
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        LogUtil.d(TAG, "onCreateLoader id="+Integer.toString(id));
        this.isLoading = true;
        // Instagram API へリクエストを投げる
        HttpAsyncLoader loader = new HttpAsyncLoader(this, this.mList.getNextUrl());
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        LogUtil.d(TAG, "onLoadFinished onCreateLoader id="+Integer.toString(loader.getId()));

        // スワイプのくるくるを非表示
        mSwipeRefreshLayout.setRefreshing(false);

        if (data == null) {
            // データがなかった場合、ネットワークエラー
            Toast.makeText(this, "ネットワークエラー", Toast.LENGTH_SHORT).show();
            this.noMoreLoading = true;
            this.isLoading = false;
            return;
        }
        // Json文字列を変換してリストに保存
        parseInstagramJson.loadJson(data);
        // recyclerViewの表示を更新
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // タグ名を保存
        outState.putString(MainActivity.KEY_QUERY, this.query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setNewQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

}
