package com.upp.ui.play;

import android.annotation.SuppressLint;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;
import com.upp.MyApplication;
import com.upp.data.NoLimit91PornServiceApi;
import com.upp.data.model.UnLimit91PornItem;
import com.upp.ui.download.DownloadPresenter;
import com.upp.ui.favorite.FavoritePresenter;
import com.upp.utils.BoxQureyHelper;
import com.upp.utils.CallBackWrapper;
import com.upp.utils.Constants;
import com.upp.utils.ParseUtils;
import com.upp.utils.RandomIPAdderssUtils;

import io.objectbox.Box;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

public class PlayVideoPresenter extends MvpBasePresenter<PlayVideoView> implements IPlay {

    private NoLimit91PornServiceApi mNoLimit91PornServiceApi = MyApplication.getInstace().getNoLimit91PornService();
    private FavoritePresenter favoritePresenter;
    private DownloadPresenter downloadPresenter;

    @Override
    public void loadVideoUrl(String viewKey) {

        String ip = RandomIPAdderssUtils.getRandomIPAdderss();
        MyApplication.getInstace().getCacheProviders().getVideoPlayPage(mNoLimit91PornServiceApi.getVideoPlayPage(viewKey, ip), new DynamicKey(viewKey), new EvictDynamicKey(false))
                .compose(getView().bindView())
                .map(new Function<Reply<String>, String>() {
                    @Override
                    public String apply(Reply<String> responseBodyReply) throws Exception {
                        switch (responseBodyReply.getSource()) {
                            case CLOUD:
                                Logger.d("数据来自：网络");
                                break;
                            case MEMORY:
                                Logger.d("数据来自：内存");
                                break;
                            case PERSISTENCE:
                                Logger.d("数据来自：磁盘缓存");
                                break;
                            default:
                                break;
                        }
                        return responseBodyReply.getData();
                    }
                }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                System.out.println("sucess:" + s);
                return getH5ShareUrl(ParseUtils.parseVideoH5ShareUrl(s));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CallBackWrapper<String>() {
            @Override
            public void onBegin(Disposable d) {
                if (isViewAttached()) {
                    getView().showParsingDialog();
                }
            }

            @Override
            public void onSuccess(String s) {
                MyApplication.getInstace().cleanCookies();
                if (isViewAttached()) {
//                    getView().playVideo(s);
                }
            }

            @Override
            public void onError(String msg, int code) {
                if (isViewAttached()) {
                    getView().errorParseVideoUrl(msg + " code:" + code);
                }
            }
        });
    }

    @Override
    public void saveVideoUrl(String videoUrl, UnLimit91PornItem unLimit91PornItem) {
        Box<UnLimit91PornItem> unLimit91PornItemBox = MyApplication.getInstace().getBoxStore().boxFor(UnLimit91PornItem.class);
        UnLimit91PornItem tmp = BoxQureyHelper.findByViewKey(unLimit91PornItem.getViewKey());
        if (tmp == null) {
            unLimit91PornItem.setFavorite(UnLimit91PornItem.FAVORITE_NO);
            unLimit91PornItem.setVideoUrl(videoUrl);
            unLimit91PornItemBox.put(unLimit91PornItem);
        } else {
            tmp.setVideoUrl(videoUrl);
            unLimit91PornItemBox.put(tmp);
        }
    }

    @Override
    public void downloadVideo(UnLimit91PornItem unLimit91PornItem) {
        if (downloadPresenter == null) {
            downloadPresenter = new DownloadPresenter();
        }
        downloadPresenter.downloadVideo(unLimit91PornItem, new DownloadPresenter.DownloadListener() {
            @Override
            public void onSuccess(String message) {
                if (isViewAttached()) {
                    getView().showMessage(message);
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().showMessage(message);
                }
            }
        });
    }

    @Override
    public void favorite(UnLimit91PornItem unLimit91PornItem) {
        if (favoritePresenter == null) {
            favoritePresenter = new FavoritePresenter();
        }
        favoritePresenter.favorite(unLimit91PornItem, new FavoritePresenter.FavoriteListener() {
            @Override
            public void onSuccess(String message) {
                if (isViewAttached()) {
                    getView().showMessage(message);
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().showMessage(message);
                }
            }
        });
    }


    private String getH5ShareUrl(String urlStr) {
        final String[] result = {""};
        String ip = RandomIPAdderssUtils.getRandomIPAdderss();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        mNoLimit91PornServiceApi = retrofit.create(NoLimit91PornServiceApi.class);
        mNoLimit91PornServiceApi.getVideoUrl(urlStr, ip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CallBackWrapper<String>() {
            @Override
            public void onBegin(Disposable d) {
            }

            @Override
            public void onSuccess(String s) {
                getView().playVideo(ParseUtils.parseVideoPlayUrl(s));
                result[0] = ParseUtils.parseVideoPlayUrl(s);
            }

            @Override
            public void onError(String msg, int code) {
                System.out.println(msg);
            }
        });
        return result[0];
    }

}
