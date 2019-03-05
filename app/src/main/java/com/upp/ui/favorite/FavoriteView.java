package com.upp.ui.favorite;

import com.upp.data.model.UnLimit91PornItem;
import com.upp.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/25
 * @describe
 */

public interface FavoriteView extends BaseView {
    void setFavoriteData(List<UnLimit91PornItem> unLimit91PornItemList);

    void deleteFavoriteSucc(int position);

    void noLoadMoreData();
}
