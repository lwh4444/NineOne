package com.upp.ui.index

import android.util.Log
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.orhanobut.logger.Logger
import com.upp.MyApplication
import com.upp.data.model.UnLimit91PornItem
import com.upp.ui.favorite.FavoritePresenter
import com.upp.utils.CallBackWrapper
import com.upp.utils.ParseUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.rx_cache2.EvictProvider
import io.rx_cache2.Source
import okhttp3.internal.cache.CacheStrategy

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

class IndexPresenter : MvpBasePresenter<IndexView>(), IIndex {
    private val mNoLimit91PornServiceApi = MyApplication.getInstace().noLimit91PornService
    private val cacheProviders = MyApplication.getInstace().cacheProviders
    private var favoritePresenter: FavoritePresenter? = null

    /**
     * 加载首页视频数据
     *
     * @param pullToRefresh 是否刷新
     */
    override fun loadIndexData(pullToRefresh: Boolean) {
        val indexPhpObservable = mNoLimit91PornServiceApi.indexPhp()
        cacheProviders.getIndexPhp(indexPhpObservable, EvictProvider(pullToRefresh))
                .compose(view.bindView())
                .map { responseBodyReply ->
                    when (responseBodyReply.source) {
                        Source.CLOUD -> Logger.d("数据来自：网络   是否加密：" + responseBodyReply.isEncrypted)
                        Source.MEMORY -> Logger.d("数据来自：内存   是否加密：" + responseBodyReply.isEncrypted)
                        Source.PERSISTENCE -> Logger.d("数据来自：磁盘缓存   是否加密：" + responseBodyReply.isEncrypted)
                        else -> {
                        }
                    }
                    responseBodyReply.data
                }
                .map { s ->
                    System.out.println("911s$s")
                    ParseUtils.parseIndex(s)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CallBackWrapper<List<UnLimit91PornItem>>() {
                    override fun onBegin(d: Disposable) {
                        if (isViewAttached && !pullToRefresh) {
                            view.showLoading(pullToRefresh)
                        }
                    }

                    override fun onSuccess(unLimit91PornItems: List<UnLimit91PornItem>) {
                        if (isViewAttached) {
                            view.setData(unLimit91PornItems)
                            view.showContent()
                        }
                    }

                    override fun onError(msg: String, code: Int) {
                        if (isViewAttached) {
                            loadDataForNet(pullToRefresh)
                            view.showError(Throwable(msg), false)
                        }
                    }
                })
    }

    private fun loadDataForNet(pullToRefresh: Boolean) {
        val indexPhpObservable = mNoLimit91PornServiceApi.indexPhp()
        indexPhpObservable.map { s ->
            ParseUtils.parseIndex(s)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CallBackWrapper<List<UnLimit91PornItem>>() {
                    override fun onBegin(d: Disposable) {
                        if (isViewAttached && !pullToRefresh) {
                            view.showLoading(pullToRefresh)
                        }
                    }

                    override fun onSuccess(unLimit91PornItems: List<UnLimit91PornItem>) {
                        if (isViewAttached) {
                            view.setData(unLimit91PornItems)
                            view.showContent()
                        }
                    }

                    override fun onError(msg: String, code: Int) {
                        if (isViewAttached) {
                            view.showError(Throwable("NET_LOAD$msg"), false)
                        }
                    }
                })
    }

    override fun favorite(unLimit91PornItem: UnLimit91PornItem) {
        if (favoritePresenter == null) {
            favoritePresenter = FavoritePresenter()
        }
        favoritePresenter!!.favorite(unLimit91PornItem, object : FavoritePresenter.FavoriteListener {
            override fun onSuccess(message: String) {
                if (isViewAttached) {
                    view.showMessage(message)
                }
            }

            override fun onError(message: String) {
                if (isViewAttached) {
                    view.showMessage(message)
                }
            }
        })
    }
}
