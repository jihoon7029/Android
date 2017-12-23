package kr.ac.inje.jihoon7029oasis.smart_home_delivery_box;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {//AppCompatActivity {

    private static final int MY_PERMISSIONS_RECIEVE_SMS_CONTACTS = 1;
    private static final int MY_PERMISSIONS_READ_SMS_CONTACTS = 2;
    private static final int MY_PERMISSIONS_RECIEVE_MMS_CONTACTS = 3;
    public static SQLiteDatabase DB;
    public static DatabaseHelper DBH;
    Button Button1;
    Button Button2;
    Button Button3;
    Button Button4;


    Intent List_activity_intent;


    int permissionCheck1;
    int permissionCheck2;
    int permissionCheck3;

    public static final String DATABASE_NAME = "Smart_box.db";
    public static final String TABLE_NAME = "Smart_table_1";


    SharedPreferences Master_Key;


    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
  //  private static final int REQUEST_CONNECT_DEVICE_SECURE=1;
 //   private static final int REQUEST_CONNECT_DEVICE_INSECURE=2;
    private static final int REQUEST_ENABLE_BT=1;
    private static final int REQUEST_MASTER=2;
    private BluetoothAdapter mBluetoothAdapter=null;
    private BluetoothDevice mRemoteDevie=null;
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, Splash.class));
        setContentView(R.layout.activity_main);

        List_activity_intent = new Intent(this, ListActivity.class);
        List_activity_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        try{
            DB=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
            DBH= new DatabaseHelper(this);
            DBH.onCreate(DB);
            //  DB= openOrCreateDatabase("samrtbox1",MODE_PRIVATE,null);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if(permissionCheck1== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {

                // 이 권한을 필요한 이유를 설명해야하는가?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)) {

                    // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                    // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_RECIEVE_SMS_CONTACTS);
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_RECIEVE_SMS_CONTACTS);

                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
                }
            }
        }
        permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if(permissionCheck2== PackageManager.PERMISSION_DENIED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_SMS)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_READ_SMS_CONTACTS);
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_READ_SMS_CONTACTS);
                }
            }
        }
        permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_MMS);

        if(permissionCheck3== PackageManager.PERMISSION_DENIED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_MMS)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_MMS)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_MMS}, MY_PERMISSIONS_RECIEVE_MMS_CONTACTS);
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_MMS}, MY_PERMISSIONS_RECIEVE_MMS_CONTACTS);
                }
            }
        }

        Button1 = (Button) findViewById(R.id.button);
        Button2 = (Button) findViewById(R.id.button2);
        Button3 = (Button) findViewById(R.id.button3);
        Button4 = (Button) findViewById(R.id.button4);


        Master_Key = getSharedPreferences("Master_Key", MODE_PRIVATE);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();


        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(List_activity_intent);
            }
        });

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //startActivity(new Intent(MainActivity.this,dialog.class));
                startActivityForResult(new Intent(MainActivity.this,dialog.class), REQUEST_MASTER);
            }
        });

        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBluetoothAdapter.isEnabled())
                {
                    Intent enableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
                }
                else
                    selectDevice();
            }
        });
        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = "";
                    Cursor c1 = DB.rawQuery("select name from " + TABLE_NAME, null);
                    c1.moveToFirst();
                    for (int i = 1; i <= c1.getCount(); i++) {
                        //    Log.i("여기다!!!!!!!!!!!",c1.getString(0));
                        str += c1.getString(0);
                        str += '@';
                        sendData(str);

                        Thread.sleep(100);

                        str = "";
                        c1.moveToNext();
                    }

                }catch (InterruptedException e) { }
           //     if(mSocket!=null)
            //    sendData(str);
         //       else
                if(mSocket==null)
                    Toast.makeText(getApplicationContext(), "블루투스 연결이 안되어있습니다.", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECIEVE_SMS_CONTACTS:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    Toast.makeText(this, "권한1사용을 동의 해주셔야 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                    permissionCheck1=0;
                    finish();
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
            case MY_PERMISSIONS_READ_SMS_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                    Toast.makeText(this, "권한2사용을 동의 해주셔야 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                    permissionCheck2=0;
                    finish();
                }
                return;
            case MY_PERMISSIONS_RECIEVE_MMS_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    Toast.makeText(this, "권한3사용을 동의 해주셔야 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                    permissionCheck3=0;
                    finish();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // startActivityForResult 를 여러번 사용할 땐 이런 식으로
        // switch 문을 사용하여 어떤 요청인지 구분하여 사용함.
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) { // 블루투스 활성화 상태
                    selectDevice();
                }
                else if(resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
                    Toast.makeText(getApplicationContext(), "블루투스를 활성화 해주세요",
                            Toast.LENGTH_LONG).show();
          //          finish();
                }
                break;
            case REQUEST_MASTER:
                if(resultCode == RESULT_OK) { // 마스터키 전송
                    if(mSocket!=null) {
                        String resultMsg=data.getStringExtra("Key");
                        String temp_master = "M";
                        temp_master += resultMsg;
                        send_master_key(temp_master);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "블루투스 연결이 안되어있습니다.", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    void selectDevice() {
        // 블루투스 디바이스는 연결해서 사용하기 전에 먼저 페어링 되어야만 한다
        // getBondedDevices() : 페어링된 장치 목록 얻어오는 함수.
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();

        if(mPariedDeviceCount == 0 ) { // 페어링된 장치가 없는 경우.
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
       //     finish(); // App 종료.
        }
        // 페어링된 장치가 있는 경우.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");

        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices) {
            // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
            listItems.add(device.getName());
        }
        listItems.add("취소");  // 취소 항목 추가.


        // CharSequence : 변경 가능한 문자열.
        // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if(item == mPariedDeviceCount) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
      //              finish();
                }
                else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    connectToSelectedDevice(items[item].toString());
                }
            }

        });

        builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }

    void connectToSelectedDevice(String selectedDeviceName) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        // java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와
            //                                           통신할 수 있는 소켓을 생성함.
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는
            //  BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            // 1. 데이터를 보내기 위한 OutputStrem
            mOutputStream = mSocket.getOutputStream();

        }catch(Exception e) { // 블루투스 연결 중 오류 발생
            Toast.makeText(getApplicationContext(),
                    "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        //    finish();  // App 종료
        }
    }
    BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : 페어링 된 기기 목록을 얻어옴.
        BluetoothDevice selectedDevice = null;
        // getBondedDevices 함수가 반환하는 페어링 된 기기 목록은 Set 형식이며,
        // Set 형식에서는 n 번째 원소를 얻어오는 방법이 없으므로 주어진 이름과 비교해서 찾는다.
        for(BluetoothDevice deivce : mDevices) {
            // getName() : 단말기의 Bluetooth Adapter 이름을 반환
            if(name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }
    void sendData(String msg) {
    //    msg += mStrDelimiter;  // 문자열 종료표시 (\n)
        try{
            // getBytes() : String을 byte로 변환
            // OutputStream.write : 데이터를 쓸때는 write(byte[]) 메소드를 사용함.
            // byte[] 안에 있는 데이터를 한번에 기록해 준다.
            mOutputStream.write(msg.getBytes());  // 문자열 전송.
            Toast.makeText(getApplicationContext(), "데이터 전송 완료",
                    Toast.LENGTH_LONG).show();
        }catch(Exception e) {  // 문자열 전송 도중 오류가 발생한 경우
            Toast.makeText(getApplicationContext(), "데이터 전송중 오류가 발생",
                    Toast.LENGTH_LONG).show();
        //    finish();  // App 종료
        }
    }
    void send_master_key(String msg) {
        //    msg += mStrDelimiter;  // 문자열 종료표시 (\n)
        try{
            // getBytes() : String을 byte로 변환
            // OutputStream.write : 데이터를 쓸때는 write(byte[]) 메소드를 사용함.
            // byte[] 안에 있는 데이터를 한번에 기록해 준다.
            mOutputStream.write(msg.getBytes());  // 문자열 전송.
            Toast.makeText(getApplicationContext(), "마스터키 전송 완료",
                    Toast.LENGTH_LONG).show();
        }catch(Exception e) {  // 문자열 전송 도중 오류가 발생한 경우
            Toast.makeText(getApplicationContext(), "데이터 전송중 오류가 발생",
                    Toast.LENGTH_LONG).show();
            //    finish();  // App 종료
        }
    }
}




