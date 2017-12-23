package kr.ac.inje.jihoon7029oasis.smart_home_delivery_box;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyService extends Service {
    TextView TV1;
    String message;
    DatabaseHelper DBH;
    SQLiteDatabase DB;
    Intent main_activity_intent ;

    String match_message;
    boolean check = true;
    Pattern pattern;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        message= intent.getStringExtra("message");

        if(message.contains("우체국택배") ||message.contains("우체국")) {
             pattern  =  Pattern.compile("[0-9]{13}");
        }
        else if(message.contains("로젠택배") ||message.contains("로젠")) {
            pattern  =  Pattern.compile("[0-9]{11-12}");
        }
        else if(message.contains("CJ대한통운") ||message.contains("대한통운")||message.contains("CJ택배")) {
            pattern  =  Pattern.compile("[0-9]{12}");
        }
        else if(message.contains("한진택배") ||message.contains("한진 택배")) {
             pattern  =  Pattern.compile("[0-9]{12}");
        }
        else if(message.contains("현대택배") ||message.contains("현대 택배")) {
            pattern  =  Pattern.compile("[0-9]{12}");
        }
        else if(message.contains("KG로지스") ||message.contains("KG 로지스") || message.contains("로지스택배")) {
            pattern  =  Pattern.compile("[0-9]{12}");
        }
        else if(message.contains("CVS편의점택배") ||message.contains("CVS 편의점택배")
                || message.contains("편의점택배")|| message.contains("편의점 택배") || message.contains("포스트박스")
                || message.contains("포스트 박스")) {
            pattern  =  Pattern.compile("[0-9]{10}");
        }
        else if(message.contains("합동택배") ||message.contains("합동 택배")) {
            pattern  =  Pattern.compile("[0-9]{12}");
        }
        else if(message.contains("KGB택배") ||message.contains("KGB 택배")) {
            pattern  =  Pattern.compile("[0-9]{10}");
        }
        else if(message.contains("로켓배송") ||message.contains("로켓 배송") ||message.contains("로켓맨")) {
            pattern  =  Pattern.compile("[0-9]{14}");
        }
        else {
            check=false;
            this.stopSelf();
        }

        if(check) {
            Matcher match = pattern.matcher(message);
            if (match.find()) {
                match_message = match.group();
                Log.d("저장시작!!", "저장시작!!!");
                DBH = new DatabaseHelper(this);
                DB = DBH.getWritableDatabase();
                DBH.insert_into(DB, match_message);
            }
        }
        stopSelf();
        return START_REDELIVER_INTENT;    ///// 메모리 부족 현상으로 종료되더라도  이전의 intent 값을 가져와 서비스 재실행
    }
}
