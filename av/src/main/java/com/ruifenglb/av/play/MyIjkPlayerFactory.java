package com.ruifenglb.av.play;

import android.content.Context;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.AbstractPlayer;

public class MyIjkPlayerFactory extends IjkPlayerFactory {

    public static MyIjkPlayerFactory create(){
        return new MyIjkPlayerFactory();
    }
    @Override
    public IjkPlayer createPlayer(Context context) {

        return new MyIjkPlayer(context);
    }

}
