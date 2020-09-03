package com.wwx.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.framelibrary.widget.image.ShowImagesDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> urls = new ArrayList<>();
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511198824138&di=cec97b6363a1bce28b8499a31b78df83&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F3e282f8762696b0bbb3ed16a5dc193c718e5aff9.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511793592&di=08b8de22336028a68c9a0bbbe7a9066d&imgtype=jpg&er=1&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2F7ea7878cfbddb27bb8a23e2407bfa7a48655f317.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511198824136&di=f4dc71ffdbbb16d9e3d496cf8add5376&imgtype=0&src=http%3A%2F%2Foss.tan8.com%2Fresource%2Fattachment%2F2017%2F201707%2F962b7304d0adc7e2e1d374dc6786e302.jpg");
        findViewById(R.id.btn_show_imgs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowImagesDialog(MainActivity.this, urls).show();
            }
        });
    }
}
