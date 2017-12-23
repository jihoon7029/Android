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

public class Vegetable extends Fragment {
    RelativeLayout layout;

    private ImageView Img_vegetable[];
    private TextView Txt_vegetable[];
    private boolean check_vegetable[];
    private LinearLayout vegetable_ID[];
    private int vegetable_img_ID[];
    private final String vegetable_txt[] = {"마늘","감자","오이","당근","대파","쪽파","양파","청양고추","꽈리고추",
                                            "새송이 버섯","느타리 버섯","양송이 버섯","팽이 버섯","콩나물","무","무순","부추","숙주"};
    private static SQLiteDatabase DB;
    private static DatabaseHelper DBH;
    private static final String DATABASE_NAME = "Hoons_cook_class.db";

    private Thread thread1;

    public Vegetable(){};
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Img_vegetable = new ImageView[18];
        Txt_vegetable = new TextView[18];
        check_vegetable = new boolean[18];
        vegetable_img_ID = new int [18];
        vegetable_ID = new LinearLayout[18];
        for(int i=0;i<18;i++){
            String resName="@drawable/vegetable_"+(i+1);
            vegetable_img_ID[i]=getResources().getIdentifier(resName, "drawable", this.getActivity().getPackageName());
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
                    Cursor c1 = DB.rawQuery("select _check from Vegetable", null);
                    c1.moveToFirst();
                    int count = 0;

                    while (!c1.isAfterLast()) {
                        Log.v("count", "." + count);
                        if (c1.getInt(0) == 0)
                            check_vegetable[count] = false;
                        else if (c1.getInt(0) == 1)
                            check_vegetable[count] = true;
                        c1.moveToNext();
                        count++;
                    }

                } catch (Exception ex) {}
            }
        };
        thread1.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        layout = (RelativeLayout) inflater.inflate(R.layout.vegetable, container, false);
        return layout;
    }
    @Override
    public void onResume() {
        super.onResume();
        try{ thread1.join(); }catch (InterruptedException e) { }
        for (int i = 0; i < 18; i++) {
            vegetable_ID[i] = (LinearLayout)layout.findViewById(getResources().getIdentifier("vegetable_item"+(i+1),"id",this.getActivity().getPackageName()));
            vegetable_ID[i].setOnClickListener(vegClickListener);
            Img_vegetable[i]=(ImageView)vegetable_ID[i].findViewById(R.id._img);
            if (check_vegetable[i])
                Img_vegetable[i].setImageResource(R.drawable.check);
            else
                Img_vegetable[i].setImageResource(vegetable_img_ID[i]);
            Txt_vegetable[i]=(TextView)vegetable_ID[i].findViewById(R.id._text);
            Txt_vegetable[i].setText(vegetable_txt[i]);

        }
    }
    View.OnClickListener vegClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            LinearLayout linear = (LinearLayout) v;
            int count = 0;
            for (LinearLayout temp : vegetable_ID) {
                if (temp == linear) {
                    if (check_vegetable[count]) {
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(vegetable_img_ID[count]);
                        DBH.insert_into(DB,"Vegetable",0,count);
                    }
                    else {
                        Log.v("여기여ㅛ", "1312321");
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(R.drawable.check);
                        DBH.insert_into(DB,"Vegetable",1,count);
                    }
                    Log.v("here",check_vegetable[count]+"");
                    check_vegetable[count]=!check_vegetable[count];
                    Log.v("here",check_vegetable[count]+"");
                    break;
                }
                count++;
            }
        }
    };
}
