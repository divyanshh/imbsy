package com.divyanshjain.imbsy;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Divyansh Jain on 7/5/2016.
 */
public class NewSQLView extends AppCompatActivity implements View.OnClickListener {

    private Database info;

    public NewSQLView() {
        info = new Database(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlview);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout parent = (LinearLayout) findViewById(R.id.linearLayout);

        for (Pair<String, String> idAndPhoneNo : info.open().getIdAndValues()) {
            LinearLayout child = (LinearLayout) inflater.inflate(R.layout.activity_sqlview_2, null);
            TextView textView = (TextView) child.findViewById(R.id.textView2);
            Button removeButton = (Button) child.findViewById(R.id.button4);

            textView.setText(idAndPhoneNo.second);

            removeButton.setId(Integer.parseInt(idAndPhoneNo.first));
            removeButton.setOnClickListener(this);

            parent.addView(child);
        }

        info.close();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            int srNo = v.getId();
            info.open().deleteEntry(srNo);
            info.close();
            NewSQLView.this.recreate();
        }
    }

}
