package com.upp.ui.play;

import com.upp.ui.BaseView;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

public interface PlayVideoView extends BaseView {
    void showParsingDialog();

    void playVideo(String videoUrl);

    void errorParseVideoUrl(String errorMessage);
}
