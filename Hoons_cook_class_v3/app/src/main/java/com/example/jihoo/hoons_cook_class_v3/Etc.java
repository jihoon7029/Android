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
 * Created by jihoon on 2012. 5. 14..
 */

public class Etc extends Fragment {
    RelativeLayout layout;

    private ImageView Img_etc[];
    private TextView Txt_etc[];
    private boolean check_etc[];

    private LinearLayout etc_ID[];
    private int etc_img_ID[];
    private final String etc_txt[]={"우유","계란","슬라이스 치즈","모짜렐라 치즈","슬라이스 햄","스팸","햄","두부","카레가루","라면","통조림 골뱅이","어묵"};

    private static SQLiteDatabase DB;
    private static DatabaseHelper DBH;
    private static final String DATABASE_NAME = "Hoons_cook_class.db";

    private Thread thread1;

    public Etc(){};
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Img_etc = new ImageView[12];
        check_etc = new boolean[12];
        etc_img_ID = new int [12];
        Txt_etc = new TextView[12];
        etc_ID = new LinearLayout[12];
        for(int i=0;i<12;i++){
            String resName="@drawable/etc_"+(i+1);
            etc_img_ID[i]=getResources().getIdentifier(resName, "drawable", this.getActivity().getPackageName());
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
                    Cursor c1 = DB.rawQuery("select _check from Etc", null);
                    c1.moveToFirst();
                    int count = 0;

                    while (!c1.isAfterLast()) {
                        Log.v("count", "." + count);
                        if (c1.getInt(0) == 0)
                            check_etc[count] = false;
                        else if (c1.getInt(0) == 1)
                            check_etc[count] = true;
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
        layout = (RelativeLayout) inflater.inflate(R.layout.etc, container, false);
        return layout;
    }
    @Override
    public void onResume() {
        super.onResume();
        try{ thread1.join(); }catch (InterruptedException e) { }
        for (int i = 0; i < 12; i++) {
            etc_ID[i] = (LinearLayout)layout.findViewById(getResources().getIdentifier("etc_item"+(i+1),"id",this.getActivity().getPackageName()));
            etc_ID[i].setOnClickListener(etcClickListener);
            Img_etc[i]=(ImageView)etc_ID[i].findViewById(R.id._img);
            if (check_etc[i])
                Img_etc[i].setImageResource(R.drawable.check);
            else
                Img_etc[i].setImageResource(etc_img_ID[i]);
            Txt_etc[i]=(TextView)etc_ID[i].findViewById(R.id._text);
            Txt_etc[i].setText(etc_txt[i]);

        }
    }

    View.OnClickListener etcClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            LinearLayout linear = (LinearLayout) v;
            int count = 0;
            for (LinearLayout temp : etc_ID) {
                if (temp == linear) {
                    if (check_etc[count]) {
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(etc_img_ID[count]);
                        DBH.insert_into(DB,"Etc",0,count);
                    }
                    else {
                        Log.v("여기여ㅛ", "1312321");
                        ((ImageView)v.findViewById(R.id._img)).setImageResource(R.drawable.check);
                        DBH.insert_into(DB,"Etc",1,count);
                    }
                    Log.v("here",check_etc[count]+"");
                    check_etc[count]=!check_etc[count];
                    Log.v("here",check_etc[count]+"");
                    break;
                }
                count++;
            }
        }
    };
}
