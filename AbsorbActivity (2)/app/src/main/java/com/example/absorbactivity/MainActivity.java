package com.example.absorbactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private EditText et1,et2;
private TextView tv1, tv2;
private Button btn;
private int a=0, b=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et2=findViewById(R.id.sText);
        et1=findViewById(R.id.tText);
        tv1=findViewById(R.id.textView_1);
        tv2=findViewById(R.id.textView_2);
        btn=findViewById(R.id.startButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTime = et1.getText().toString();
                String getStudy = et2.getText().toString();

                Intent intent= new Intent(MainActivity.this,AbsorbActivity.class);
                intent.putExtra("extra_time",getTime);
                intent.putExtra("extra_studyName",getStudy);
                startActivityForResult(intent,1);


            }
        });


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK)
                {
                   a = data.getIntExtra("studyTime_return",0);
                   b = data.getIntExtra("stopTime_return",0);
                   tv1.setText(Integer.toString(a));
                   tv2.setText(Integer.toString(b));
                   Toast.makeText(MainActivity.this, "测试",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }


}
