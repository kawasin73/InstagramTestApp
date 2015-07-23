package jp.co.zanon.instagramtestapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.image_view)
    SquaredImageView mImageView;
    @Bind(R.id.text)
    TextView mTextView;

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
        if (intent == null)
            return;
        if (intent.hasExtra("url")){
            // 画像の表示
            String url = intent.getStringExtra("url");
            Picasso.with(this).load(url).into(mImageView);
        }
        if (intent.hasExtra("msg") && intent.getStringExtra("msg").length() > 0 ){
            // テキスト文の表示
            String msg = intent.getStringExtra("msg");
            mTextView.setText(msg);
        } else {
            // テキストがない場合は、テキストビューを見えなくする
            mTextView.setVisibility(View.INVISIBLE);
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
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
