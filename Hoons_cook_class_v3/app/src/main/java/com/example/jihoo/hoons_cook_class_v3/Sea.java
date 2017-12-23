package com.example.jihoo.hoons_cook_class_v3;



import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jihoon on 2017. 5. 14..
 */

public class Sea extends Fragment {

    private ImageView Img_sea[];
    private TextView Txt_sea[];
    private boolean check_sea[];
    private LinearLayout sea_ID[];
    private int sea_img_ID[];
    private final String sea_txt[]={"중멸치","김가루","김","미역","고등어","홍합","오징어","새우"};


    private static SQLiteDatabase DB;
    private static DatabaseHelper DBH;
    private static final String DATABASE_NAME = "Hoons_cook_class.db";

    private RelativeLayout layout;
    private Thread thread1;

    public Sea() {};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Img_sea = new ImageView[8];
        check_sea = new boolean[8];
        Txt_sea = new TextView[8];
        sea_ID = new LinearLayout[8];
        sea_img_ID = new int [8];

        for(int i=0;i<8;i++){
            String resName="@drawable/sea_"+(i+1);
            sea_img_ID[i]=getResources().getIdentifier(resName, "drawable", this.getActivity().getPackageName());
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
                    Cursor c1 = DB.rawQuery("select _check from Sea", null);
                    c1.moveToFirst();
                    int count = 0;

                    while (!c1.isAfterLast()) {
                        Log.v("count", "." + count);
                        if (c1.getInt(0) == 0)
                            check_sea[count] = false;
                        else if (c1.getInt(0) == 1)
                            check_sea[count] = true;
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
        layout = (RelativeLayout) inflater.inflate(R.layout.sea, container, false);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{ thread1.join(); }catch (InterruptedException e) { }
        for (int i = 0; i < 8; i++) {
            sea_ID[i] = (LinearLayout)layout.findViewById(getResources().getIdentifier("sea_item"+(i+1),"id",this.getActivity().getPackageName()));
            sea_ID[i].setOnClickListener(seaClickListener);
            Img_sea[i]=(ImageView)sea_ID[i].findViewById(R.id._img);
            if (check_sea[i])
                Img_sea[i].setImageResource(R.drawable.check);
            else
                Img_sea[i].setImageResource(sea_img_ID[i]);
            Txt_sea[i]=(TextView)sea_ID[i].findViewById(R.id._text);
            Txt_sea[i].setText(sea_txt[i]);
        }
    }

    View.OnClickListener seaClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Log.v("주모 성공이요! !!!!!!!",""+v.getId());
            LinearLayout linear = (LinearLayout) v;
            int count = 0;
            for (LinearLayout temp : sea_ID) {
                if (temp == linear) {
                    if (check_sea[count]) {
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(sea_img_ID[count]);
                        DBH.insert_into(DB,"Sea",0,count);
                    } else {
                        Log.v("여기여ㅛ", "1312321");
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(R.drawable.check);
                        DBH.insert_into(DB,"Sea",1,count);
                    }
                    Log.v("here", check_sea[count] + "");
                    check_sea[count] = !check_sea[count];
                    Log.v("here", check_sea[count] + "");
                    break;
                }
                count++;
            }

        }
    };
}
