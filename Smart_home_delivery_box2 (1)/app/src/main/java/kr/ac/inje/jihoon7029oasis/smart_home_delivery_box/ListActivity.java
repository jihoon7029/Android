package kr.ac.inje.jihoon7029oasis.smart_home_delivery_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jihoo on 2016-11-17.
 */

public class ListActivity extends Activity {//AppCompatActivity {

    ListView LV1;
    ArrayList<String> Array1;
    SQLiteDatabase DB;
    DatabaseHelper DBH;
    Button button_back;
    ArrayAdapter<String> Adapter;

    public static final String TABLE_NAME = "Smart_table_1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        int i;
        LV1 = (ListView) findViewById(R.id.ListView_02);
        Array1 = new ArrayList<String>();

        button_back=(Button)findViewById(R.id.back_button);

       DBH = new DatabaseHelper(this);
       DB = DBH.getReadableDatabase();

     /*   Cursor c1 = DB.rawQuery("select count(*) as Total from " + TABLE_NAME, null);
        count = c1.getCount();
        for (int i = 1; i <= count; i++) {
            Array1.add(c1.getString(i));
            c1.moveToNext();
        }
        c1.close();*/
        //Cursor cursor = DB.rawQuery("SELECT * FROM ex", null);
       // startManagingCursor(cursor);
        Cursor c1 = DB.rawQuery("select name from "+TABLE_NAME,null);
        c1.moveToFirst();
        for( i=1;i<=c1.getCount();i++){
    //    Log.i("여기다!!!!!!!!!!!",c1.getString(0));
            Array1.add(c1.getString(0));
          c1.moveToNext();
       }

        Adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, Array1);
        LV1.setAdapter(Adapter);

        LV1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int pos, long l) {
           //     Toast.makeText(ListActivity.this, "Hi", Toast.LENGTH_SHORT).show();

                final String delete_string=Array1.get(pos);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListActivity.this,R.style.AlertDialogCustom);

                alertDialogBuilder.setTitle("아이템 삭제");

                alertDialogBuilder
                        .setMessage("아이템을 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {

                                        DB.execSQL("delete from "+TABLE_NAME+" where name ='"+ delete_string+"'");
                                        Array1.remove(delete_string);
                                        LV1.setAdapter(Adapter);
                                    }
                                })
                        .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                AppCompatDialog dialog1=alertDialogBuilder.create();
                dialog1.show();

                return false;
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListActivity.this.finish();
            }
        });
    }
}
