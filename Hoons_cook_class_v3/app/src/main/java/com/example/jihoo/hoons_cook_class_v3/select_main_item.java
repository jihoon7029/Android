package com.example.jihoo.hoons_cook_class_v3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jihoon on 2017. 5. 21..
 */

public class select_main_item extends AppCompatActivity {

    private Button search_ok;
    private Button search_cancel;
    private ProgressDialog mProgressDialog;
    private boolean first_select = false;
    private boolean second_select = false;

    private int main_image[];
    private TextView main_text[];
    private boolean check_main[];
    private String item1, item2;

    private LinearLayout _main_item[];

    private final String main[] = {"돼지고기 찌개용","등심","목살","삼겹살","소(국거리)","소(불고기)","차돌박이","닭가슴살","닭(백숙용)","닭다리",
                            "미역","고등어","김","중멸치","홍합","오징어",
                            "감자","무","숙주",
                            "라면","스팸","어묵","햄","통조림골뱅이","계란","카레가루"};

    private JSONArray recipes = null;
    private ArrayList<HashMap<String, String>> recipeList;

   private  Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_main_item);

        search_ok = (Button) findViewById(R.id.search_ok);
        search_cancel = (Button) findViewById(R.id.search_cancel);
        recipeList = new ArrayList<HashMap<String, String>>();

        check_main=new boolean[26];
        main_image = new int[26];
        main_text = new TextView[26];
        _main_item=new LinearLayout[26];
        intent = new Intent(this,show_cook_list.class);

        for(int i=0;i<26;i++){
            String resName="@drawable/main_"+(i+1);
            main_image[i]=getResources().getIdentifier(resName, "drawable",getPackageName());
        }

        for(int i=0;i<26;i++)
        {
            _main_item[i]=(LinearLayout)findViewById(getResources().getIdentifier("main_item"+(i+1),"id",getPackageName()));
            Log.v("여기다 씹새야", ""+_main_item[i]);
            _main_item[i].setOnClickListener(main_item_Listener);
            Log.v("여기여ㅛ", "1312321");
            ((ImageView)_main_item[i].findViewById(R.id._img)).setImageResource(main_image[i]);
            ((TextView)_main_item[i].findViewById(R.id._text)).setText(main[i]);
        }

        main_text=new TextView[26];
        item1="";
        item2="";

        //ok_button
        search_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // searching();
                //ㅇㅋ 버튼 눌렀을 때
                if(second_select) {
                    intent.putExtra("second",true);
                    intent.putExtra("item1",item1.toString());
                    intent.putExtra("item2",item2.toString());
                    startActivity(intent);
            //        searching(item1, item2);
                }else if(first_select){
                    intent.putExtra("second",false);
                    intent.putExtra("item1",item1);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "아이템을 선택해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
        //cancel_button
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    //Imgbutton
    View.OnClickListener main_item_Listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            LinearLayout linear = (LinearLayout) v;
            ImageView temp_img;
            TextView temp_textView;
            int count = 0;
            for (LinearLayout temp : _main_item) {
                if (temp == linear) {
                    if (check_main[count]) {
                        temp_img=(ImageView) linear.findViewById(R.id._img);
                        temp_img.setImageResource(main_image[count]);
                        if(second_select==true) {
                            second_select = false;
                            item2="";
                        }
                        else if(first_select==true) {
                            first_select = false;
                            item1="";
                        }
                    }
                    else {
                        if(!second_select) {
                            temp_img = (ImageView) linear.findViewById(R.id._img);
                            temp_img.setImageResource(R.drawable.check);
                            if (first_select == false) {
                                first_select = true;
                                temp_textView = (TextView) v.findViewById(R.id._text);
                                item1 = temp_textView.getText().toString();
                            } else if (second_select == false) {
                                second_select = true;
                                temp_textView = (TextView) v.findViewById(R.id._text);
                                item2 = temp_textView.getText().toString();
                            }
                        }
                        else {
                            Toast.makeText(select_main_item.this, "최대 2개까지만 고를 수 있습니다", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    check_main[count] = !check_main[count];
                    break;
                }
                count++;
            }

        }
    };
}
