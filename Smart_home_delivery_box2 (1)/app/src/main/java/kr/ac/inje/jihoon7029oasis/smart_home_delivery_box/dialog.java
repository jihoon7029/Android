package kr.ac.inje.jihoon7029oasis.smart_home_delivery_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * Created by jihoo on 2016-11-21.
 */

public class dialog extends Activity  {

  //  Dialog dialog;
    Button Button3;
    Button Button4;
    EditText EdT1;
    EditText EdT2;
    EditText EdT3;
    TextView TV1;

    SharedPreferences Master_Key;
    SharedPreferences.Editor Master_Key_editor;
    static final boolean first=true;
    private View.OnClickListener mCorkyListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.masterkey_dialog);
        dialog.show();*/

        setContentView(R.layout.masterkey_dialog);

        Button3=(Button)findViewById(R.id.Master_Key_Cancel_Button);
        Button4=(Button)findViewById(R.id.Master_Key_OK_Button);
        EdT1=(EditText)findViewById(R.id.editText2);
        EdT2=(EditText)findViewById(R.id.editText3);
        EdT3=(EditText)findViewById(R.id.editText4);
        TV1 = (TextView)findViewById(R.id.textView5);

        Master_Key = getSharedPreferences("Master_Key", MODE_PRIVATE);
        Master_Key_editor =Master_Key.edit();
        if(Master_Key.getBoolean("First",true)==true){
            Master_Key_editor.putInt("Master_Key", 0000);
            Master_Key_editor.apply();
            Master_Key_editor.putBoolean("First",false);
            Master_Key_editor.apply();
        }

             mCorkyListener = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.Master_Key_Cancel_Button:
                        dialog.this.finish();
                        // do something when the corky is clicked

                        break;
                    case R.id.Master_Key_OK_Button:
                        String temp_String1=EdT1.getText().toString();
                        String temp_String2=EdT2.getText().toString();
                        String temp_String3=EdT3.getText().toString();
                        if(temp_String1.equals("") || temp_String2.equals("") || temp_String3.equals("")) {
                            TV1.setVisibility(View.VISIBLE);
                            }
                        else
                        {
                            int now_password = Integer.parseInt(temp_String1);
                            int new_password = Integer.parseInt(temp_String2);
                            int Masterkey= Master_Key.getInt("Master_Key", 1);
                            if (now_password == Masterkey && temp_String2.equals(temp_String3)) {
                                Master_Key_editor.remove("Master_Key");
                                Master_Key_editor.putInt("Master_Key", new_password);
                                Master_Key_editor.apply();
                                Intent intent = new Intent();
                                intent.putExtra("Key", temp_String2);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else
                                TV1.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        Button3.setOnClickListener(mCorkyListener);
        Button4.setOnClickListener(mCorkyListener);

              /*  Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.this.finish();
            }
        });

        Button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EdT1.getText().toString()!="" && EdT2.getText().toString()!=""&& EdT3.getText().toString()!="") {
                            int now_password = Integer.parseInt(EdT1.getText().toString());
                            int new_password = Integer.parseInt(EdT2.getText().toString());
                            int new_password_check = Integer.parseInt(EdT3.getText().toString());
                            if (now_password == Master_Key.getInt("password", 0000) && new_password == new_password_check) {
                                Master_Key_editor.remove("password");
                                Master_Key_editor.putInt("password", new_password);
                                dialog.this.finish();
                            }
                            else
                                TV1.setVisibility(View.VISIBLE);
                        }
                        else
                            TV1.setVisibility(View.VISIBLE);
            }
        });

*/

    }

}
