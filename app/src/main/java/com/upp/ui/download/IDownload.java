package com.upp.ui.download;

import com.upp.data.model.UnLimit91PornItem;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IDownload extends IBaseDownload {
    void downloadVideo(UnLimit91PornItem unLimit91PornItem, DownloadPresenter.DownloadListener downloadListener);

    void loadDownloadingData();

    void loadFinishedData();

    void deleteDownloadingTask(UnLimit91PornItem unLimit91PornItem);

    void deleteDownloadedTask(UnLimit91PornItem unLimit91PornItem, boolean isDeleteFile);
}
