package com.gs.buluo.store.model;

import com.gs.buluo.store.bean.DetailStoreSetMeal;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.ServeResponse;
import com.gs.buluo.store.network.ServeApis;
import com.gs.buluo.store.network.TribeRetrofit;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServeModel {

    public void getServeListFirst(String category, int limitSize,String sort,String coordinate, Callback<ServeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ServeApis.class).
                getServiceListFirst(category,limitSize, sort,coordinate).enqueue(callback);
    }

    public void getServeList(String category, int limitSize, String sort, String sortSkip,String coordinate, Callback<ServeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ServeApis.class).
                getServiceList(category,limitSize, sortSkip,sort,coordinate).enqueue(callback);
    }

    public void getServeListFirst(String category, int limitSize,String sort, Callback<ServeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ServeApis.class).
                getServiceListFirst(category,limitSize, sort).enqueue(callback);
    }

    public void getServeList(String category, int limitSize, String sort, String sortSkip, Callback<ServeResponse> callback) {
        TribeRetrofit.getInstance().createApi(ServeApis.class).
                getServiceList(category,limitSize, sortSkip,sort).enqueue(callback);
    }

    public void getServeDetail(String id, Callback<BaseResponse<DetailStoreSetMeal>> callback) {
        TribeRetrofit.getInstance().createApi(ServeApis.class).
                getServeDetail(id).enqueue(callback);
    }

}
