package com.framelibrary.util.select.selectdata;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.framelibrary.util.StringUtils;

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

}