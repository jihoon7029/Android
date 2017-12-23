package kr.ac.inje.jihoon7029oasis.smart_home_delivery_box;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jihoo_000 on 2016-10-23.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Smart_box.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Smart_table_1";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
   /*     String CREATE_SQL = "create table " + TABLE_NAME + "("
                + "_id integer PRIMARY KEY autoincrement, "
                + "name text, "
                + "number  int)";*/
        String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + "_id integer PRIMARY KEY autoincrement, "
                + "name text)";

        db.execSQL(CREATE_SQL);
    }

    public void onOpen(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
     //   onCreate(db);
    }

    public void insert_into(SQLiteDatabase db, String message)
    {
       // db.execSQL("insert into"+TABLE_NAME+"(name,number) values (");
        db.execSQL("insert into " + TABLE_NAME + "(name) values ('" + message + "');");
    }
    public String return_table_name(){ return TABLE_NAME ;}
    public String return_database_name(){ return DATABASE_NAME ;}
}
