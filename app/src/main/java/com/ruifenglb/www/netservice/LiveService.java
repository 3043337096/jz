package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.LiveBean;
import com.ruifenglb.www.bean.PageResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface LiveService {
    @GET(ApiConfig.getLiveList)
    Observable<PageResult<LiveBean>> getLiveList();
}
