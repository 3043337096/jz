package com.ruifenglb.www.netservice;


import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.TypeBean;
import com.ruifenglb.www.bean.BaseResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CardMoreService {
    @GET(ApiConfig.getCardListByType)
    Observable<BaseResult<TypeBean>> getCardListByType(@Query("type_id") int type_id);
}
