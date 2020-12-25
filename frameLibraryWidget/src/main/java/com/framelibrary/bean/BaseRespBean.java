package com.framelibrary.bean;

import com.framelibrary.util.StringUtils;
import com.framelibrary.util.request.RequestUtils;

/**
 *
 */

public class BaseRespBean extends BaseBean {

    protected int errcode; //0 成功 1 失败 99 accesstoken 过期
    protected String msg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToastErrmsg() {
        if (StringUtils.isEmpty(msg)) {
            return errcode + "";
        }
        return msg;
    }

    public boolean isRequestSuccess() {
        return RequestUtils.isRequestSuccess(errcode + "");
    }


}
