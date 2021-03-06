package com.gs.buluo.store.presenter;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.OrderBean;
import com.gs.buluo.store.bean.RequestBodyBean.LogisticsRequestBody;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.OrderResponseBean;
import com.gs.buluo.store.network.OrderApis;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.view.impl.IOrderView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderPresenter extends BasePresenter<IOrderView> {
    private String status;
    private String nextSkip;
    public void getOrderListFirst(int pos) {
        setStatus(pos);
        TribeRetrofit.getInstance().createApi(OrderApis.class).getOrderFirst(TribeApplication.getInstance().getUserInfo().getId(), 20, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderResponseBean>>(false) {
                    @Override
                    public void onNext(BaseResponse<OrderResponseBean> goodListBaseResponse) {
                        nextSkip = goodListBaseResponse.data.nextSkip;
                        if (isAttach()) mView.getOrderInfoSuccess(goodListBaseResponse.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(R.string.connect_fail,e.getMessage());
                    }
                });
    }

    public void setStatus(int pos) {
        switch (pos) {
            case 0:
                status = null;
                break;
            case 1:
                status = OrderBean.OrderStatus.NO_SETTLE.name();
                break;
            case 2:
                status = OrderBean.OrderStatus.SETTLE.name();
                break;
            case 3:
                status = OrderBean.OrderStatus.DELIVERY.name();
                break;
            case 4:
                status = OrderBean.OrderStatus.RECEIVED.name();
                break;
        }
    }

    public void getOrderListMore() {
        TribeRetrofit.getInstance().createApi(OrderApis.class).getOrder(TribeApplication.getInstance().getUserInfo().getId(), 20, status, nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderResponseBean>>(false) {
                    @Override
                    public void onNext(BaseResponse<OrderResponseBean> goodListBaseResponse) {
                        nextSkip = goodListBaseResponse.data.nextSkip;
                        if (isAttach()) mView.getOrderInfoSuccess(goodListBaseResponse.data);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showError(R.string.connect_fail,e.getMessage());
                    }
                });
    }

    public void updateOrderStatus(String orderId, String num, String way, String status) {
        TribeRetrofit.getInstance().createApi(OrderApis.class).updateOrderToSend(orderId, TribeApplication.getInstance().getUserInfo().getId(),
                new LogisticsRequestBody(num, way, status))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderBean>>() {
                    @Override
                    public void onNext(BaseResponse<OrderBean> goodListBaseResponse) {
                        if (isAttach()) mView.updateSuccess(goodListBaseResponse.data);
                    }
                });
    }

    public void getOrderDetail(String orderId) {
        TribeRetrofit.getInstance().createApi(OrderApis.class).getOrderDetail(orderId,TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderBean>>() {
                    @Override
                    public void onNext(BaseResponse<OrderBean> orderBeanBaseResponse) {
                        mView.getOrderDetailSuccess(orderBeanBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode(),"获取订单详情失败");
                    }
                });
    }
}
