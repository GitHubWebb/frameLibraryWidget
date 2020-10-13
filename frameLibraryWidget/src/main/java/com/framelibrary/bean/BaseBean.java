package com.framelibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 实体基类
 */

public class BaseBean implements Serializable {
    private List<BaseBean> baseBeans ;

    public BaseBean() {
    }

    public BaseBean(List<BaseBean> baseBeans) {
        this.baseBeans = baseBeans;
    }

    public List<BaseBean> getBaseBeans() {
        return baseBeans;
    }

    public void setBaseBeans(List<BaseBean> baseBeans) {
        this.baseBeans = baseBeans;
    }
}
