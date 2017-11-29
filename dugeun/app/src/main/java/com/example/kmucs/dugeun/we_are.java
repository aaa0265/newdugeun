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
        //intent 할 객체 intent 생성, -> 이동할 액티비티 클래스( info2) 를 인자로 제공해주기
        startActivity(intent);
        //startActivity 는 화면전환을 뜻함 -> intent를 실행하라 -> info로 액티비티 넘어감!
    }
}
