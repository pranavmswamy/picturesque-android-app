package com.example.pranav.picturesque;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button mNormalMap, mSatelliteMap, mHybridMap, mTerrainMap;
    private FloatingActionButton mGallery;
    private ArrayList<ArrayList<Float>> LatLong = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();

    protected GoogleMap getMap() {
        return mMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mtravelLog = (Button) findViewById(R.id.btnTravelLog);
        mNormalMap = (Button) findViewById(R.id.btnNormalMap);
        mSatelliteMap = (Button) findViewById(R.id.btnSatelliteMap);
        mHybridMap = (Button) findViewById(R.id.btnHybridMap);
        mTerrainMap = (Button) findViewById(R.id.btnTerrainMap);
        mGallery = (FloatingActionButton) findViewById(R.id.btnGallery);



        mNormalMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        mSatelliteMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        mHybridMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        mTerrainMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_VIEW);
                galleryIntent.setType("image/*");
                startActivity(galleryIntent);
            }
        });

       loadGPSCoordinates(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    loadGPSCoordinates(this);
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menuAbout)
        {
            Intent aboutIntent = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(aboutIntent);
        }
        return true;
    }

    public void loadGPSCoordinates(Activity context)
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
        else
        {
            float latLongArray[] = new float[2];
            final String[] columns = {MediaStore.Images.Media.DATA};//get all columns of type images
            //final String orderBy = MediaStore.Images.Media.DATE_TAKEN;//order data by date
           // Cursor imageCursor = context.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
              //      null, orderBy + " DESC");//get all data in Cursor by sorting in DESC order
            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,null);


            for (int i = 0; i < imageCursor.getCount(); i++)
            {
                imageCursor.moveToPosition(i);
                int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);//get column index
                String imageUrl = imageCursor.getString(dataColumnIndex);

                try
                {
                    ExifInterface image = new ExifInterface(imageUrl);
                    if(image.getLatLong(latLongArray))
                    {
                            imageUrls.add(imageUrl);
                            ArrayList<Float> temp = new ArrayList<Float>();
                            temp.add(latLongArray[0]);
                            temp.add(latLongArray[1]);
                            LatLong.add(temp);

                            BitmapFactory.Options bitOp = new BitmapFactory.Options();
                            bitOp.inSampleSize = 16;
                            //bitmaps.add(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imageUrl),120,120,false));
                            bitmaps.add(BitmapFactory.decodeFile(imageUrl,bitOp));
                    }
                    }
                    catch(IOException e)
                    {

                    }

                }



//--------------------------------------------------------------------------------

            Log.e("fetch in", "images");
            Log.i("ALL PICS LENGTH",LatLong.size()+"\n"+bitmaps.size()+"\n");
            Log.i("Pranav","Pranav");Log.i("Pranav","Pranav");
//---------------------------------------------------------------------------
        }
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<Picture> mClusterManager;

    private void setUpClusterer() {
        // Position the map.
       // getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Picture>(this, getMap());
        mClusterManager.setRenderer(new OwnIconRendered(getApplicationContext(), getMap(), mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Picture>() {
            @Override
            public boolean onClusterItemClick(Picture picture) {

                Intent imageIntent = new Intent(getApplicationContext(),ImageActivity.class);
                imageIntent.putExtra("image",picture.getIcon());
                imageIntent.putExtra("picUrl",picture.getImageUrl());
                startActivity(imageIntent);

                return false;
            }
        });

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        for (int i = 0; i < LatLong.size(); i++)
        {
            float latitude = LatLong.get(i).get(0);
            float longitude = LatLong.get(i).get(1);
            //LatLng location = new LatLng(latitude, longitude);
            Picture offsetItem = new Picture(latitude,longitude,bitmaps.get(i),imageUrls.get(i));
            mClusterManager.addItem(offsetItem);
            //mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(bitmaps.get(i))));
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bangalore = new LatLng(13, 77.5);
        CameraPosition cameraPosition = CameraPosition.builder().zoom(5).bearing(0.0f).target(bangalore).tilt(0).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        setUpClusterer();
    }
}
