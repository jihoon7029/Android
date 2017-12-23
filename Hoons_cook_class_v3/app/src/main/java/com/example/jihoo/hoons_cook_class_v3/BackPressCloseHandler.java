package com.example.jihoo.hoons_cook_class_v3;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by jihoon on 2017. 4. 14..
 */

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler(Activity context) {

        this.activity = context;

    } public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
           // activity.finish();
            ActivityCompat.finishAffinity(this.activity);
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}



