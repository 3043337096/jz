package com.ruifenglb.www.base;

public interface BaseView<T> {

    void initView();

    void setPresenter(T presenter);

}
