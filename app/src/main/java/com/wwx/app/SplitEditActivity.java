package com.wwx.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.al.open.OnInputListener;
import com.al.open.SplitEditTextView;
import com.framelibrary.util.TextChangedListener;
import com.framelibrary.util.dialog.DialogDoNet;

public class SplitEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_edit);
        SplitEditTextView splitEditTextView = findViewById(R.id.splitEdit2);
        TextChangedListener.inputLimitSpaceWrap(20, splitEditTextView);

        DialogDoNet.startLoad(this, "更新了");

        splitEditTextView.setOnInputListener(new OnInputListener() {
            @Override
            public void onInputFinished(String content) {
                //内容输入完毕
            }

            @Override
            public void onInputChanged(String text) {
                //可选择重写该方法
                if (text.toString().indexOf(" ") != -1) {
                    String content = text.toString().replaceAll(" ", "").trim();
                    splitEditTextView.setText(content);
                }
            }
        });
    }
}
