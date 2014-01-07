package com.schuetz.mapareas;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class manages a map circle, with markers
 * 
 * Currently there's position marker (in the middle) and resizing marker (in border)
 * Long pressing these markers allow to change circle position or size.
 * 
 * @author ivanschuetz
 * 
 */
public class MapAreaWrapper {

	public static enum MarkerMoveResult {moved, radiusChange, minRadius, maxRadius, none};
    public static enum MarkerType {move, resize, none}

	private Marker centerMarker;
    private Marker radiusMarker;
    private Circle circle;
    private double radiusMeters;
    
    private int minRadiusMeters = -1;
    private int maxRadiusMeters = -1;
    
    
    /**
     * Primary constructor
     * 
     * @param map
     * @param center center of circle in geo coordinates
     * @param radiusMeters radius of circle in meters
     * @param strokeWidth circle stroke with in pixels
     * @param strokeColor circle stroke color
     * @param fillColor circle fill color
     * @param minRadiusMeters optional - circle min radius in meters (circle will not shrink bellow this, and callback is called when reached)
     * @param maxRadiusMeters optional - circle max radius in meters (circle will not expand above this, and callback is called when reached)
     * @param centerDrawableId drawable ressource id for positioning marker. If not set a default geomarker is used
     * @param radiusDrawableId  drawable ressource id for resizing marker. If not set a default geomarker is used
     * @param moveDrawableAnchorU horizontal anchor for move drawable
     * @param moveDrawableAnchorV vertical anchor for move drawable
     * @param resizeDrawableAnchorU horizontal anchor for resize drawable
     * @param resizeDrawableAnchorV vertical anchor for resize drawable
     */
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, float strokeWidth, int strokeColor, int fillColor, int minRadiusMeters, int maxRadiusMeters, 
    		int centerDrawableId, int radiusDrawableId, float moveDrawableAnchorU, float moveDrawableAnchorV, float resizeDrawableAnchorU, float resizeDrawableAnchorV) {
    	
        this.radiusMeters = radiusMeters;
        this.minRadiusMeters = minRadiusMeters;
        this.maxRadiusMeters = maxRadiusMeters;
        
        centerMarker = map.addMarker(new MarkerOptions()
                .position(center)
                .anchor(moveDrawableAnchorU, moveDrawableAnchorV)
                .draggable(true));
    	
        if (centerDrawableId != -1) {
        	centerMarker.setIcon(BitmapDescriptorFactory.fromResource(centerDrawableId));
        }
        
        radiusMarker = map.addMarker(new MarkerOptions()
	        .position(MapAreasUtils.toRadiusLatLng(center, radiusMeters))
	        .anchor(resizeDrawableAnchorU, resizeDrawableAnchorV)
	        .draggable(true));
        
        if (radiusDrawableId != -1) {
        	radiusMarker.setIcon(BitmapDescriptorFactory.fromResource(radiusDrawableId));
        }
		
        circle = map.addCircle(new CircleOptions()
                .center(center)
                .radius(radiusMeters)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor));
    }
    
    /**
     * Convenience constructor
     * 
     * Will pass -1 as move and resize drawable resource id, with means we will use default geo markers
     * 
     * @params see primary constructor
     */
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, float strokeWidth, int strokeColor, int fillColor, int minRadius, int maxRadius) {
    	this(map, center, radiusMeters, strokeWidth, strokeColor, fillColor, minRadius, maxRadius, -1, -1);
    }
    
    /**
     * Convenience constructor
     * 
     * Uses default values for marker's drawable anchors  
     * 
     * @params see primary constructor
     */
    public MapAreaWrapper(GoogleMap map, LatLng center, double radiusMeters, float strokeWidth, int strokeColor, int fillColor, int minRadius, int maxRadius, 
    		int centerDrawableId, int radiusDrawableId) {
    	
    	this(map, center, radiusMeters, strokeWidth, strokeColor, fillColor, minRadius, maxRadius, centerDrawableId, radiusDrawableId, 0.5f, 1f, 0.5f, 1f);
    }
    
    
    /**
     * @return center of circle in geocoordinates
     */
    public LatLng getCenter() {
    	return centerMarker.getPosition();
    }
    
    /**
     * @return radius of circle in meters
     */
    public double getRadius() {
    	return radiusMeters;
    }

    public void setStokeWidth(float strokeWidth) {
    	circle.setStrokeWidth(strokeWidth);
    }
    
    public void setStokeColor(int strokeColor) {
    	circle.setStrokeColor(strokeColor);
    }
    
    public void setFillColor(int fillColor) {
    	circle.setFillColor(fillColor);
    }
    
    public void setCenter(LatLng center) {
    	centerMarker.setPosition(center);
        onCenterUpdated(center);
    }
    
    /**
     * This modifies circle according to marker's type and position
     * 
     * if the marker is position marker (from this circle), the circle will be moved according to marker's position
     * if the marker is resizing marker (from this circle), the circle will be resized according to marker's position
     *
     * If the marker is not in this circle (it's not the position or resizing marker) no action will be done 
     *
     * @param marker
     * 
     * @return flag indicating which action was done.
	 * When the marker is not in this circle returned action is MarkerMoveResult.none
     */
    public MarkerMoveResult onMarkerMoved(Marker marker) {
        if (marker.equals(centerMarker)) {
        	onCenterUpdated(marker.getPosition());
            return MarkerMoveResult.moved;
        }
        
        if (marker.equals(radiusMarker)) {
        	 double newRadius = MapAreasUtils.toRadiusMeters(centerMarker.getPosition(), marker.getPosition());
        		
        	 if (minRadiusMeters != -1 && newRadius < minRadiusMeters) {
        		 return MarkerMoveResult.minRadius;
        		 
        	 } else if (maxRadiusMeters != -1 && newRadius > maxRadiusMeters) {
        		 return MarkerMoveResult.maxRadius;
        		 
        	 } else {
        		 setRadius(newRadius);
        		 
        		 return MarkerMoveResult.radiusChange;
        	 }
             
        }
        return MarkerMoveResult.none;
    }
    
    /**
     * Called after update position of center marker, to update the circle and the radius marker 
     * @param center
     */
    public void onCenterUpdated(LatLng center) {
    	circle.setCenter(center);
        radiusMarker.setPosition(MapAreasUtils.toRadiusLatLng(center, radiusMeters));
    }
    
    /**
     * Set the radius of circle
     * the map circle will be updated immediately
     * 
     * @param radiusMeters
     */
    public void setRadius(double radiusMeters) {
    	this.radiusMeters = radiusMeters;
    	circle.setRadius(radiusMeters);
    }
    
    @Override
    public String toString() {
    	return "center: " + getCenter() + " radius: " + getRadius();
    }
}