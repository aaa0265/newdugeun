package com.example.kmucs.dugeun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(2000);             // 2초쉬고 넘어간다.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //로딩이 끝난후 이동할 액티비티 -> duguen
        startActivity(new Intent(this,Duguen.class));
        //종료하기
        finish();
    }
}
