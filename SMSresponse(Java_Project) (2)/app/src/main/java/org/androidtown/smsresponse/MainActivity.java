package org.androidtown.smsresponse;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    ToggleButton tButton;                       // 버튼
    AudioManager clsAudioManager;          // 폰 진동,벨,무음 체크
    SharedPreferences mPref;                //토글상태저장
    SharedPreferences.Editor mPrefEdit;
    SharedPreferences mPre_audio;               //무음, 진동 상태 저장
    SharedPreferences.Editor mPrefEdit_audio;
    NotificationManager manager;
    NotificationCompat.Builder builder;
    TextView textView;
    String text = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, Splash.class));
        intent = new Intent(this, MyService.class);
        clsAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);    //수신모드 체크매니저
        tButton = (ToggleButton) findViewById(R.id.toggleButton);
        textView = (TextView) findViewById(R.id.textView1);
        mPref = getSharedPreferences("Pref1", 0);            //토글상태 저장
        mPrefEdit = mPref.edit();
        mPre_audio = getSharedPreferences("Pref2", 0);        //무음 혹은 진동
        mPrefEdit_audio = mPre_audio.edit();
        if (mPref.getBoolean("Toggle", false))
            tButton.setChecked(true);
        else
            tButton.setChecked(false);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setSmallIcon(R.drawable.noti_icon_48)
                .setContentTitle("실행중")
                .setContentText("자동응답기가 실행중입니다.")
                .setAutoCancel(false) // 알림바에서 자동 삭제
                .setOngoing(true);


        final SharedPreferences shPref = getSharedPreferences("MyPref", 0);
        text = shPref.getString("SaveText", "");
        textView.setText(shPref.getString("SaveText", ""));

        Button changemessagebutton = (Button) findViewById(R.id.textChange);
        intent.putExtra("txt", text);
        changemessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("문구변경");
                dialog.setContentView(R.layout.activity_messagepage);
                dialog.show();

                final EditText editText = (EditText) dialog.findViewById(R.id.editText1);
                Button okButton = (Button) dialog.findViewById(R.id.okButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text = editText.getText().toString();
                        textView.setText(text);
                        SharedPreferences.Editor prefEditor = shPref.edit();
                        prefEditor.putString("SaveText", text);
                        intent.putExtra("txt", text);
                        if (tButton.isChecked() == true) {
                            startService(intent);
                        }
                        prefEditor.apply();
                        dialog.cancel();
                    }
                });
                editText.setText(text);
                editText.setSelection(editText.length());

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });
        Button removemessagebutton = (Button) findViewById(R.id.textRemove);
        removemessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEditor = shPref.edit();
                prefEditor.putString("SaveText", "");
                prefEditor.apply();
                text = shPref.getString("SaveText", "");

                intent.putExtra("txt", text);
                if (tButton.isChecked() == true) {
                    startService(intent);
                }
                textView.setText(text);
            }
        });
    }
    public void Toggle_clicked(View v) {
        if (tButton.isChecked() == true) {
            manager.notify(1, builder.build());
            intent.putExtra("txt", text);
            startService(intent);
            mPrefEdit.putBoolean("Toggle", true);
            mPrefEdit.commit();

            switch (clsAudioManager.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    mPrefEdit_audio.putString("Audio", "silent");
                    mPrefEdit_audio.commit();
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    mPrefEdit_audio.putString("Audio", "vibrate");
                    mPrefEdit_audio.commit();
                    clsAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    Toast.makeText(this, "무음모드가 되었습니다.", Toast.LENGTH_LONG).show();
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    mPrefEdit_audio.putString("Audio", "nomal");
                    mPrefEdit_audio.commit();
                    clsAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    Toast.makeText(this, "무음모드가 되었습니다.", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {

            stopService(intent);
            manager.cancel(1);
            mPrefEdit.putBoolean("Toggle", false);
            mPrefEdit.commit();

            if (mPre_audio.getString("Audio", "nomal").equals("silent"))
                clsAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            else if (mPre_audio.getString("Audio", "nomal").equals("vibrate")) {
                clsAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Toast.makeText(this, "무음모드가 해제되었습니다.", Toast.LENGTH_LONG).show();
            } else if (mPre_audio.getString("Audio", "nomal").equals("nomal")) {
                clsAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(this, "무음모드가 해제되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
