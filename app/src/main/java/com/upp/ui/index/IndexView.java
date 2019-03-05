package com.upp.ui.index;

import com.upp.data.model.UnLimit91PornItem;
import com.upp.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

public interface IndexView extends BaseView {

    void loadData(boolean pullToRefresh);

    void setData(List<UnLimit91PornItem> data);
}
