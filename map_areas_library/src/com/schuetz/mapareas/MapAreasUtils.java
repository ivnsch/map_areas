package com.schuetz.mapareas;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Utilities
 * 
 * @author ivanschuetz 
 */
public class MapAreasUtils {

    public static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / MapAreasConstants.RADIUS_OF_EARTH_METERS) / Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    public static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude, radius.latitude, radius.longitude, result);
        return result[0];
    }
}
