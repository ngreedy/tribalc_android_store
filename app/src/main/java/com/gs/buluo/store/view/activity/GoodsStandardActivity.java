package com.gs.buluo.store.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.adapter.StandardListAdapter;
import com.gs.buluo.store.bean.GoodsCategory;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.GoodsStandardMeta;
import com.gs.buluo.store.bean.ListGoodsDetail;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.store.network.GoodsService;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/1/20.
 */
public class GoodsStandardActivity extends BaseActivity implements Callback<BaseResponse<GoodsStandardResponse>> {
    @Bind(R.id.good_standard_list)
    RefreshRecyclerView refreshRecyclerView;

    StandardListAdapter listAdapter;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        final String category = getIntent().getStringExtra(Constant.ForIntent.GOODS_CATEGORY);
        findViewById(R.id.goods_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsStandardActivity.this, NewGoodsActivity.class);
                intent.putExtra(Constant.ForIntent.GOODS_CATEGORY,category);
                startActivity(intent);
            }
        });
        listAdapter=new StandardListAdapter(this);
        refreshRecyclerView.setAdapter(listAdapter);
        TribeRetrofit.getInstance().createApi(GoodsService.class).getStandardList(TribeApplication.getInstance().getUserInfo().getId(),category)
        .enqueue(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_standard_choose;
    }

    @Override
    public void onResponse(Call<BaseResponse<GoodsStandardResponse>> call, Response<BaseResponse<GoodsStandardResponse>> response) {
        if (response!=null&&response.body()!=null&&response.body().code==200){
            List<GoodsStandardMeta> content = response.body().data.content;
            listAdapter.addAll(content);
        }else {
            ToastUtils.ToastMessage(this,R.string.connect_fail);
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<GoodsStandardResponse>> call, Throwable t) {
        ToastUtils.ToastMessage(this,R.string.connect_fail);
    }
}
