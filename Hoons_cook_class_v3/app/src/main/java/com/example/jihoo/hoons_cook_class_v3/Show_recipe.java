package com.example.jihoo.hoons_cook_class_v3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jihoon on 2017. 6. 22..
 */

public class Show_recipe extends AppCompatActivity {
    private Intent intent;
    private ProgressDialog mProgressDialog;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_ITEM1 = "item1";
    private static final String TAG_ITEM2 = "item2";
    private static final String TAG_NUM = "num";
    private static final String TAG_URL1 = "url1";
    private static final String TAG_URL2 = "url2";
    private static final String TAG_SUB1 = "sub1";
    private static final String TAG_SUB2 = "sub2";
    private static final String TAG_SUB3 = "sub3";
    private static final String TAG_SUB4 = "sub4";
    private static final String TAG_BASE = "base";
    private static final String TAG_STEP = "step";
    private static final String TAG_TIP = "tip";
    private static final String TAG_ANOTHER1 = "another1";
    private static final String TAG_ANOTHER2 = "another2";

    //private ArrayList<HashMap<String, String>> recipeList;
    private  HashMap<String, String> one_recipe;
    private ArrayList<String> my_item;
    private String myJSON;
    private JSONArray recipes = null;
    private String recipe_num;
    private Thread mThread;//url로 이미지 불러올 작업할 쓰레드
    private Bitmap bitmap;

    private static SQLiteDatabase DB;
    private static DatabaseHelper DBH;
    private static final String DATABASE_NAME = "Hoons_cook_class.db";

    private String sub_="";
    boolean checked =false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_recipe);
        intent = getIntent();
        one_recipe = new HashMap<String,String>();
        my_item = new ArrayList<String>();
        recipe_num=intent.getStringExtra("recipe_num");
        searching(recipe_num);
        try {
            DB = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            DBH = new DatabaseHelper(this);
        } catch (Exception ex) {
            ex.getMessage();
        }

    }
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            recipes = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject c = recipes.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String item1 = c.getString(TAG_ITEM1);
                String item2 = c.getString(TAG_ITEM2);
                int num = c.getInt(TAG_NUM);
                String url1 = c.getString(TAG_URL1);
                String url2 = c.getString(TAG_URL2);
                String sub1 = c.getString(TAG_SUB1);
                String sub2 = c.getString(TAG_SUB2);
                String sub3 = c.getString(TAG_SUB3);
                String sub4 = c.getString(TAG_SUB4);
                String base = c.getString(TAG_BASE);
                String step = c.getString(TAG_STEP);
                String tip = c.getString(TAG_TIP);
                String another1 = c.getString(TAG_ANOTHER1);
                String another2 = c.getString(TAG_ANOTHER2);

                one_recipe = new HashMap<String, String>();

                one_recipe.put(TAG_NAME, name);
                one_recipe.put(TAG_ITEM1, item1);
                one_recipe.put(TAG_ITEM2, item2);
                one_recipe.put(TAG_NUM, num+"");
                one_recipe.put(TAG_URL1, url1);
                one_recipe.put(TAG_URL2, url2);
                one_recipe.put(TAG_SUB1,sub1);
                one_recipe.put(TAG_SUB2,sub2);
                one_recipe.put(TAG_SUB3,sub3);
                one_recipe.put(TAG_SUB4,sub4);
                one_recipe.put(TAG_BASE,base);
                one_recipe.put(TAG_STEP,step);
                one_recipe.put(TAG_TIP,tip);
                one_recipe.put(TAG_ANOTHER1,another1);
                one_recipe.put(TAG_ANOTHER2,another2);

                ImageView img =(ImageView)findViewById(R.id.recipe_img);
                TextView main=(TextView)findViewById(R.id.recipe_main_item);
                TextView sub=(TextView)findViewById(R.id.recipe_sub_item);
                TextView recipe_base=(TextView)findViewById(R.id.recipe_base_item);
                TextView another=(TextView)findViewById(R.id.recipe_another_item);
                TextView recipe=(TextView)findViewById(R.id.recipe_recipe);
                TextView recipe_tip=(TextView)findViewById(R.id.recipe_tip);

                mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://" + one_recipe.get(TAG_URL1));
                            Log.v("URL", "" + url);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            //BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 10240);
                            bitmap = BitmapFactory.decodeStream(is);
                            is.close();

                        } catch (IOException ex) { }
                    }
                };
                mThread.start();
                try {
                    mThread.join();
                    mThread = null;
                    img.setImageBitmap(bitmap);
                } catch (Exception ex) { }
                my_item=DBH.true_check(DB);
                for(int k=1;k<=4;k++) {
                    String s = "sub" + k;
                    String temp1="";
                    String temp2="";
                    for (int j = 0; j < my_item.size(); j++) {

                        temp1=one_recipe.get(s).toString();
                        temp2=my_item.get(j).toString();
                        if (temp1.equals(temp2)==true) {checked=true; break;}
                        else{checked=false;}
                    }
                    if(checked==true)
                        sub_ += one_recipe.get(s)+"  ";
                    else
                        sub_ += "<font color=red>" + one_recipe.get(s) + "</font>"+"  ";
                }

                main.setText("메인재료 : "+one_recipe.get(TAG_ITEM1)+"  "+one_recipe.get(TAG_ITEM2));
                if(sub_.equals(""))
                    sub.setText("부재료 : "+one_recipe.get(TAG_SUB1)+"  "+one_recipe.get(TAG_SUB2)+"  "+one_recipe.get(TAG_SUB3)+"  "+one_recipe.get(TAG_SUB4));
                else {
                    sub_="부재료 : "+sub_;
                    sub.setText(Html.fromHtml(sub_));
                }
                another.setText("대체 가능 재료 : "+one_recipe.get(TAG_ANOTHER1)+one_recipe.get(TAG_ANOTHER2));
                recipe_base.setText("기본 재료 : "+one_recipe.get(TAG_BASE));
                recipe.setText("요리순서\n"+one_recipe.get(TAG_STEP));
                if(!(one_recipe.get(TAG_TIP).equals("")))
                    recipe_tip.setText("요리팁\n"+one_recipe.get(TAG_TIP));
            }
        } catch (JSONException e) {
            Log.v("JSON에러에여!!!!", "" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void searching(String num) {

        class search_data extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.v("여기여", "onPreExecute");
                mProgressDialog = ProgressDialog.show(Show_recipe.this, "", "잠시만 기다려 주세요.", true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                mProgressDialog.dismiss();
                myJSON = result;
                showList();
            }

            //http://127.0.0.1/searching_db.php?ITEM1=라&ITEM2=가
            protected String doInBackground(String... params) {
                try {
                    String num = params[0].trim();
                    String link;
                    StringBuilder sb = new StringBuilder();

                        link = "http://211.116.222.49:4550/recipe_search.php?NUM="+num;
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
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            while (true) {
                                String line = reader.readLine();
                                if (line == null) {
                                    sb.append("\n");
                                    return sb.toString().trim();
                                }
                                else if (line.equals("쿼리문 실패")) {
                                    return "false";
                                }
                                else
                                    sb.append(line + "\n");
                            }
                        }
                    }
                    return "서버 연결 실패";
                } catch (Exception ex) {
                    return ex.getMessage();

                }
            }
        }
        search_data task = new search_data();
        task.execute(num);
    }

}
