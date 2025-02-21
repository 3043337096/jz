package com.ruifenglb.www.pip;

import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.dueeeke.videoplayer.controller.ControlWrapper;

public interface PipIControlComponent {
    void attach(@NonNull ControlWrapper controlWrapper);

    View getView();

    void show(Animation showAnim);

    void hide(Animation hideAnim);

    void onPlayStateChanged(int playState);

    void onPlayerStateChanged(int playerState);

    void adjustView(int orientation, int space);

    void setProgress(int duration, int position);

    void onLockStateChanged(boolean isLocked);
}
