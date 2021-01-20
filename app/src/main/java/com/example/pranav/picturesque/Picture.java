package com.example.pranav.picturesque;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Pranav on 08-10-2018.
 */

public class Picture implements ClusterItem {

    private final LatLng mPosition;
    private Bitmap icon;
    private String imageUrl;

    public Picture(double lat, double lng, Bitmap icon, String imageUrl) {
        mPosition = new LatLng(lat, lng);
        this.icon = icon;
        this.imageUrl = imageUrl;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public Bitmap getIcon()
    {
        return icon;
    }

    public String getImageUrl() { return imageUrl; }

}
