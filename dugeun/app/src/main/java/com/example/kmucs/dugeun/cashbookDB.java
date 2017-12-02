package com.example.kmucs.dugeun;

/**
 * 20163129 유지원
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class cashbookDB extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음 (DB 매개변수에 대한 설명은 cashbook.java에 첨부 되어있음)
    public cashbookDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //super : 상위클래스의 생성자를 호출
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        
        
        // 새로운 테이블 생성
         /* 
          * 이름은 MONEYBOOK이고, 자동으로 값이 증가(KEY AUTOINCREMENT)하는 _id 정수형 기본키 컬럼과
          * item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. 	
          **/

        db.execSQL("CREATE TABLE MONEYBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, create_at TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String create_at, String item, int price) {
        // 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값(parameter로 받아옴)으로 행 추가
        db.execSQL("INSERT INTO MONEYBOOK VALUES(null, '" + item + "', " + price + ", '" + create_at + "');");
        db.close();
    }

    // 내용 입력 : DB에 가격, 항목 입력
    public void update(String item, int price) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
        db.close();
    }

    // 내용 삭제 : DB에서 항목을 찾아 삭제
    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
        db.close();
    }

    // 조회(내용 출력) 함수 : DB 읽기가능으로 열어서 한줄(행)씩 읽어옴
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        // 처음에는 result값 초기화
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);
        // 테이블의 모든 행을 방문
        while (cursor.moveToNext()) {
            /* result += cursor.getString(0) 이렇게 하면 현재 커서의 위치가 출력 되지만
        	 * 고정된 문자열("지출내역 : ")만 출력할 것임
             **/
            result += " 지출내역 : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "원 "
                    + cursor.getString(3)
                    + "\n";
        }

        return result;
    }
}

