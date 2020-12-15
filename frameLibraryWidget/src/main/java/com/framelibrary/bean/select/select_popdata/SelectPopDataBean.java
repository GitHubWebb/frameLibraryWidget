package com.framelibrary.bean.select.select_popdata;


import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.Gson;

/**
 * SelectPopWindow选择器实体数据¬
 *
 * @author wangweixu
 * @Date 2020年12月15日13:26:46
 * @see "基于'com.contrarywind:Android-PickerView:4.1.9' 实现"
 */
public class SelectPopDataBean implements IPickerViewData {
    private String id;
    private String name;
    private String parentId;
    private String description;
    private String others;

    public SelectPopDataBean(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public SelectPopDataBean(String id, String name, String description, String others) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.others = others;
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
