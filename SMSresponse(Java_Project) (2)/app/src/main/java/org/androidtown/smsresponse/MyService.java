package org.androidtown.smsresponse;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;


public class MyService extends Service {
    boolean running;
    PhoneStateCheckListener phoneCheckListener;  //폰 상태체크  ( 통화가 오고있는지, 통화중인지, 그 외의 상태인지)
    boolean call_check;                 // 부재중을 체크하기 위한 변수
    boolean T_send_sms_ready = false;             // 부재중이 온 후 문자를 보낼 준비가 되었는지 확인하는 변수 ( 문자를 보낸후 즉시 false)
    String number;                              // 전화번호 저장하는 변수
    MyThread thread1;
    String message;

    @Override
    public void onCreate() {
        phoneCheckListener = new PhoneStateCheckListener();                                             //전화상태
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneCheckListener, PhoneStateListener.LISTEN_CALL_STATE);
        thread1 = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //     Log.d("slog", "onStart()");
        //     Log.d("slog", "onStart()");
        //     Log.d("slog", "onStart()");
        message = intent.getStringExtra("txt");
        if (thread1 == null) {
            thread1 = new MyThread();
            running = true;
            thread1.start();

        }
        return 0;
    }

    @Override
    public void onDestroy() {
        //    Toast.makeText(this, "서비스 오프!.", Toast.LENGTH_LONG).show();
        //   Log.i("slog1", "onDestroy()");
        //   Log.i("slog1", "onDestroy()");
        //    Log.i("slog1", "onDestroy()");
        running = false;
        thread1 = null;
        super.onDestroy();
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
//            Log.d("slog", "In Thread");
//            Log.d("slog", "In Thread");
//            Log.d("slog", "In Thread");
            while (running) {
                if (running == true && T_send_sms_ready == true) {
                    sendSMS(number);
                    T_send_sms_ready = false;
                }
            }
        }
    }

    public class PhoneStateCheckListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_IDLE) {
                if (call_check == true && number.substring(0, 3).equals(("010"))) {
                    T_send_sms_ready = true;
                } else {
                    call_check = false;
                }
            } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                number = incomingNumber;
                call_check = true;
//수신 부분 입니다.
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                call_check = false;
            }
        }
    }

    public void sendSMS(String number) {//}, String message) {
        SmsManager sms = SmsManager.getDefault();
        if (message.length() == 0) {
            message = "부재중입니다.";
        }
        sms.sendTextMessage(number, null, message, null, null);
    }
}