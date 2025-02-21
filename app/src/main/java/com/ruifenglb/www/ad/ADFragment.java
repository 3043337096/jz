package com.ruifenglb.www.ad;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.ad.biddingsdk.AdConstant;
import com.app.ad.biddingsdk.AdUtils;
import com.ruifenglb.www.R;
import com.ruifenglb.www.base.BaseMainFragment;
import com.superad.ad_lib.SuperKsContentPageAD;

public class ADFragment extends BaseMainFragment {

    Fragment contentFragment ;
    private SuperKsContentPageAD ksContentPageAD;
    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_ad;
    }
    public static ADFragment newInstance() {
        Bundle args = new Bundle();
        ADFragment fragment = new ADFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void Show(){
        if(ksContentPageAD==null){
            ksContentPageAD = AdUtils.GetsContentPageAD(getContext(), AdConstant.VIDEO_AD);
            contentFragment = ksContentPageAD.getFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.rootView,
                    contentFragment).commitAllowingStateLoss();
        }else{
            ksContentPageAD.tryToRefresh();
        }
        Log.e("test","show");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
       /* if(hidden){
            if(contentFragment!=null){
                getChildFragmentManager().beginTransaction().remove(
                        contentFragment).commitAllowingStateLoss();
                contentFragment = null;
            }
        }*/
    }


}
