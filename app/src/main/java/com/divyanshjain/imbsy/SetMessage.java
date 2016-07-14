package com.divyanshjain.imbsy;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SetMessage extends AppCompatActivity implements View.OnClickListener {

    Button setText;
    EditText setMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_message);

        setText = (Button) findViewById(R.id.setMessage);
        setText.setOnClickListener(this);

        setMessage = (EditText) findViewById(R.id.editText2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.setMessage:
                String s = setMessage.getText().toString();
                FileOutputStream fos;
                try {
                    fos = openFileOutput("message.txt", Context.MODE_PRIVATE);
                    //default mode is PRIVATE, can be APPEND etc.
                    fos.write(s.getBytes());
                    fos.close();
                    Toast.makeText(this, "Message Set", Toast.LENGTH_LONG).show();
                    SetMessage.this.onBackPressed();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
