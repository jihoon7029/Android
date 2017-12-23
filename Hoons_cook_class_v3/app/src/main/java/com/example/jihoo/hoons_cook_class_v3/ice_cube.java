package com.example.jihoo.hoons_cook_class_v3;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * Created by jihoon on 2017. 5. 14..
 */

public class ice_cube extends AppCompatActivity {

    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ice_cube);
        vp = (ViewPager) findViewById(R.id.vp);
        Button btn_first = (Button) findViewById(R.id.meat);
        Button btn_second = (Button) findViewById(R.id.sea);
        Button btn_third = (Button) findViewById(R.id.vegetable);
        Button btn_forth = (Button) findViewById(R.id.etc);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);


        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
        btn_third.setOnClickListener(movePageListener);
        btn_third.setTag(2);
        btn_forth.setOnClickListener(movePageListener);
        btn_forth.setTag(3);
    }
    ////뒤로가기 오버라이드
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_slide_enter, R.anim.activity_slide_exit);
    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.v("주모 여기요 !!!!!!!","1");
                    return new Meat();
                case 1:
                    Log.v("주모 여기요 !!!!!!!","2");
                    return new Sea();
                case 2:
                    Log.v("주모 여기요 !!!!!!!","3");
                    return new Vegetable();
                case 3:
                    Log.v("주모 여기요 !!!!!!!","4");
                    return new Etc();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
