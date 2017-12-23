package com.example.jihoo.hoons_cook_class_v3;
//<avtivity android:windowSoftInputMode="adjustResize"/><avtivity android:windowSoftInputMode="adjustResize"/>
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jihoon on 2017. 5. 10..
 */

public class Sign extends AppCompatActivity {
    private Button cancel_listener;
    private Button ok_listener;
    private EditText ID;
    private EditText Password;
    private EditText Password_check;
    private EditText Email;
    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        ID = (EditText) findViewById(R.id.sign_ID);
        Password = (EditText) findViewById(R.id.sign_Password);
        Password_check = (EditText) findViewById(R.id.sign_Passwordcheck);
        Email = (EditText) findViewById(R.id.sign_Email);

        cancel_listener = (Button) findViewById(R.id.cancel_button);
        ok_listener = (Button) findViewById(R.id.ok_button);


        ///취소버튼 리스너
        cancel_listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sign.this.finish();
            }
        });
        ///ok버튼 리스너
        ok_listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Password.getText().toString().equals(Password_check.getText().toString()))
                    insertToDatabase(ID.getText().toString(),Password.getText().toString(),Email.getText().toString());
                else
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지않습니다 비밀번호를 일치하게 입력해주세요",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void insertToDatabase(String ID, String Password, String Email) {

        class InsertData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = ProgressDialog.show(Sign.this, "", "잠시만 기다려 주세요.", true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
            protected String doInBackground(String... params) {
                try
                {
                    String ID = params[0];
                    String Password = params[1];
                    String Email = params[2];
                  /*  String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8") + "&";
                    data += URLEncoder.encode("PASSWORD", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8") + "&";
                    data += URLEncoder.encode("MAIL", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");*/
                    String link = "http://211.116.222.49:2724/sign_db.php?ID="+ID+"&PASSWORD="+Password+"&MAIL="+Email;
                    //  link += "?"+data;
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    if (conn != null) {
                        conn.setConnectTimeout(600);
                        conn.setUseCaches(false);
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                    //    OutputStream os = conn.getOutputStream();
                        // BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                    //    os.write(data.getBytes("UTF-8"));
                    //    os.flush();
                    //    os.close();
                        conn.connect();
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                while (true) {
                                    String line = reader.readLine();
                                    if (line == null) break;
                                     else if (line.equals("success")) {
                                        return "회원가입 성공";
                                    } else if (line.equals("fail_overlap")) {
                                        return "아이디 중복";
                                    }
                                }
                            }
                            else  return "서버 연결 실패";
                        }
                    }
                    return "서버 연결 실패";
                } catch (Exception ex) {
                    return "실패"+ex.getMessage();
                    // return new String("Exception: " + ex.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(ID,Password,Email);
    }
}

