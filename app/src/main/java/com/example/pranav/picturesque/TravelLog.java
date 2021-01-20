package com.example.pranav.picturesque;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TravelLog extends AppCompatActivity {
    EditText txtLog;
    FloatingActionButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_log);
        txtLog = findViewById(R.id.txtLog);
        save = findViewById(R.id.btnSave);



        DatabaseHandler db = new DatabaseHandler(getApplicationContext(),"picturesque",null,1);
        Bundle extras = getIntent().getExtras();
        final String picid = (String)extras.get("picid");

        //Toast.makeText(getApplicationContext(),picid,Toast.LENGTH_SHORT).show();

        txtLog.setText(db.displayNotes(picid));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext(),"picturesque",null,1);
                if(!txtLog.getText().toString().equals(db.displayNotes(picid)))
                    db.insertRow(picid,txtLog.getText().toString());
            }
        });
    }
}
