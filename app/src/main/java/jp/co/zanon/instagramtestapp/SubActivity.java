package jp.co.zanon.instagramtestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.image_view)
    SquaredImageView mImageView;
    @Bind(R.id.text)
    TextView mTextView;
    @Bind(R.id.prog_bar)
    ProgressBar mProgressBar;

    private boolean isMsgSet = false;
    private boolean isTextShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        ButterKnife.bind(this);

        // ツールバーの設定
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // 表示情報をセット
        setExtras(getIntent());

    }

    private void setExtras(Intent intent) {
        if (intent == null) {
            Toast.makeText(this, "システムエラー", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "intent is not found");
            return;
        }

        if (intent.hasExtra("url")) {
            // プログレスバーの表示
            mProgressBar.setVisibility(View.VISIBLE);
            // 画像の表示
            String url = intent.getStringExtra("url");
            Picasso.with(this).load(url).into(this.mImageView, new ImageLoadedCallback(this, mProgressBar));
        }
        if (intent.hasExtra("msg") && intent.getStringExtra("msg").length() > 0) {
            this.isMsgSet = true;
            this.isTextShow = true;
            // テキスト文の表示
            String msg = intent.getStringExtra("msg");
            this.mTextView.setText(msg);
        } else {
            this.isMsgSet = false;
            this.isTextShow = false;
            // テキストがない場合は、テキストビューを見えなくする
            this.mTextView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.sub_layout)
    void toggleText() {
        //画面がタップされた時

        // メッセージがセットされていない場合はテキストは常に非表示
        if (!isMsgSet)
            return;

        // テキストビューの表示・非表示を切り替える。
        if (isTextShow) {
            mTextView.setVisibility(View.INVISIBLE);
            isTextShow = false;
        } else {
            mTextView.setVisibility(View.VISIBLE);
            isTextShow = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
