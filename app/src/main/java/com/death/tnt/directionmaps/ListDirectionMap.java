package com.death.tnt.directionmaps;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.death.tnt.R;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;

import java.util.ArrayList;

public class ListDirectionMap extends AppCompatActivity {
    MapView map = null;
    private SimpleLocationOverlay mMyLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    //your items
    ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    ItemizedIconOverlay<OverlayItem> currentLocationOverlay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directionmaps);
        double lat1 = getIntent().getExtras().getDouble("lat1");
        double lon1 = getIntent().getExtras().getDouble("lon1");
        double lat2 = getIntent().getExtras().getDouble("lat2");
        double lon2 = getIntent().getExtras().getDouble("lon2");
        Log.e("location", String.valueOf(lat1));
        map = (MapView)findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);


        GeoPoint startPoint = new GeoPoint(lat1,lon1);
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", startPoint);
        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.markerblack);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);
        items.add(myLocationOverlayItem);



        GeoPoint endPoint = new GeoPoint(lat2,lon2);
        myLocationOverlayItem = new OverlayItem("Here", "Destination", endPoint);
        myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.markerblack);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);
        items.add(myLocationOverlayItem);

        currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return false;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, this);
        this.map.getOverlays().add(this.currentLocationOverlay);

        PathOverlay myPath = new PathOverlay(Color.RED,this);
        myPath.addPoint(startPoint);
        myPath.addPoint(endPoint);
        map.getOverlays().add(myPath);
        mapController.setCenter(startPoint);
        mapController.animateTo(startPoint);
    }
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }
}
