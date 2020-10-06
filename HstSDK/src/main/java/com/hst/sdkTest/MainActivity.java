package com.hst.sdkTest;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inpor.fastmeetingcloud.sdk.HstLoginManager;



public class MainActivity extends AppCompatActivity {
    EditText edit_user,edit_password,edit_room,edit_serverIp,edit_port,edit_roomId,edit_nickname,edit_roomPwd,editEnterMode,editJoinMode;
    Button btnLogin,btnJoin,btnEnterRoom;
    SharedPreferences sharedPreferences;
    private HstLoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginManager = HstLoginManager.getInstance();

        sharedPreferences = this.getSharedPreferences("hstInfo",MODE_PRIVATE);
        edit_user = (EditText)findViewById(R.id.edit_user);
        edit_password = (EditText)findViewById(R.id.edit_password);
        edit_room = (EditText)findViewById(R.id.edit_room);
        edit_serverIp = (EditText)findViewById(R.id.edit_serverIp);
        edit_roomId = (EditText)findViewById(R.id.edit_roomId);
        edit_port = (EditText)findViewById(R.id.edit_port);
        edit_nickname = (EditText)findViewById(R.id.edit_nickname);
        edit_roomPwd = (EditText)findViewById(R.id.edit_roomPwd);
        editEnterMode = (EditText)findViewById(R.id.enterMode);
        editJoinMode = (EditText)findViewById(R.id.joinMode);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnJoin = (Button)findViewById(R.id.btnJoin);
        btnEnterRoom = (Button)findViewById(R.id.btnEnterRoom);


        edit_serverIp.setText(sharedPreferences.getString("serverIp", ""));
        edit_port.setText(sharedPreferences.getString("port",""));
        edit_user.setText(sharedPreferences.getString("userName", ""));
        edit_password.setText(sharedPreferences.getString("password", ""));
        edit_room.setText(sharedPreferences.getString("roomId1", ""));
        edit_nickname.setText(sharedPreferences.getString("nickname", ""));
        edit_roomId.setText(sharedPreferences.getString("roomId2", ""));
        edit_roomPwd.setText(sharedPreferences.getString("roomPwd", ""));
        editEnterMode.setText(String.valueOf(sharedPreferences.getInt("enterMode",0)));
        editJoinMode.setText(String.valueOf(sharedPreferences.getInt("joinMode", 0)));


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join();
            }
        });
        btnEnterRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterRoom();
            }
        });

    }

    /**
     * 用户名+密码入会
     */
    public void enterRoom(){
        String serverIp = edit_serverIp.getText().toString().trim();
        String port = edit_port.getText().toString().trim();
        String userName = edit_user.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        String roomId1 = edit_room.getText().toString().trim();
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("serverIp",serverIp);
        editor.putString("port",port);
        editor.putString("userName",userName);
        editor.putString("password",password);
        editor.putString("roomId1",roomId1);
        editor.apply();

        loginManager.enterRoom(this,serverIp,port,userName,password,roomId1);
    }


    /**
     * 房间号+房间密码+昵称入会
     */
    public void join(){

        String serverIp = edit_serverIp.getText().toString().trim();
        String port = edit_port.getText().toString().trim();
        String userName = edit_user.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        String roomId1 = edit_room.getText().toString().trim();
        String nickname = edit_nickname.getText().toString().trim();
        String roomId2 = edit_roomId.getText().toString().trim();
        String roomPwd = edit_roomPwd.getText().toString().trim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("serverIp",serverIp);
        editor.putString("port",port);
        editor.putString("nickname","nickname");
        editor.putString("roomId2",roomId2);
        editor.putString("roomPwd",roomPwd);
        editor.apply();
        loginManager.joinMeeting(this,edit_serverIp.getText().toString().trim(),port,nickname,roomId2,roomPwd);
    }

}
