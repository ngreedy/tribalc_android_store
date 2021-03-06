package com.gs.buluo.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.store.bean.BillEntity;
import com.gs.buluo.store.utils.CommonUtils;
import com.gs.buluo.store.utils.GlideUtils;
import com.gs.buluo.store.utils.TribeDateUtils;
import com.gs.buluo.store.view.activity.BillDetailActivity;
import com.gs.buluo.store.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.store.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillListAdapter extends RecyclerAdapter<BillEntity> {
    private long today;
    private String currentMonth;  //当前时间
    private int lastMonth;  //上一个有变化的 月份
    Context context;

    public BillListAdapter(Context context, List<BillEntity> list) {
        super(context, list);
        this.context = context;
        today = System.currentTimeMillis();
        currentMonth = TribeDateUtils.dateFormat5(new Date(today)).split("-")[1];
    }

    @Override
    public BaseViewHolder<BillEntity> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new BillHolder(parent, R.layout.bill_list_item);
    }

    class BillHolder extends BaseViewHolder<BillEntity> {
        TextView week;
        TextView time;
        TextView money;
        TextView detail;
        TextView month;
        ImageView icon;

        public BillHolder(ViewGroup itemView, int res) {
            super(itemView, res);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            week = findViewById(R.id.bill_week);
            time = findViewById(R.id.bill_time);
            money = findViewById(R.id.bill_money);
            detail = findViewById(R.id.bill_detail);
            month = findViewById(R.id.bill_item_month);
            icon = findViewById(R.id.bill_icon);
        }

        @Override
        public void setData(BillEntity entity) {
            super.setData(entity);
            String url = Constant.Base.BASE_ONLINE_URL + entity.anotherId + "/icon.jpg";
            GlideUtils.loadImage(getContext(), url, icon, true);
            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);

            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            int w = instance.get(Calendar.DAY_OF_WEEK);

            String s = TribeDateUtils.dateFormat5(date);
            String we = CommonUtils.getWeekFromCalendar(w);
            week.setText(we);

            if (TribeDateUtils.getTimeIntervalByDay(createTime, today) < 1) {
                week.setText(R.string.today);
                time.setText(TribeDateUtils.dateFormat6(date));
            } else {
                time.setText(TribeDateUtils.dateFormat8(date));
            }

            money.setText(entity.amount);
            detail.setText(entity.title);
            String newMonth = s.split("-")[1];
            month.setText(newMonth + "月");
            if (Integer.parseInt(newMonth) != lastMonth) {
                month.setVisibility(View.VISIBLE);
                month.setText(newMonth + "月");
                lastMonth = Integer.parseInt(newMonth);
            } else {
                month.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemViewClick(BillEntity entity) {
            super.onItemViewClick(entity);
            Intent intent = new Intent(context, BillDetailActivity.class);
            intent.putExtra(Constant.BILL, entity);
            context.startActivity(intent);
        }
    }
}
