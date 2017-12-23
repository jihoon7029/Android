package com.example.jihoo.hoons_cook_class_v3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jihoo on 2017-04-02.
 */

public class Login extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;


    private SharedPreferences mPre;
    private SharedPreferences.Editor mPrefEdit_Auto_login;
    private SharedPreferences.Editor mPrefEdit_ID;
    private SharedPreferences.Editor mPrefEdit_Password;

    private CheckBox checkbox_listener;
    private Button Login_button_listener;
    private Button Sign_button_listener;
    private EditText ID_editText;
    private EditText Password_editText;

    private String ID_str;
    private String Password_str;

    private ProgressDialog mProgressDialog;

    private Boolean login_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        backPressCloseHandler = new BackPressCloseHandler(this);  //뒤로가기 핸들러

        ID_editText = (EditText) findViewById(R.id.editText);
        Password_editText = (EditText) findViewById(R.id.editText2);
        mPre = getSharedPreferences("Pref1", MODE_PRIVATE);
        mPrefEdit_Auto_login = mPre.edit();
        mPrefEdit_ID = mPre.edit();
        mPrefEdit_Password = mPre.edit();

        Login_button_listener = (Button) findViewById(R.id.login_button);
        Sign_button_listener = (Button) findViewById(R.id.sign_button);
        checkbox_listener = (CheckBox) findViewById(R.id.checkBox);
        checkbox_listener.setChecked(mPre.getBoolean("Login", false));

        login_success=false;

        ////아이디 비밀번호 창을 채울것인가
        if (checkbox_listener.isChecked()) {
            ID_editText.setText(mPre.getString("ID", ""));
            Password_editText.setText(mPre.getString("Password", ""));
        }

        ///체크박스 리스너
        checkbox_listener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPrefEdit_Auto_login.putBoolean("Login", true);
                    mPrefEdit_Auto_login.commit();
                } else {
                    mPrefEdit_Auto_login.putBoolean("Login", false);
                    mPrefEdit_Auto_login.commit();
                    mPrefEdit_ID.putString("ID", "");
                    mPrefEdit_ID.commit();
                    mPrefEdit_Password.putString("Password", "");
                    mPrefEdit_Password.commit();
                }
            }
        });
        /// 회원가입 버튼 리스너
        Sign_button_listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Sign.class));

            }
        });
        ///로그인버튼 리스너
        Login_button_listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID_str = ID_editText.getText().toString();
                Password_str = Password_editText.getText().toString();
                if (ID_str.equals(""))
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_LONG).show();
                else if (Password_str.equals(""))
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                else {

                    if (checkbox_listener.isChecked()) {
                        mPrefEdit_ID.putString("ID", ID_editText.getText().toString());
                        mPrefEdit_ID.commit();
                        mPrefEdit_Password.putString("Password", Password_editText.getText().toString());
                        mPrefEdit_Password.commit();
                    }
                    Login_doing(ID_str, Password_str);
               /* if(login_success==true)
                    Login.this.finish();*/
                }
            }
        });
    }

    ////뒤로가기 오버라이드
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void Login_doing(String ID, String Password) {

        class InsertData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog=ProgressDialog.show(Login.this, "", "잠시만 기다려 주세요.", true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if(s.equals("로그인 성공"))  {
                    login_success=true;
                    Login.this.finish();
                }
                else if(s.equals("아이디 혹은 비밀번호가 틀렸습니다.")) {
                    login_success=false;
                }
            }

            protected String doInBackground(String... params) {
                try {
                    String ID = params[0];
                    String Password = params[1];
                    String link = "http://211.116.222.49:4550/login_db.php?ID="+ID+"&PASSWORD="+Password;
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    if (conn != null) {
                        conn.setConnectTimeout(600);
                        conn.setUseCaches(false);
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                    }
                    conn.connect();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Log.v("여기도요3","333333333");
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            while (true) {
                                String line = reader.readLine();
                                Log.v("여기도요2",""+line);
                                if (line == null) break;
                                else if (line.equals("로그인 성공")) {
                                    return "로그인 성공";
                                } else if (line.equals("PASSWORD ERROR") || line.equals("ID DOESN'T EXIST")) {
                                    return "아이디 혹은 비밀번호가 틀렸습니다.";
                                }
                            }
                        }
                    }
                return "서버 연결 실패";
                } catch (Exception ex) {
                    return ex.getMessage();

                }
            }
        }
        InsertData task = new InsertData();
        task.execute(ID, Password);
    }
}


////http://127.0.0.1/sign_db.php?ID=test1&PASSWORD=123&MAIL=123456@naver.com