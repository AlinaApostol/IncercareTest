package com.example.root.incercaretest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SecundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secund);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int result = 0;

        Intent intent = getIntent();
        if (intent != null) {
            String toCompute = intent.getStringExtra("string_to_compute");
            String[] splits = toCompute.split("\\+");

            for(int i = 0; i < splits.length; i++) {
                result += Integer.parseInt(splits[i]);
            }
            intent.putExtra("computed_result", result);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

}
