package com.example.kmucs.dugeun;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class we_are extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_are);
    }

    public void info2(View v){
        Intent intent = new Intent(getApplicationContext(), info2.class); //intent 할 객체 intent 생성, -> 이동할 액티비티 클래스( info2) 를 인자로 제공해주기
        startActivity(intent); //startActivity 는 화면전환을 뜻함 -> intent를 실행하라 -> info로 액티비티 넘어감!
    }
}
