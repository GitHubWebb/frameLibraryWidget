package com.framelibrary.util.select.selectdata;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.framelibrary.bean.select.select_popdata.SelectPopDataBean;
import com.framelibrary.util.StringUtils;
import com.framelibrary.util.share.DeviceDataShare;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 级联动数据选择器
 *
 * @author wangweixu
 * @Link "https://github.com/Bigkoo/Android-PickerView/wiki/%E4%B8%AD%E6%96%87%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3%EF%BC%884.x%E7%89%88%EF%BC%89"
 * @Date 2020年12月14日14:26:23
 */
public class SelectPopWindowLevelLinkageData {
    private static Context mContext;
    private static int mOptions1 = 1; // 设置默认初始三级选中项
    private static int mOptions2 = 1;
    private static int mOptions3 = 1;
    private static List<SelectPopDataBean> options1Items = null;
    private static List<List<SelectPopDataBean>> options2Items = null;
    private static List<List<List<SelectPopDataBean>>> options3Items = null;

    /**
     * 注意œœœ 使用该方法传递的实体Bean必须为SelectPopDataBean
     * 显示选择数据的PopWindow
     * 条件选择器	联动数据需要调用此方法添加数据源
     *
     * @param popDataBean 数据源
     * @param tvOptions   需要显示的控件
     */
    public static void showSelectDataPicker(Context context, boolean linkage, final TextView tvOptions, String separator, SelectPopDataBean popDataBean) {
        mContext = context;

        if (mContext == null || tvOptions == null)
            return;
        if (StringUtils.isBlank(separator))
            return;
        if (popDataBean == null || StringUtils.isBlank(popDataBean.getId()) ||
                popDataBean.getChildListBean() == null || popDataBean.getChildListBean().isEmpty())
            return;

        OptionsPickerBuilder optionsPickerBuilder = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                JsonObject putSPJO = new JsonObject();
                putSPJO.addProperty("options1", options1);
                putSPJO.addProperty("options2", options2);
                putSPJO.addProperty("options3", options3);

                /**
                 * 将当前选中的进度保存下来用于下次展示时候设置默认值,
                 * 在使用选择器的时候,通常意味着要像后台传递选中的参数,但可能传递的是选中参数对应的值,例如 code:123,name:测试,选中测试,要把123传递给后台
                 * 所以把当前选中下标保存下来
                 */
                DeviceDataShare.getInstance().setStringValueByKey(popDataBean.getId(), putSPJO.toString());

                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items == null ? "" : options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        separator + options2Items.get(options1).get(options2).getName() : "";

                String opt3tx = options3Items == null ? "" : options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        separator + options3Items.get(options1).get(options2).get(options3).getName() : "";

                String tx = opt1tx + opt2tx + opt3tx;
                tvOptions.setText(tx);

            }
        })
                .setTitleText(popDataBean.getDescription())
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20);

        String tvStr = tvOptions.getText() == null ? "" : tvOptions.getText().toString(); // 获取TextView上面的文本值
        String[] splitDataArr = tvStr.split(separator); // 将文本值根据分割符进行拆分用于跟三个数组集合进行比对

        // 获取存储的选择器上一次选择的下标,JSON格式
        String valueByKeyJOStr = DeviceDataShare.getInstance().getStringValueByKey(popDataBean.getId());
        JsonObject cacheSelectPositionByJO = new Gson().fromJson(valueByKeyJOStr, JsonObject.class);

        mOptions1 = 1;
        mOptions2 = 1;
        mOptions3 = 1;
        boolean isSetOptions1 = false, isSetOptions2 = false, isSetOptions3 = false; // 默认没有进行设置初始化默认值

        if (cacheSelectPositionByJO != null) {

            mOptions1 = !cacheSelectPositionByJO.has("options1") ?
                    mOptions1 : cacheSelectPositionByJO.get("options1").getAsInt();
            mOptions2 = !cacheSelectPositionByJO.has("options2") ?
                    mOptions2 : cacheSelectPositionByJO.get("options2").getAsInt();
            mOptions3 = !cacheSelectPositionByJO.has("options3") ?
                    mOptions3 : cacheSelectPositionByJO.get("options3").getAsInt();
        }

        options1Items = popDataBean.getChildListBean();

        for (int i = 0; i < options1Items.size(); i++) {//遍历所有一级列表数据
            SelectPopDataBean oneChildataBean = options1Items.get(i);

            // 有选择器初始化已经有一级情况,则需要替换一级默认值
            if (splitDataArr != null && splitDataArr.length >= 1)
                if (StringUtils.isEquals(oneChildataBean.getName(), splitDataArr[0]) && !isSetOptions1) {
                    mOptions1 = i;
                    isSetOptions1 = true;
                }

            List<SelectPopDataBean> twoChildDataBeanList = oneChildataBean.getChildListBean(); // 该一级下的所有数据（第二级）
            // 如果第一条就不存在二级数据的话,那二级列表也没必要存在了
            if (twoChildDataBeanList == null) {
                options2Items = null;
                options3Items = null;
                break;
            } else options2Items = options2Items != null ? options2Items : new ArrayList<>();

            // 将该一级对应对的所有二级数据添加到二级数据列表
            if (options2Items != null)
                options2Items.add(twoChildDataBeanList);

            List<List<SelectPopDataBean>> threeChildDataBeans = new ArrayList<>(); // 存储该二级下的所有三级数据
            // 再根据当前一级列表的二级列表集合,取出每一条二级列表对应的三级列表数据集
            for (int j = 0; j < twoChildDataBeanList.size(); j++) {
                SelectPopDataBean twoChildDataBean = twoChildDataBeanList.get(j);

                if (splitDataArr != null && splitDataArr.length >= 2)
                    if (StringUtils.isEquals(twoChildDataBean.getName(), splitDataArr[1]) && !isSetOptions2) {
                        mOptions2 = j;
                        isSetOptions2 = true;
                    }

                List<SelectPopDataBean> threeChildDataBeanList = twoChildDataBean.getChildListBean(); // 该二级下的所有数据（第二级）
                // 同理,如果第一条就不存在三级数据的话,那三级列表也没必要存在了
                if (threeChildDataBeanList == null) {
                    options3Items = null;
                    break;
                } else
                    options3Items = options3Items != null ? options3Items : new ArrayList<>();

                if (splitDataArr != null && splitDataArr.length >= 3)
                    for (int k = 0; k < threeChildDataBeanList.size(); k++) {
                        SelectPopDataBean threeItemPopList = threeChildDataBeanList.get(k);
                        if (StringUtils.isEquals(threeItemPopList.getName(), splitDataArr[2]) && !isSetOptions3) {
                            mOptions3 = k;
                            isSetOptions3 = true;
                            break;
                        }

                    }
                // 将当前二级下所有三级数据整合成对应二级的一条子项
                threeChildDataBeans.add(threeChildDataBeanList);
            }

            // 把合并后的对应二级对的三级子项存储,转换为三级列表s
            if (options3Items != null)
                options3Items.add(threeChildDataBeans);
        }

        // 第二级都不存在 第三极更没必要存在
        if (options2Items == null)
            options3Items = null;

        optionsPickerBuilder
                .setSelectOptions(mOptions1, mOptions2, mOptions3); // 设置默认选中项

        OptionsPickerView pvOptions = optionsPickerBuilder.build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        if (linkage)
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        else
            pvOptions.setNPicker(options1Items, options2Items, options3Items);//三级选择器

        pvOptions.show();
    }
}