package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

/**
 * Created by hjn on 2017/1/3.
 */

public class WxPayResponse implements IBaseResponse {
    public String appid;
    public String partnerid;
    public String prepayid;
    //    public String package;
    public String noncestr;
    public String timestamp;
    public String sign;
}
