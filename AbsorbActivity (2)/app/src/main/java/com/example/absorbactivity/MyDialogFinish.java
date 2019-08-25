package com.example.absorbactivity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;


public class MyDialogFinish extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("是否退出");
        builder.setMessage("是否确定退出本次学习?");
        builder.setCancelable(false);

        //当选择yes时
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MyInterface myInterface = (MyInterface)getActivity();
                myInterface.buttonYesClicked();

            }
        });

        //当选择no时
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyInterface myInterface = (MyInterface)getActivity();
                myInterface.buttonNoClicked();
            }
        });


        return builder.create();
    }
}
