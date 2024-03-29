package com.upp.ui.favorite;

import com.upp.data.model.UnLimit91PornItem;

/**
 * @author flymegoc
 * @date 2017/11/28
 * @describe
 */

public interface IFavorite extends IBaseFavorite {
    void loadFavoriteData(int skip, int pageSize);

    void deleteFavorite(int position, UnLimit91PornItem unLimit91PornItem);

    void exportData(boolean onlyUrl);
}
