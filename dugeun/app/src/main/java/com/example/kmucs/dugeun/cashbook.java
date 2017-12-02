package com.example.kmucs.dugeun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class cashbook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashbook);                        // view는 cashbook layout
        
        /*	
         * context에 는 DB를 생성하는 컨텍스트, 보통 메인 액티비티를 전달
         *	name은 데이터베이스 파일 이름(MoneyBook로 임의 지정)
         *	factory는  커스텀 커서 사용을 위해 지정, 표준 커서 이용 시 null로 지정.
         *		커서: DB에서 가져온 데이터를 쉽게 처리하기 위해 사용
         *			 Cursor는 기본적으로 DB에서 값을 가져와서 마치 실제 Table의 한 행(Row), 한 행(Row)을 참조하는 것 처럼 사용 할 수 있게 해줌
         *			 개발자는 마치 그 행(Row)을 가지고 행(Row)에 있는 데이터를 가져다가 쓰는 것 처럼 사용하면 되는 편의성을 제공받음
         *	version은 데이터베이스의 버전
         */
        
        // listener로 전달하고 싶은 지역 변수는 final로 처리해야함 (final을 달면 지역변수가 사실상 전역변수처럼 지속됨)
        final cashbookDB dbHelper = new cashbookDB(getApplicationContext(), "MoneyBook.db", null, 1);   // moneyBook은 임의로 만든 이름

        // 테이블에 있는 모든 데이터 출력
        final TextView result = (TextView) findViewById(R.id.result);
   
        // 날짜, 항목, 금액
        final EditText etDate = (EditText) findViewById(R.id.date);
        final EditText etItem = (EditText) findViewById(R.id.item);
        final EditText etPrice = (EditText) findViewById(R.id.price);

        // 날짜는 현재 날짜로 고정
        // 현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);


        // 출력될 포맷 설정
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        etDate.setText(simpleDateFormat.format(date));


        // DB에 데이터 추가
        // Onclick의 값을 OnclickListener가 값을 받음
        /*
         * 추가 버튼을 누르면
         * data에 현재 날짜가 추가 되고
         * item에는 항목 내용이 추가됨
         * 가격은 String으로 받은 것을 int로 변환
         */
        Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etDate.getText().toString();
                String item = etItem.getText().toString();
                int price = Integer.parseInt(etPrice.getText().toString());

                dbHelper.insert(date, item, price);
                result.setText(dbHelper.getResult());
            }
        });

        // DB의 데이터 수정
        /*
         * 이름이 같은 것은 같이 수정되고
         * 날짜와 항목은 수정이 불가함
         */
        Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString();
                int price = Integer.parseInt(etPrice.getText().toString());

                dbHelper.update(item, price);
                result.setText(dbHelper.getResult());
            }
        });

        // DB의 데이터 삭제
        	// 항목이 같은 것은 한꺼번에 삭제 됨
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString();
                dbHelper.delete(item);
                result.setText(dbHelper.getResult());
            }
        });

        // DB 조회 (저장된 값들을 출력)
        	// 어플 종료 후 다시 켰을 때 "조회"버튼을 누르면 내용 보이기
        Button select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(dbHelper.getResult());}
        });
    }
}
