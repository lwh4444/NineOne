package com.upp.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.format.Formatter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.upp.R;
import com.upp.data.model.UnLimit91PornItem;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/14
 * @describe
 */

public class UnLimit91CustomAdapter extends BaseQuickAdapter<UnLimit91PornItem, BaseViewHolder> {

    public UnLimit91CustomAdapter(int layoutResId, @Nullable List<UnLimit91PornItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UnLimit91PornItem item) {
        helper.setText(R.id.tv_91porn_item_title, item.getTitle() + "  (" + item.getDuration() + ")");
        helper.setText(R.id.tv_91porn_item_info, item.getInfo());
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.iv_91porn_item_img);
        Uri uri = Uri.parse(item.getImgUrl());
        simpleDraweeView.setImageURI(uri);

        helper.setVisible(R.id.tv_91porn_item_info, true);

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
}
