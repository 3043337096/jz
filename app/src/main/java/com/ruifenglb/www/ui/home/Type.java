package com.ruifenglb.www.ui.home;

import java.io.Serializable;

import com.ruifenglb.www.bean.ExtendBean;

public interface Type extends Serializable {

    String getTypeName();

    int getTypeId();

    ExtendBean getExtend();
}
