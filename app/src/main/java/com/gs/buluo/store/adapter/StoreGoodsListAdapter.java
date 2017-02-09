package com.gs.buluo.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.GoodsMeta;
import com.gs.buluo.store.bean.ResponseBody.BaseResponse;
import com.gs.buluo.store.bean.ResponseBody.CodeResponse;
import com.gs.buluo.store.network.GoodsService;
import com.gs.buluo.store.network.TribeCallback;
import com.gs.buluo.store.network.TribeRetrofit;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.ToastUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.activity.AddGoodsWithStandardActivity;
import com.gs.buluo.store.view.widget.SwipeMenuLayout;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Date;

import retrofit2.Response;

/**
 * Created by hjn on 2017/1/24.
 */
public class StoreGoodsListAdapter extends RecyclerAdapter<GoodsMeta> {
    public StoreGoodsListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<GoodsMeta> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new StoreGoodsHolder(parent);
    }


    private class StoreGoodsHolder extends BaseViewHolder<GoodsMeta> {
        public ImageView picture;
        public TextView name;
        public TextView repertory;
        public TextView price;
        public TextView saleNum;
        public TextView time;
        public SwipeMenuLayout swipeMenuLayout;

        public StoreGoodsHolder(ViewGroup parent) {
            super(parent, R.layout.store_goods_item);
        }

        @Override
        public void onInitializeView() {
            picture = findViewById(R.id.goods_item_picture);
            name = findViewById(R.id.goods_item_name);
            repertory = findViewById(R.id.goods_item_repertory);
            price = findViewById(R.id.goods_item_price);
            saleNum = findViewById(R.id.goods_item_sale);
            time = findViewById(R.id.goods_item_create_time);
            swipeMenuLayout = findViewById(R.id.goods_item_swipe);
        }

        @Override
        public void setData(final GoodsMeta entity) {
            super.setData(entity);
            picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(getContext()).load(GlideUtils.formatImageUrl(entity.mainPicture)).placeholder(R.mipmap.default_pic).into(picture);
            name.setText(entity.name);
            repertory.setText(entity.priceAndRepertory.repertory + "");
            price.setText(entity.priceAndRepertory.salePrice + "");
            saleNum.setText(100 + "");
            time.setText(TribeDateUtils.dateFormat5(new Date(entity.createTime)));

            findViewById(R.id.goods_item_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeMenuLayout.quickClose();
                    deleteGoods(entity);
                }
            });

            findViewById(R.id.goods_item_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddGoodsWithStandardActivity.class);
                    intent.putExtra(Constant.ForIntent.GOODS_BEAN,entity);
                    getContext().startActivity(intent);
                    swipeMenuLayout.quickClose();
                }
            });
        }
    }

    private void deleteGoods(final GoodsMeta entity) {
        TribeRetrofit.getInstance().createApi(GoodsService.class).deleteGoods(entity.id).enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                remove(entity);
                ToastUtils.ToastMessage(getContext(),R.string.delete_success);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(getContext(),R.string.delete_fail);
            }
        });

    }
}
