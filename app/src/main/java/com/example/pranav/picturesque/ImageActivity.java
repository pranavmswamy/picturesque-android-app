package com.example.pranav.picturesque;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap icon;
    Button mtravelLog;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();
        extras = getIntent().getExtras();
        icon = (Bitmap)extras.get("image");
        imageUrl = extras.getString("picUrl");
        imageView.setImageBitmap(icon);
        mtravelLog = findViewById(R.id.btnTravelLog);


        mtravelLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent travelLogIntent = new Intent(getApplicationContext(), TravelLog.class);
                travelLogIntent.putExtra("picid",imageUrl);
                startActivity(travelLogIntent);

            }
        });

    }
}
