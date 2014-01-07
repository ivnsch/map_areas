package com.schuetz.mapareas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.schuetz.maparea.R;
import com.schuetz.mapareas.MapAreaManager.CircleManagerListener;


/**
 * Demo of map areas
 * 
 * @author ivanschuetz 
 */
public class DemoActivity extends FragmentActivity {
    private GoogleMap map;
    private MapAreaManager circleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {
                setupMap();
            }
        }
    }

    private void setupMap() {
        circleManager = new MapAreaManager(map, 
        		
    		4, Color.RED, Color.HSVToColor(70, new float[] {1, 1, 200}), //styling 
    		
    		R.drawable.move, R.drawable.resize, //custom drawables for move and resize icons
    		
    		0.5f, 0.5f, 0.5f, 0.5f, //sets anchor point of move / resize drawable in the middle
    		
    		new MapAreaMeasure(100, MapAreaMeasure.Unit.pixels), //circles will start with 100 pixels (independent of zoom level)
    		
    		new CircleManagerListener() { //listener for all circle events
    	
				@Override
				public void onResizeCircleEnd(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on drag end circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void onCreateCircle(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on crate circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void onMoveCircleEnd(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on moved circle: " + draggableCircle, Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void onMoveCircleStart(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on move circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void onResizeCircleStart(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on resize circle start: " + draggableCircle, Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void onMinRadius(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on min radius: " + draggableCircle, Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void onMaxRadius(MapAreaWrapper draggableCircle) {
					Toast.makeText(DemoActivity.this, "do something on max radius: " + draggableCircle, Toast.LENGTH_LONG).show();
				}
		});
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52, 13), 6)); 
    }
}