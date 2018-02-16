package com.example.android.beritafiras.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class MyFunction extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=MyFunction.this;

    }

    // simpleIntent Default
    public void simpleIntent(Class dest){
        Intent move = new Intent(context, dest);
        startActivity(move);
    }
    // simpleIntent
    public void simpleIntent(Class dest, int key, ArrayList<String> val){
        Intent move = new Intent(context, dest);
        for(int i=0; i<key; i++) {
            move.putExtra("KEY"+i, val.get(i));
        }
        startActivity(move);
    }
    // simpleGetIntent
    public void simpleGetIntent(int key){
        ArrayList<String> val = new ArrayList<String>();

        Intent delivered = getIntent();
        for (int i=0; i<key; i++){
            val.add(delivered.getStringExtra("KEY"+i));
        }
    }
    // simpleToast Default
    public void simpleToast (String msg){
        Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
    }
    // simpleToast
    public void simpleToast (String msg, int time){
        if(time<2 && time>=1) {
            Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
        } else if(time>1) {
            Toast.makeText(context, msg , Toast.LENGTH_LONG).show();
        }
    }
    // simpleAlert
    public void simpleAlert(String message){
        AlertDialog.Builder builder = new  AlertDialog.Builder(context);
        builder.setTitle("informasi");

    }
}