package com.example.root.incercaretest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestMainActivity extends AppCompatActivity {

    private EditText leftEditText = null;
    private EditText rightEditText = null;
    private Button leftButton = null;
    private Button rightButton = null;
    private boolean isChange = false;
    private int one = 0;
    private int cache;
    private int serviceStatus = 2; //stop
    private boolean first = true;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {


            switch(view.getId()) {
                case R.id.addButton:
                    if (one == 0) {
                        rightEditText.setText(leftEditText.getText().toString());
                        one = 1;
                    } else {
                        rightEditText.setText(rightEditText.getText().toString() + "+" + leftEditText.getText().toString());
                    }
                    isChange = true;
                    break;
                case R.id.computeButton:
                    if (isChange == true) {
                        Intent intent = new Intent(getApplicationContext(), SecundActivity.class);
                        intent.putExtra("string_to_compute", rightEditText.getText().toString());
                        startActivityForResult(intent, 1);
                        break;
                    } else {
                        Toast.makeText(getApplicationContext(), "The activity returned with result  " + cache + " from cache", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        int result = intent.getIntExtra("computed_result", -1);
        cache = result;
        isChange = false;
        if (requestCode == 1) {
            Toast.makeText(getApplicationContext(), "The activity returned with result " + result, Toast.LENGTH_LONG).show();
        }
        if (first == true && serviceStatus == 2 && cache > 10) {
            first = false;
            serviceStatus = 1;
            Intent inte = new Intent(getApplicationContext(), TestService.class);
            inte.putExtra("sum", cache);
            getApplicationContext().startService(inte);
            serviceStatus = 1;
        }
    }

    private IntentFilter intentFilter = new IntentFilter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        leftEditText = (EditText)findViewById(R.id.inputText);
        rightEditText = (EditText)findViewById(R.id.computeText);
        leftButton = (Button)findViewById(R.id.addButton);
        rightButton = (Button)findViewById(R.id.computeButton);

        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("leftCount")) {
                rightEditText.setText(savedInstanceState.getString("leftCount"));
            }

            if (savedInstanceState.containsKey("rightCount")) {
                leftEditText.setText(savedInstanceState.getString("rightCount"));
            }
            if (savedInstanceState.containsKey("cache")) {
                cache = savedInstanceState.getInt("cache");
            }
        }
        for (int index = 0; index < 1; index++) {
            intentFilter.addAction("activity1");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("leftCount", leftEditText.getText().toString());
        savedInstanceState.putString("rightCount", rightEditText.getText().toString());
        savedInstanceState.putInt("cache", cache);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("leftCount")) {
            leftEditText.setText(savedInstanceState.getString("leftCount"));
        }
        if (savedInstanceState.containsKey("rightCount")) {
            rightEditText.setText(savedInstanceState.getString("rightCount"));
        }
        if (savedInstanceState.containsKey("cache")) {
            cache = savedInstanceState.getInt("cache");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, TestService.class);
        stopService(intent);
        super.onDestroy();
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }
}
