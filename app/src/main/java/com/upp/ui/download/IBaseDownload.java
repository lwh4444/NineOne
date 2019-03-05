package com.upp.ui.download;

import com.upp.data.model.UnLimit91PornItem;
import com.upp.ui.favorite.IBaseFavorite;

/**
 * @author flymegoc
 * @date 2017/11/26
 * @describe
 */

public interface IBaseDownload extends IBaseFavorite{
    void downloadVideo(UnLimit91PornItem unLimit91PornItem);
}
