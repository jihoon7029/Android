package kr.ac.inje.jihoon7029oasis.smart_home_delivery_box;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

    String str = ""; // 출력할 문자열 저장
Intent myIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        myIntent = new Intent(context, MyService.class);
        Log.d("브로드캐스트 비교전!!!","브로드캐스트 비교전!!!");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") ||intent.getAction().equals("android.provider.Telephony.MMS_RECEIVED") ) {
            Log.d("브로드캐스트 비교후!!!","브로드캐스트 비교후!!!");
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getMessageBody().toString() ;
                // 번호확인 msgs[i].getOriginatingAddress()
                // 내용확인 msgs[i].getMessageBody().toString()
                myIntent.putExtra("message", str);
                context.startService(myIntent);
            }
        }
    }
}
/*
  String str = ""; // 출력할 문자열 저장
    Intent myIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        myIntent = new Intent(context, MyService.class);
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object [] pdus = (Object[])bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress() + "에게 문자왔음, " + msgs[i].getMessageBody().toString() +"\n";
                myIntent.putExtra("message", str);
                context.startService(myIntent);
            }
        }
    }
}

 */
/*    Intent myIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        myIntent = new Intent(context, MyService.class);
        Log.v("알람", "시작!!!!!!!!!!!!!!!!!");
        Log.v("알람", "시작!!!!!!!!!!!!!!!!!");
        Log.v("알람", "시작!!!!!!!!!!!!!!!!!");
        Log.v("알람", "시작!!!!!!!!!!!!!!!!!");

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Log.v("알람","브캐1 !!!!!!!!!!!!!!!!!");
            myIntent.putExtra("message", "Hi !!!!!!!!!!!!!");
            Log.v("알람", "브캐2 !!!!!!!!!!!!!!!!!");
            context.startService(myIntent);
            Log.v("알람", "브캐3 !!!!!!!!!!!!!!!!!");
            Toast.makeText(context, "문자 수신 성공", Toast.LENGTH_SHORT).show();
            Log.v("알람", "브캐4 !!!!!!!!!!!!!!!!!");
        }

        Log.v("알람", "마지막!!!!!!!!!!!!!!!!!");
        Log.v("알람", "마지막!!!!!!!!!!!!!!!!!");
        Log.v("알람", "마지막!!!!!!!!!!!!!!!!!");
        Log.v("알람", "마지막!!!!!!!!!!!!!!!!!");
    }
}
*/





