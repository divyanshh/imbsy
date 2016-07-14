package com.divyanshjain.imbsy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BlockList extends AppCompatActivity implements View.OnClickListener {

    Button sqlUpdate, sqlView;
    EditText sqlNo, sqlRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_block_list);

        sqlUpdate = (Button) findViewById(R.id.bSQLUpdate);
        sqlView = (Button) findViewById(R.id.bSQLopenView);

        sqlNo = (EditText) findViewById(R.id.etSQLNo);

        sqlView.setOnClickListener(this);
        sqlUpdate.setOnClickListener(this);
    }

    public void onClick(View arg0) {

        switch(arg0.getId()) {

            case R.id.bSQLUpdate:

                boolean didItWork = true;
                try {
                    String no = sqlNo.getText().toString();
                    if (no.length() != 0) {
                        Database entry = new Database(BlockList.this);
                        entry.open();
                        entry.createEntry(no);
                        entry.close();
                    }
                } catch (Exception e) {
                    didItWork = false;
                    String error = e.toString();
                    Dialog d = new Dialog(this);
                    d.setTitle("Dang it!");
                    TextView tv = new TextView(this);
                    tv.setText(error);
                    d.setContentView(tv);
                    d.show();
                } finally {
                    if (didItWork) {
                        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_LONG).show();
                        BlockList.this.onBackPressed();
                    }
                }
                break;

            case R.id.bSQLopenView:
                Intent openac = new Intent(BlockList.this, NewSQLView.class);
                BlockList.this.startActivity(openac);
                break;

        }
    }
}
