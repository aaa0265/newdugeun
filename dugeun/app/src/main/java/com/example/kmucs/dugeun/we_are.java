package com.example.kmucs.dugeun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class we_are extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we_are의 레이아웃 가져옴
        setContentView(R.layout.activity_we_are);
    }

    //버튼 onclick
    public void info(View v){
        // info로 넘어가기
        // getApplicationContext : 현재 액티비티 정보를 가지고 있음
        Intent intent = new Intent(getApplicationContext(), info2.class);
        startActivity(intent);
    }
}
