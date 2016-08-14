package br.com.taxiandroid.localizacao;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class Ponto extends GeoPoint {
	public Ponto(int latitudeE6, int longituteE6){
		super(latitudeE6, longituteE6);
	}
	//Converte para "graus * 1E6"
	public Ponto(double latitude, double longitude){
		this((int)(latitude * 1E6), (int)(longitude * 1E6));
	}
	public Ponto(Location location){
		this(location.getLatitude(), location.getLongitude());
	}
}
