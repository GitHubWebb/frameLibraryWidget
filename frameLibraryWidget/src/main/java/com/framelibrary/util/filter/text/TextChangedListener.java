package com.framelibrary.util.filter.text;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.framelibrary.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditText字符串修改监听
 *
 * @author wangwx
 * @Date 2020年12月22日18:25:24
 */
public class TextChangedListener {

    // 只可以输入英文+数字s
    public static InputFilter allowEnglishNumWrap = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[a-zA-Z|[0-9]*]+");
            Matcher matcher = p.matcher(source.toString());
            if (!matcher.find()) {
//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入特殊字符!");
                return "";
            } else {
                return null;
            }
        }
    };
    public static InputFilter NumWrap = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (StringUtils.isNumeric(source.toString())) {
                int sourceNum = Integer.parseInt(source.toString());
                if (sourceNum == (0) || sourceNum == (1) ||
                        sourceNum == (2) || sourceNum == (3) ||
                        sourceNum == (4) || sourceNum == (5) ||
                        sourceNum == (6) || sourceNum == (7) ||
                        sourceNum == (8) || sourceNum == (9)) {

//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入数字!");
                    return "";

                } else return null;
            } else return null;
        }
    };
    // 允许输入汉字
    public static InputFilter allowChineseWrap = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[|\u4e00-\u9fa5]+");
            Matcher matcher = p.matcher(source.toString());
            if (!matcher.find()) {
//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入特殊字符!");
                return "";
            } else {
                return null;
            }
        }
    };
    // 禁止输入汉字
    public static InputFilter prohibitChineseWrap = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[|\u4e00-\u9fa5]+");
            Matcher matcher = p.matcher(source.toString());
            if (matcher.find()) {
//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入汉字");
                return "";
            } else {
                return null;
            }
        }
    };
    /**
     * 只可以输入汉字+英文
     */
    public static InputFilter chineseTypeFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[a-zA-Z|•|\u4e00-\u9fa5]+");
            Matcher matcher = p.matcher(source.toString());
            if (!matcher.find()) {
//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入特殊字符!");
                return "";
            } else {
                return null;
            }
        }
    };
    /**
     * 禁止EditText输入特殊字符,但允许输入点  .   `
     */
    public static InputFilter filterSpecialExcludePointsCharacters = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String speChat = "[`~!@#$%^&*()+-=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find()) {
//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入特殊字符!");
                return "";
            } else {
                return null;
            }
        }
    };
    /**
     * 禁止EditText输入特殊字符
     */
    public static InputFilter filterSpecialCharacters = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String speChat = "[`~!@#$%^&*()+-=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if (matcher.find()) {
//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入特殊字符!");
                return "";
            } else {
                return null;
            }
        }
    };
    /**
     * 禁止输入空格
     */
    private static InputFilter SpaceWrap = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source.equals(" ") || source.toString().contentEquals("\n")) {

//                ToastUtils.showShortToast(BaseApplication.getInstance().getBaseContext(), "禁止输入空格!");
                return "";

            } else return null;
        }
    };

    // 限制不能输入特殊字符与数字,但允许输入点  •
    public static void specialStringExcludePointsWatcher(int maxLength, final EditText... editText) {
        InputFilter[] inputFilters = {TextChangedListener.filterSpecialExcludePointsCharacters, TextChangedListener.chineseTypeFilter, TextChangedListener.NumWrap, new EmojiFilter()};
        TextChangedListener.inputLimitSpaceWrap(maxLength, inputFilters, editText);
    }

    // 限制不能输入特殊字符与数字
    public static void specialStringWatcher(int maxLength, final EditText... editText) {
        InputFilter[] inputFilters = {TextChangedListener.chineseTypeFilter, TextChangedListener.NumWrap, TextChangedListener.filterSpecialCharacters, new EmojiFilter()};
        TextChangedListener.inputLimitSpaceWrap(20, inputFilters, editText);
    }

    // 限制输入框不能输入汉字
    public static void stringWatcher(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c >= 0x4e00 && c <= 0X9fff) {
                            s.delete(i, i + 1);
                        }
                    }
                }
            }
        });
    }

    //限制不能输入空格与换行
    public static void inputLimitSpaceWrap(int maxLength, EditText... editText) {

        /*for (int i = 0; i < editText.length; i++) {
            editText[i].setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength), SpaceWrap});
        }*/
        inputLimitSpaceWrap(maxLength, new InputFilter[]{new InputFilter.LengthFilter(maxLength), SpaceWrap}, editText);
    }

    /**
     * 根据传递InputFilter[] 设置限制输入条件
     *
     * @param maxLength
     * @param inputFilterArr
     * @param editText
     */
    public static void inputLimitSpaceWrap(int maxLength, InputFilter[] inputFilterArr, EditText... editText) {
        if (inputFilterArr != null && inputFilterArr.length != 0) {
            for (int i = 0; i < editText.length; i++) {
                List<InputFilter> inputFilterList = new ArrayList<>();
                inputFilterList.add(new InputFilter.LengthFilter(maxLength));
                inputFilterList.add(SpaceWrap);

                for (int j = 0; j < inputFilterArr.length; j++) {
                    inputFilterList.add(inputFilterArr[j]);
                }

                InputFilter[] inputFilters = new InputFilter[inputFilterList.size()];
                for (int z = 0; z < inputFilterList.size(); z++) {
                    inputFilters[z] = inputFilterList.get(z);
                }
                editText[i].setFilters(inputFilters);
            }
        }
    }

    /**
     * 根据regular规则限制EditText输入类型
     *
     * @param editText
     */
    public static void InputLimitRegular(EditText... editText) {
        String regular = "+1234567890";
        for (int i = 0; i < editText.length; i++) {
            InputLimitRegular(editText[i], regular);
        }
    }


    /**
     * 根据regular规则限制EditText输入类型
     *
     * @param editText
     * @param regular  限制格式 仅可输入
     *                 //限制输入框只能输入英文和数字
     */
    public static void InputLimitRegular(EditText editText, String regular) {
        //限制输入框只能输入英文和数字
        if (StringUtils.isBlank(regular))
            regular = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        final String finalRegular = regular;
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] ac = finalRegular.toCharArray();
                return ac;
            }
        });
    }
}