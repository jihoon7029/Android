package com.example.jihoo.hoons_cook_class_v3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private SharedPreferences mPre;
    private SharedPreferences.Editor mPrefEdit_first_check;

    public static SQLiteDatabase DB;
    public static DatabaseHelper DBH;

    public static final String DATABASE_NAME = "Hoons_cook_class.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPre = getSharedPreferences("Pref1", MODE_PRIVATE);
        mPrefEdit_first_check = mPre.edit();
        try {
            DB =openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            DBH= new DatabaseHelper(this);
            DBH.onCreate(DB);
            if(mPre.getBoolean("app_first",true)) {
                mPrefEdit_first_check.putBoolean("app_first",false);
                mPrefEdit_first_check.commit();
                DBH.first(DB);
            }
        }catch(Exception ex){
            ex.getMessage();
        }
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        },3000);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
}
