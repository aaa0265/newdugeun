package com.example.kmucs.dugeun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class onememo extends SQLiteOpenHelper{

    private  static final String DB_NAME="EDMTDev";
    private static final int DB_VER = 1;
    public static final String DB_TABLE = "Task";
    public static final String DB_COLUMN ="TaskName";


    public onememo(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //테이블 생성
    @Override
    public void onCreate (SQLiteDatabase db){
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", DB_TABLE, DB_COLUMN);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    //내용 삽입 함수
    public void insertNewTask(String task){
        //쓰기가능으로 열기
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        //------------------ 모름
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    //삭제 함수
    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        //중복된 값도 삭제됨.
        db.delete(DB_TABLE,DB_COLUMN + " = ?",new String[]{task});
        db.close();
    }

    //task table에 있는 메모들을 가져온다.
    public ArrayList<String> getTaskList(){
            ArrayList<String> taskList = new ArrayList<>();
            //읽기가능한 형태로 db불러오기
            SQLiteDatabase db = this.getReadableDatabase();

        /** query() 메소드사용
         1. 대상 테이블 이름
         2. 내가 가져올 데이터가 있는 칼럼의 배열
         3~7 : 필요없는 부분이라 null
         가져온 애들을 cursor에 넣는다. */

        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN},null,null,null,null,null);
        while (cursor.moveToNext()){
            //getColumnIndex(DB_COLUMN) = 1번째 열 = taskname
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }

}
