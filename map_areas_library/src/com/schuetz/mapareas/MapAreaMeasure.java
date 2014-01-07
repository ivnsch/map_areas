package com.schuetz.mapareas;

/**
 * MapAreaMeasure
 * 
 * @author ivanschuetz 
 */
public class MapAreaMeasure {
    
	public static enum Unit {pixels, meters}
	
	public double value;
	public Unit unit;
	
	public MapAreaMeasure(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}
}