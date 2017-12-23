package com.example.jihoo.hoons_cook_class_v3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jihoo_000 on 2016-10-23.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Hoons_cook_class.db";
    private static final int DATABASE_VERSION = 1;
    private static final String[] TABLE_NAME={"Meat","Sea","Vegetable","Etc"};



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
   /*     String CREATE_SQL = "create table " + TABLE_NAME + "("
                + "_id integer PRIMARY KEY autoincrement, "
                + "name text, "
                + "number  int)";*/

        for (int i = 0; i < 4; i++) {
            String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME[i] + "("
                    + "_id integer PRIMARY KEY autoincrement, "
                    + "_name text, "
                    + "_check THINYINT(1)" + ")";
            db.execSQL(CREATE_SQL);
        }
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
     //   onCreate(db);
    }
    public void first(SQLiteDatabase db){
        String INSERT_SQL = "INSERT INTO "+ TABLE_NAME[0]+"(_id, _name, _check)"+
                "VALUES"+
                "(0,\"목살\",0),"+
                "(1,\"삼겹살\",0),"+
                "(2,\"등심\",0),"+
                "(3,\"돼지고기 찌개용\",0),"+
                "(4,\"소(불고기)\",0),"+
                "(5,\"소(국거리)\",0),"+
                "(6,\"차돌박이\",0),"+
                "(7,\"닭가슴살\",0),"+
                "(8,\"닭(백숙용)\",0),"+
                "(9,\"닭다리\",0)";
        db.execSQL(INSERT_SQL);

        INSERT_SQL = "INSERT INTO "+ TABLE_NAME[1]+"(_id, _name, _check)"+
                "VALUES"+
                "(0,\"중멸치\",0),"+
                "(1,\"김가루\",0),"+
                "(2,\"김\",0),"+
                "(3,\"미역\",0),"+
                "(4,\"고등어\",0),"+
                "(5,\"홍합\",0),"+
                "(6,\"오징어\",0),"+
                "(7,\"새우\",0)";
        db.execSQL(INSERT_SQL);
        INSERT_SQL = "INSERT INTO "+ TABLE_NAME[2]+"(_id, _name, _check)"+
                "VALUES"+
                "(0,\"마늘\",0),"+
                "(1,\"감자\",0),"+
                "(2,\"오이\",0),"+
                "(3,\"당근\",0),"+
                "(4,\"대파\",0),"+
                "(5,\"쪽파\",0),"+
                "(6,\"양파\",0),"+
                "(7,\"청양고추\",0),"+
                "(8,\"꽈리고추\",0),"+
                "(9,\"새송이 버섯\",0),"+
                "(10,\"느타리 버섯\",0),"+
                "(11,\"양송이 버섯\",0),"+
                "(12,\"팽이 버섯\",0),"+
                "(13,\"콩나물\",0),"+
                "(14,\"무\",0),"+
                "(15,\"무순\",0),"+
                "(16,\"부추\",0),"+
                "(17,\"숙주\",0)";
        db.execSQL(INSERT_SQL);

        INSERT_SQL = "INSERT INTO "+ TABLE_NAME[3]+"(_id, _name, _check)"+
                "VALUES"+
                "(0,\"우유\",0),"+
                "(1,\"계란\",0),"+
                "(2,\"슬라이스 치즈\",0),"+
                "(3,\"모짜렐라 치즈\",0),"+
                "(4,\"슬라이스 햄\",0),"+
                "(5,\"스팸\",0),"+
                "(6,\"햄\",0),"+
                "(7,\"두부\",0),"+
                "(8,\"카레가루\",0),"+
                "(9,\"라면\",0),"+
                "(10,\"통조림 골뱅이\",0),"+
                "(11,\"어묵\",0)";
        db.execSQL(INSERT_SQL);
    }
    public void insert_into(SQLiteDatabase db,String table ,int check, int index)
    {
        db.execSQL("UPDATE "+table+" set _check="+check+" where _id="+index+";");
    }
    public ArrayList<String> true_check(SQLiteDatabase db){
        ArrayList<String> temp=new ArrayList<String>();
        String temp_string;
        try {
            Cursor c1 = db.rawQuery("select _name, _check from Meat", null);
            c1.moveToFirst();
            int count = 0;
            while (!c1.isAfterLast()) {
                if(c1.getInt(1) == 1){
                    temp_string=c1.getString(0);
                    temp.add(temp_string);
                }
                c1.moveToNext();
                count++;
            }
        } catch (Exception ex) {}
        try {
            Cursor c1 = db.rawQuery("select _name, _check from Sea", null);
            c1.moveToFirst();
            int count = 0;
            while (!c1.isAfterLast()) {
                if(c1.getInt(1) == 1){
                    temp_string=c1.getString(0);
                    temp.add(temp_string);
                }
                c1.moveToNext();
                count++;
            }
        } catch (Exception ex) {}

        try {
            Cursor c1 = db.rawQuery("select _name, _check from Vegetable", null);
            c1.moveToFirst();
            int count = 0;
            while (!c1.isAfterLast()) {
                if(c1.getInt(1) == 1){
                    temp_string=c1.getString(0);
                    temp.add(temp_string);
                }
                c1.moveToNext();
                count++;
            }
        } catch (Exception ex) {}

        try {
            Cursor c1 = db.rawQuery("select _name, _check from Etc", null);
            c1.moveToFirst();
            int count = 0;
            while (!c1.isAfterLast()) {
                if(c1.getInt(1) == 1){
                    temp_string=c1.getString(0);
                    temp.add(temp_string);
                }
                c1.moveToNext();
                count++;
            }
        } catch (Exception ex) {}


        Log.v("ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "!" + temp);
        return temp;
    }
}
