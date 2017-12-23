package com.example.jihoo.hoons_cook_class_v3;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by jihoon on 2017. 5. 14..
 */

public class Meat extends Fragment {
    private ImageView Img_meat[];
    private TextView Txt_meat[];
    private boolean check_meat[];
    private int meat_img_ID[];
    private LinearLayout meat_ID[];
    private final String meat_txt[]={"목살","삼겹살","등심","돼지(찌개용)","소(불고기)","소(국거리)","차돌박이","닭가슴살","닭(백숙용)","닭다리"};

    private static SQLiteDatabase DB;
    private static DatabaseHelper DBH;
    private static final String DATABASE_NAME = "Hoons_cook_class.db";

    private Thread thread1;

    public Meat() {
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Img_meat = new ImageView[10];
        check_meat = new boolean[10];
        meat_img_ID = new int[10];
        meat_ID = new LinearLayout[10];
        Txt_meat = new TextView[10];
        for(int i=0;i<10;i++){
            String resName="@drawable/meat_"+(i+1);
            meat_img_ID[i]=getResources().getIdentifier(resName, "drawable", this.getActivity().getPackageName());
        }
        try {
            DB = this.getActivity().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            DBH = new DatabaseHelper(this.getActivity());
        } catch (Exception ex) {
            ex.getMessage();
        }
        thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    Cursor c1 = DB.rawQuery("select _check from Meat", null);
                    c1.moveToFirst();
                    int count = 0;

                    while (!c1.isAfterLast()) {
                        Log.v("count", "." + count);
                        if (c1.getInt(0) == 0)
                            check_meat[count] = false;
                        else if (c1.getInt(0) == 1)
                            check_meat[count] = true;
                        c1.moveToNext();
                        count++;
                    }
                } catch (Exception ex) {}
            }
        };
        thread1.start();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.meat, container, false);
        try{ thread1.join(); }catch (InterruptedException e) { }
         for (int i = 0; i < 9; i++) {
             meat_ID[i] = (LinearLayout)layout.findViewById(getResources().getIdentifier("meat_item"+(i+1),"id",this.getActivity().getPackageName()));
            meat_ID[i].setOnClickListener(meatClickListener);
             Img_meat[i]=(ImageView)meat_ID[i].findViewById(R.id._img);
            if (check_meat[i])
                Img_meat[i].setImageResource(R.drawable.check);
             else
                Img_meat[i].setImageResource(meat_img_ID[i]);
             Txt_meat[i]=(TextView)meat_ID[i].findViewById(R.id._text);
             Txt_meat[i].setText(meat_txt[i]);
        }
        return layout;
    }

    View.OnClickListener meatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout linear = (LinearLayout) v;
            int count = 0;
            for (LinearLayout temp : meat_ID) {
                if (temp == linear) {
                    if (check_meat[count]) {
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(meat_img_ID[count]);
                        DBH.insert_into(DB,"Meat",0,count);
                    }
                    else {
                        Log.v("여기여ㅛ", "1312321");
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(R.drawable.check);
                        DBH.insert_into(DB,"Meat",1,count);
                    }
                    Log.v("here",check_meat[count]+"");
                    check_meat[count]=!check_meat[count];
                    Log.v("here",check_meat[count]+"");
                    break;
                }
                count++;
            }

        }
    };
}
