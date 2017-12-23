package com.example.jihoo.hoons_cook_class_v3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jihoon on 2017. 6. 18..
 */

public class show_cook_list extends AppCompatActivity {
    private ListView _listView;

    private ProgressDialog mProgressDialog;
    private boolean first_select = false;
    private boolean second_select = false;

    private String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_NUM = "num";
    private static final String TAG_URL2 = "url2";

    private JSONArray recipes = null;
    private ArrayList<HashMap<String, String>> recipeList;

    private ArrayList<String> items;

    private Intent intent;
    private Boolean second_check;
    private String item1;
    private String item2;

    private Thread mThread;//url로 이미지 불러올 작업할 쓰레드
    private Bitmap bitmap;
    private  CustomAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_list);

        items = new ArrayList<String>();
        intent = getIntent();
        second_check=intent.getBooleanExtra("second",false);
        Log.v("세컨드 체크!!!!!", ""+second_check);
        if(second_check) {
            item1 = intent.getStringExtra("item1");
            item2 = intent.getStringExtra("item2");

            searching(item1, item2);
        }
        else{
            item1 = intent.getStringExtra("item1");
            Log.v("item2222222", ""+item1+"       ");
            searching(item1,"");
        }
        recipeList= new ArrayList<HashMap<String, String>>();

        _listView = (ListView)this.findViewById(R.id.listview_);


        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(show_cook_list.this, ""+recipeList.get(position).get(TAG_NAME), Toast.LENGTH_SHORT).show();
                String recipe_num = recipeList.get(position).get(TAG_NUM);
                Intent intent2 = new Intent(show_cook_list.this,Show_recipe.class);
                intent2.putExtra("recipe_num",recipe_num);
                startActivity(intent2);
            }
        });


    }

    private class CustomAdapter extends ArrayAdapter<String> {//implements View.OnClickListener{//AdapterView.OnItemClickListener {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        @Override
        public int getCount() {
            super.getCount();
            Log.v("ㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑ", "" + items.size());
            return items.size(); //items 개수를 리턴

        }

        @Override
        public long getItemId(int position) {
            Log.v("ㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗ", "" + position);
            return position;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.custom_list_item, parent, false);
            }
            // ImageView 인스턴스
            ImageView imageView_ = (ImageView) v.findViewById(R.id.imageView_);
            // 리스트뷰의 아이템에 이미지를 변경한다.
            mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://" + items.get(position));
                        Log.v("URL", "" + url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        //BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 10240);
                        bitmap = BitmapFactory.decodeStream(is);
                        is.close();

                    } catch (IOException ex) {
                        Log.v("EXXXX1", "" + ex);
                    }
                }
            };
            mThread.start();
            try {
                mThread.join();
                mThread = null;
                imageView_.setImageBitmap(bitmap);
                TextView textView = (TextView) v.findViewById(R.id.textView);
                textView.setText(recipeList.get(position).get(TAG_NAME));
            } catch (Exception ex) {
                Log.v("EXXXX1", "" + ex);
            }//InterruptedException e){Log.v("EXXXXXX2", ""+e);}
            return v;
        }
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            recipes = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject c = recipes.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                int num = c.getInt(TAG_NUM);
                String url2 = c.getString(TAG_URL2);
                HashMap<String, String> one_recipe = new HashMap<String, String>();

                one_recipe.put(TAG_NAME, name);
                one_recipe.put(TAG_NUM, num + "");
                one_recipe.put(TAG_URL2,url2);
                recipeList.add(one_recipe);

                items.add(url2);

            }
            adapter = new CustomAdapter(this, 0, items);
            _listView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.v("JSON에러에여!!!!", "" + e.getMessage());
            e.printStackTrace();
        }
    }
//    AdapterView.OnItemClickListener Listview_Listener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //클릭된 아이템의 위치를 이용하여 데이터인 문자열을 Toast로 출력
//            Toast.makeText(show_cook_list.this, items.get(position), Toast.LENGTH_SHORT).show();
//
//        }
//    }

    public void searching(String item1, String item2) {

        class search_data extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.v("여기여", "onPreExecute");
                mProgressDialog = ProgressDialog.show(show_cook_list.this, "", "잠시만 기다려 주세요.", true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.v("여기여ㅛ", "onPostExecute");
                mProgressDialog.dismiss();
                Log.v("여기여???", ""+result);
                myJSON = result;
                showList();
            }

           // http://127.0.0.1/searching_db.php?ITEM1=라&ITEM2=가
            protected String doInBackground(String... params) {
                try {
                    String item1 = params[0].trim();
                    String item2 = params[1].trim();
                    String link;
                    StringBuilder sb = new StringBuilder();

                    if (second_check == true)
                        link = "http://211.116.222.49:4550/searching_db.php?ITEM1=" + item1 + "&ITEM2=" + item2 + "&CHOICE=list";
                    else
                        link = "http://211.116.222.49:4550/searching_db.php?ITEM1=" + item1+"&CHOICE=list";

                    Log.v("주소확인!!!!!!",""+link);
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

                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                            while(true){
                                String line = reader.readLine();
                                Log.v("여기도 함 봐여???", ""+line);
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
        Log.v("item1", ""+item1+"       "+item2);
        task.execute(item1, item2);
    }
}
