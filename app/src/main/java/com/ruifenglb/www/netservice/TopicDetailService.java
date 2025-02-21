package com.ruifenglb.www.netservice;

import com.ruifenglb.www.ApiConfig;
import com.ruifenglb.www.bean.BaseResult;
import com.ruifenglb.www.bean.TopicDetailBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopicDetailService {
    @GET(ApiConfig.getTopicDetail)
    Observable<BaseResult<TopicDetailBean>> getTopicDetail(@Query("topic_id") String topic_id);
}
