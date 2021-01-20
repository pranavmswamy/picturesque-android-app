package com.example.pranav.picturesque;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Pranav on 08-10-2018.
 */

public class OwnIconRendered extends DefaultClusterRenderer<Picture> {

    public OwnIconRendered(Context context, GoogleMap map, ClusterManager<Picture> clusterManager)
    {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Picture picture, MarkerOptions markerOptions)
    {
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(picture.getIcon()));
        super.onBeforeClusterItemRendered(picture, markerOptions);
    }
}
