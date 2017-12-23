package com.example.jihoo.hoons_cook_class_v3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    private Button logout;
    private Button Menu;
    private FloatingActionButton ice_cube_button;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private Button search_button;

    private BackPressCloseHandler backPressCloseHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this,Splash.class));

        backPressCloseHandler = new BackPressCloseHandler(this);  //뒤로가기 핸들러

        logout=(Button)findViewById(R.id.logout_button);
        Menu = (Button)findViewById(R.id.Menu);
        search_button = (Button)findViewById(R.id.search_button);
        ice_cube_button = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        drawerView = findViewById(R.id.drawer);

        //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        //메뉴 버튼
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        //냉장고 버튼
        ice_cube_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ice_cube.class));
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);

            }
        });
        //검색 버튼
        search_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, select_main_item.class));
            }
        });
    }
    ////뒤로가기 오버라이드
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
   /* @Override
    protected void onStop(){
        super.onStop();
        runOnUiThread(new Runnable(){
            public void run(){
                drawerLayout.closeDrawers();
            }
        });
    }*/
}
