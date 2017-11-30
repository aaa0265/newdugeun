package com.example.kmucs.dugeun;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YoungeunSong on 2017-10-31.
 */

// 기억해라!!!!!  ArrayAdapter<String> : 리스트 객체 안에 저장된 데이터들을 우리가 볼 수 있게 ListView 로 뿌려주는 역할.


public class onememo_main extends AppCompatActivity {

    //객체생성
    onememo onememo_;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;


    //onememo 초기화면 구성 함수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //onememo_main 레이아웃을 가져옴
        setContentView(R.layout.activity_onememo_main);

        onememo_ = new onememo(this);
        lstTask = (ListView)findViewById(R.id.lstTask);
        loadTaskList();
    }

    // list 내용을 load함.
    private void loadTaskList(){
        ArrayList<String> taskList = onememo_.getTaskList();
        //내용이 없으면 새로 추가
        if (mAdapter==null) {
            //this = mainactivity의 instance
            // row = 한 줄씩, taskList = 내용 목록
            mAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.task_title, taskList);
            lstTask.setAdapter(mAdapter);
        }

        // mAdapter를 초기화 한다-> .clear()
        // mAdapter에 taskList의 내용을 모두 담는다. ->.addAll
        // notifyDataSetChanged : 새로운 내용이 있나 확인하고 변경사항이 있으면 리스트를 갱신하라는 메소드(새로운 내용 추가됨)
        else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    //메뉴 추가 아이콘 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    // 메모 추가를 눌렀을 때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                //dialog생성하는 부분
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("메모 추가")
                        .setMessage("무엇을 기록할까요?")
                        .setView(taskEditText)
                        .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                onememo_.insertNewTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .create();
                dialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //삭제함
    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());
        onememo_.deleteTask(task);
        loadTaskList();
    }

}
