package com.upp.ui.play;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.helper.loadviewhelper.help.OnLoadViewListener;
import com.helper.loadviewhelper.load.LoadViewHelper;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.upp.MyApplication;
import com.upp.R;
import com.upp.data.model.UnLimit91PornItem;
import com.upp.ui.MvpActivity;
import com.upp.utils.BoxQureyHelper;
import com.upp.utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.rx_cache2.Reply;

/**
 * @author flymegoc
 */
public class PlayVideoActivity extends MvpActivity<PlayVideoView, PlayVideoPresenter> implements PlayVideoView {

    private final String TAG = PlayVideoActivity.class.getSimpleName();

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard jzVideoPlayerStandard;
    @BindView(R.id.fl_comment)
    FrameLayout flComment;
    @BindView(R.id.tips)
    TextView tips;

    private AlertDialog mAlertDialog;
    HttpProxyCacheServer proxy = MyApplication.getInstace().getProxy();
    private LoadViewHelper helper;

    private UnLimit91PornItem unLimit91PornItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        unLimit91PornItem = (UnLimit91PornItem) getIntent().getSerializableExtra(Keys.KEY_INTENT_UNLIMIT91PORNITEM);

        if (unLimit91PornItem == null) {
            showMessage("参数错误，无法解析");
            tips.setVisibility(View.GONE);
            return;
        }
        setTitle(unLimit91PornItem.getTitle());
        initProgressDialog();

        helper = new LoadViewHelper(flComment);
        helper.setListener(new OnLoadViewListener() {
            @Override
            public void onRetryClick() {
                presenter.loadVideoUrl(unLimit91PornItem.getViewKey());
            }
        });

        String videoUrl = BoxQureyHelper.getVideoUrlByViewKey(unLimit91PornItem.getViewKey());
        tips.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(videoUrl)) {
            presenter.loadVideoUrl(unLimit91PornItem.getViewKey());

        } else {
            Logger.t(TAG).d("使用已有播放地址");
            showMessage("使用已有播放地址,点击播放");
            playVideo(unLimit91PornItem.getTitle(), videoUrl, "", "");
        }
//        tips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.downloadVideo(unLimit91PornItem);
//            }
//        });
    }

    private void initProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.LoadingDialogStyle);
        builder.setView(R.layout.loading_layout);
        mAlertDialog = builder.create();
        mAlertDialog.setCanceledOnTouchOutside(false);
    }

    private void playVideo(String title, String videoUrl, String name, String thumImgUrl) {
        String proxyUrl = proxy.getProxyUrl(videoUrl);
        jzVideoPlayerStandard.setUp(proxyUrl, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);
        if (!TextUtils.isEmpty(thumImgUrl)) {
            jzVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse(thumImgUrl));
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


    @NonNull
    @Override
    public PlayVideoPresenter createPresenter() {
        return new PlayVideoPresenter();
    }

    @Override
    public void showParsingDialog() {
        if (mAlertDialog == null) {
            initProgressDialog();
        }
        mAlertDialog.show();
    }

    @Override
    public void playVideo(String videoUrl) {
        dismissDialog();
        tips.setText("解析成功:" + unLimit91PornItem.getViewKey());
        showMessage("解析成功，点击播放");
        presenter.saveVideoUrl(videoUrl, unLimit91PornItem);
        helper.showContent();
        playVideo(unLimit91PornItem.getTitle(), videoUrl, "", "");
    }

    @Override
    public void errorParseVideoUrl(String errorMessage) {
        dismissDialog();
        helper.showError();
        showMessage(errorMessage);
    }

    @Override
    public String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public LifecycleTransformer<Reply<String>> bindView() {
        return bindToLifecycle();
    }

    @Override
    public void showMessage(String msg) {
        super.showMessage(msg);
    }

    /**
     * 是否是用户主动取消
     */
    private void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing() && !isFinishing()) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playvideo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_play_collect) {
            presenter.favorite(unLimit91PornItem);
            return true;
        } else if (id == R.id.menu_play_download) {
            presenter.downloadVideo(unLimit91PornItem);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
