package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.StartBean;
import com.ruifenglb.www.bean.BaseResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface StartService {
    @GET(ApiConfig.getStart)
    Observable<BaseResult<StartBean>> getStartBean();

//    @GET(ApiConfig.getStart)
//    Observable<BaseResult<Object>> getStartBean();
}
