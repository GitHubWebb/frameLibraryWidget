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
    private static int mOptions1 = 1; // 设置默认初始三级选中项
    private static int mOptions2 = 1;
    private static int mOptions3 = 1;
    private static List<SelectPopDataBean> options1Items = null;
    private static List<List<SelectPopDataBean>> options2Items = null;
    private static List<List<List<SelectPopDataBean>>> options3Items = null;

    /**
     * 显示选择数据的PopWindow
     * 条件选择器	不联动数据需要调用此方法添加数据源
     *
     * @param arrayListArr 数据源数组
     * @param separator    数据源之间显示分割符号
     * @param tvOptions    需要显示的控件
     */
    public static void showSelectDataNPicker(Context context, final TextView tvOptions, String separator, List<String>... arrayListArr) {
        if (context == null || tvOptions == null)
            return;
        if (StringUtils.isBlank(separator))
            return;
        if (arrayListArr.length == 0 || arrayListArr[0] == null || arrayListArr[0].isEmpty())
            return;

        OptionsPickerBuilder optionsPickerBuilder = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                StringBuffer txSB = new StringBuffer();

                switch (arrayListArr.length) {
                    case 1:
                        txSB.append(arrayListArr[0].get(options1));
                        break;
                    case 2:
                        txSB.append(arrayListArr[0].get(options1));
                        txSB.append(separator);
                        txSB.append(arrayListArr[1].get(options2));
                        break;
                    case 3:
                        txSB.append(arrayListArr[0].get(options1));
                        txSB.append(separator);
                        txSB.append(arrayListArr[1].get(options2));
                        txSB.append(separator);
                        txSB.append(arrayListArr[2].get(options3));
                        break;
                }

                tvOptions.setText(txSB.toString());
            }
        });
        optionsPickerBuilder
                .setCyclic(false, false, false); // 循环与否

        String tvStr = tvOptions.getText() == null ? "" : tvOptions.getText().toString(); // 获取TextView上面的文本值
        String[] splitData = tvStr.split(separator); // 将文本值根据分割符进行拆分用于跟三个数组集合进行比对

        switch (arrayListArr.length) {
            case 1:
                for (int i = 0; i < arrayListArr[0].size(); i++) {
                    String dataByPosition = arrayListArr[0].get(i);
                    if (StringUtils.isEquals(dataByPosition, tvStr))
                        mOptions1 = i;
                }

                optionsPickerBuilder
                        .setSelectOptions(mOptions1);  // 设置默认选中项
                break;
            case 2:
                if (splitData == null || splitData.length < 2) {
                    mOptions1 = 1;
                    mOptions2 = 1;
                } else {
                    for (int i = 0; i < arrayListArr[0].size(); i++) {
                        String dataByPosition = arrayListArr[0].get(i);
                        if (StringUtils.isEquals(dataByPosition, splitData[0]))
                            mOptions1 = i;
                    }
                    for (int j = 0; j < arrayListArr[1].size(); j++) {
                        String dataByPosition = arrayListArr[1].get(j);
                        if (StringUtils.isEquals(dataByPosition, splitData[1]))
                            mOptions2 = j;
                    }
                }

                optionsPickerBuilder
                        .setSelectOptions(mOptions1, mOptions2); // 设置默认选中项
                break;
            case 3:
                if (splitData == null || splitData.length < 3) {
                    mOptions1 = 1;
                    mOptions2 = 1;
                    mOptions3 = 1;
                } else {
                    for (int i = 0; i < arrayListArr[0].size(); i++) {
                        String dataByPosition = arrayListArr[0].get(i);
                        if (StringUtils.isEquals(dataByPosition, splitData[0]))
                            mOptions1 = i;
                    }
                    for (int j = 0; j < arrayListArr[1].size(); j++) {
                        String dataByPosition = arrayListArr[1].get(j);
                        if (StringUtils.isEquals(dataByPosition, splitData[1]))
                            mOptions2 = j;
                    }
                    for (int z = 0; z < arrayListArr[2].size(); z++) {
                        String dataByPosition = arrayListArr[2].get(z);
                        if (StringUtils.isEquals(dataByPosition, splitData[2]))
                            mOptions3 = z;
                    }
                }

                optionsPickerBuilder
                        .setSelectOptions(mOptions1, mOptions2, mOptions3); // 设置默认选中项
                break;
        }

        OptionsPickerView pvOptions = optionsPickerBuilder.build();

        switch (arrayListArr.length) {
            case 1:
                pvOptions.setPicker(arrayListArr[0]);
                break;
            case 2:
                pvOptions.setPicker(arrayListArr[0], arrayListArr[1]);
                break;
            case 3:
                pvOptions.setNPicker(arrayListArr[0], arrayListArr[1], arrayListArr[2]);
                break;
        }

        pvOptions.show();
    }

    /**
     * 显示选择数据的PopWindow
     * 条件选择器	联动数据需要调用此方法添加数据源
     *
     * @param dataBeanListArr 数据源数组
     * @param tvOptions       需要显示的控件
     */
    public static void showSelectDataPicker(Context context, final TextView tvOptions, String separator, final String titleText, List... dataBeanListArr) {
        if (context == null || tvOptions == null)
            return;
        if (StringUtils.isBlank(separator))
            return;
        if (dataBeanListArr.length == 0 || dataBeanListArr[0] == null || dataBeanListArr[0].isEmpty())
            return;

        // 三级联动默认为空
        options1Items = null;
        options2Items = null;
        options3Items = null;

        OptionsPickerBuilder optionsPickerBuilder = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
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

                .setTitleText(titleText)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20);

        String tvStr = tvOptions.getText() == null ? "" : tvOptions.getText().toString(); // 获取TextView上面的文本值
        String[] splitDataArr = tvStr.split(separator); // 将文本值根据分割符进行拆分用于跟三个数组集合进行比对

        // 根据传递的数组集合循环设置数据源和匹配当前选中项
        for (int i = 0; i < dataBeanListArr.length; i++) {
            List<Object> list = dataBeanListArr[i];
            if (list == null || list.isEmpty())
                break;

            switch (i) {
                case 0:
                    options1Items = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        Object obj = list.get(j);
                        if (obj instanceof SelectPopDataBean) {
                            SelectPopDataBean popDataBean = (SelectPopDataBean) obj;
                            if (splitDataArr != null && splitDataArr.length >= 1)
                                if (StringUtils.isEquals(popDataBean.getName(), splitDataArr[0]))
                                    mOptions1 = j;

                            options1Items.add(popDataBean);
                        }

                    }

                    break;
                case 1:
                    options2Items = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        Object obj = list.get(j);
                        if (obj instanceof List) {
                            List<SelectPopDataBean> selectPopDataBeans = (List<SelectPopDataBean>) obj;

                            if (splitDataArr != null && splitDataArr.length >= 2)
                                for (int k = 0; k < selectPopDataBeans.size(); k++) {
                                    if (StringUtils.isEquals(selectPopDataBeans.get(k).getName(), splitDataArr[1])) {
                                        mOptions2 = k;
                                        break;
                                    }
                                }

                            options2Items.add(selectPopDataBeans);
                        }
                    }

                    break;
                case 2:
                    options3Items = new ArrayList<>();
                    for (int j = 0; j < list.size(); j++) {
                        Object obj = list.get(j);
                        if (obj instanceof List) {
                            List<List<SelectPopDataBean>> selectPopDataBeans = (List<List<SelectPopDataBean>>) obj;

                            if (splitDataArr != null && splitDataArr.length >= 3)
                                for (int k = 0; k < selectPopDataBeans.size(); k++) {
                                    List<SelectPopDataBean> threeItemPopList = selectPopDataBeans.get(k);
                                    for (int l = 0; l < threeItemPopList.size(); l++) {
                                        SelectPopDataBean popDataBean = threeItemPopList.get(l);
                                        if (StringUtils.isEquals(popDataBean.getName(), splitDataArr[2])) {
                                            mOptions3 = l;
                                            break;
                                        }
                                    }
                                }

                            options3Items.add(selectPopDataBeans);
                        }
                    }

                    break;
            }
        }

        optionsPickerBuilder
                .setSelectOptions(mOptions1, mOptions2, mOptions3); // 设置默认选中项

        OptionsPickerView pvOptions = optionsPickerBuilder.build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
}