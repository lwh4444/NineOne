package com.upp.ui.common;

import com.upp.data.model.UnLimit91PornItem;
import com.upp.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/16
 * @describe
 */

public interface CommonView extends BaseView {
    void loadMoreDataComplete();

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<UnLimit91PornItem> unLimit91PornItemList);

    void loadData(boolean pullToRefresh);

    void setData(List<UnLimit91PornItem> data);

}
