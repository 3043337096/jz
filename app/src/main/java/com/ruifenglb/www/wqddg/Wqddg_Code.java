package com.ruifenglb.www.wqddg;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.Wqddg_items;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Wqddg_Code {

    @GET(ApiConfig.getStart)
    Observable<Wqddg_items> getStartBean();
}
