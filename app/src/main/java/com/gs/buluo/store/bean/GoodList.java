package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodList{
    public String category;
    public String sort;
    public String prevSkip;
    public String nextSkip;
    public Boolean hasMore;
    public List<ListGoods> content;

}
