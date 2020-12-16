package com.framelibrary.bean.select.select_popdata;


import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.Gson;

import java.util.List;

/**
 * SelectPopWindow选择器实体数据¬
 *
 * @author wangweixu
 * @Date 2020年12月15日13:26:46
 * @Filed id 根的唯一标识符 必须传递,为了避免数据错乱!!!
 * @Filed description 根的描述 随意 可不传,如果传递则description作为数据选择器的title显示
 * @see "基于'com.contrarywind:Android-PickerView:4.1.9' 实现"
 * <p>
 * 第一层Bean 为根,根中的集合为子集依次类推
 */
public class SelectPopDataBean implements IPickerViewData {
    private String id;
    private String name;
    private String parentId;
    private String description;
    private String others;
    private List<SelectPopDataBean> childListBean;

    public SelectPopDataBean(String id, String name, List<SelectPopDataBean> childListBean) {
        this.id = id;
        this.name = name;
        this.childListBean = childListBean;
    }

    public SelectPopDataBean(String id, String name, String description, List<SelectPopDataBean> childListBean) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.childListBean = childListBean;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public List<SelectPopDataBean> getChildListBean() {
        return childListBean;
    }

    public void setChildListBean(List<SelectPopDataBean> childListBean) {
        this.childListBean = childListBean;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过getPickerViewText方法获取字符串显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
