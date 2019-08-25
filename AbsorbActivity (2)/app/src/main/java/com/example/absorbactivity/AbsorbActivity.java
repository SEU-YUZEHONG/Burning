package com.example.absorbactivity;

import android.content.Intent;
import android.os.Bundle;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;

public class AbsorbActivity extends AppCompatActivity implements View.OnClickListener,MyInterface {
    private ImageButton playVideo, stopTime, finishTime;
    private TextView surTime;
    private TextView time;
    private TextView planName;
    private int studyTimeSum = 0;
    private int stopTimeSum = 0;
    private boolean isContinue = false;
    private int timeMain = 0;
    private int timeStopFix = 180;
    private final int spTime = 180;
    private int syTime = 0;
    String studyName;
    private Timer timer = null;
    private TimerTask task = null;
    private AlertDialog dialog_stop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //利用intent得到本次学习时间和学习名称
        Intent intent = getIntent();
        String time_main2string = intent.getStringExtra("extra_time");


        timeMain = Integer.valueOf(time_main2string);
        syTime = timeMain;
        studyName = intent.getStringExtra("extra_studyName");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absorb);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = findViewById(R.id.imageView);
        view.getBackground().setAlpha(80);
        initView();
        startTimeMain();

    }



    private void initView() {
        playVideo = findViewById(R.id.playvideo);
        stopTime = findViewById(R.id.stoptime);
        finishTime = findViewById(R.id.finish);
        time = findViewById(R.id.time);
        planName = findViewById(R.id.plan_name);
        playVideo.setOnClickListener(this);
        stopTime.setOnClickListener(this);
        finishTime.setOnClickListener(this);
        planName.setText(studyName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                stopTimeMain();
                showDialogFinish();
                break;
            case R.id.playvideo:
                break;
            case R.id.stoptime:
                stopTimeMain();
                showDialogStop();

                break;
            default:
                break;
        }

    }
    //打印时间
    private Handler mHandlerMain = new Handler() {
        public void handleMessage(Message msg) {
            time.setText(msg.obj.toString() + "");
            startTimeMain();
        };
    };
    //打印时间
    private Handler mHandlerStop = new Handler() {
        public void handleMessage(Message msg) {
            surTime.setText(msg.obj.toString() + "");
            startTimeStop();
        };
    };

    public void startTimeMain() {

        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (timeMain > 0) {   //加入判断不能小于0
                    timeMain--;
                    Message message = mHandlerMain.obtainMessage();
                    String timeText = TimerUtils.getTime(timeMain);
                    message.obj = timeText;
                    mHandlerMain.sendMessage(message);
                }
            }
        };
        timer.schedule(task, 1000);
    }

    public void startTimeStop() {


        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (timeStopFix > 0) {   //加入判断不能小于0
                    timeStopFix--;
                    Message message = mHandlerStop.obtainMessage();
                    String timeText = TimerUtils.getTime(timeStopFix);
                    message.obj = timeText;
                    mHandlerStop.sendMessage(message);
                }
                if(timeStopFix == 0)
                {
                    isContinue = true;
                }
            }
        };
        timer.schedule(task, 1000);

        //暂停时间结束，强制继续学习
        if(isContinue == true) {
            Toast.makeText(AbsorbActivity.this, "暂停时间结束，请继续学习", Toast.LENGTH_SHORT).show();
            isContinue = false;

            stopTimeSum += spTime - timeStopFix;
            stopTimeStop();
            dialog_stop.dismiss();;
            dialog_stop = null;
            timeStopFix = spTime;

            startTimeMain();
        }
    }


    //专注界面暂停
    public void stopTimeMain(){

        timer.cancel();
        timer = null;
        task = null;
    }

    //暂停界面暂停
    public void stopTimeStop(){
        timer.cancel();
        timer = null;
        task = null;
    }

    //转到结束dialog
    public void showDialogFinish(){
        MyDialogFinish myDialogFinish = new MyDialogFinish();
        myDialogFinish.setCancelable(false);
        myDialogFinish.show(getFragmentManager(),"是否退出");

    }

    ////结束dialog yes按钮下操作
    public void buttonYesClicked() {
        stopTimeSum += spTime - timeStopFix;
        studyTimeSum = syTime - timeMain;

        Toast.makeText(AbsorbActivity.this, "退出本次学习",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("studyTime_return", studyTimeSum);
        intent.putExtra("stopTime_return", stopTimeSum);
        setResult(RESULT_OK,intent);

        super.finish();

    }

    //结束dialog no按钮下操作
    public void buttonNoClicked() {
        startTimeMain();
        Toast.makeText(AbsorbActivity.this, "学习继续",Toast.LENGTH_SHORT).show();

    }


    //定义暂停dialog
    public void showDialogStop(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        final View v =  getLayoutInflater().inflate(R.layout.dialoglayout,null);
        surTime = v.findViewById(R.id.surtime);
        builder.setView(v);
        builder.setCancelable(false);

        //CirCleBarView mCirCleBarView = new CirCleBarView(v.getContext());

       // mCirCleBarView = v.findViewById(R.id.upload_pic_count);

        //mCirCleBarView.setProgress(50);

        startTimeStop();

        builder.setPositiveButton("继续学习", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopTimeStop();
                startTimeMain();
                stopTimeSum += spTime - timeStopFix;
                timeStopFix = spTime;
                Toast.makeText(AbsorbActivity.this, "学习继续",Toast.LENGTH_SHORT).show();
            }
        });
        dialog_stop = builder.show();

    }

}

class TimerUtils {

    public static String getTime(int second) {

        if (second < 10) {//小于十秒定义个位，以下皆考虑各种情况

            return "00:0" + second;

        }

        if (second < 60) {

            return "00:" + second;

        }

        if (second < 3600) {

            int minute = second / 60;

            second = second - minute * 60;

            if (minute < 10) {

                if (second < 10) {

                    return "0" + minute + ":0" + second;

                }

                return "0" + minute + ":" + second;

            }

            if (second < 10) {

                return minute + ":0" + second;

            }

            return minute + ":" + second;

        }

        int hour = second / 3600;

        int minute = (second - hour * 3600) / 60;

        second = second - hour * 3600 - minute * 60;

        if (hour < 10) {

            if (minute < 10) {

                if (second < 10) {

                    return "0" + hour + ":0" + minute + ":0" + second;

                }

                return "0" + hour + ":0" + minute + ":" + second;

            }

            if (second < 10) {

                return "0" + hour + ":" + minute + ":0" + second;

            }

            return "0" + hour + ":" + minute + ":" + second;

        }

        if (minute < 10) {

            if (second < 10) {

                return hour + ":0" + minute + ":0" + second;

            }

            return hour + ":0" + minute + ":" + second;

        }

        if (second < 10) {

            return hour + ":" + minute + ":0" + second;

        }

        return hour + ":" +  minute + ":" + second;

    }



}
