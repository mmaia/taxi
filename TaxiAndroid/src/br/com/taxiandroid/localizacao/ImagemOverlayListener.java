package br.com.taxiandroid.localizacao;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;

public class ImagemOverlayListener extends ImagemOverlay implements LocationListener{

	public ImagemOverlayListener(GeoPoint geoPoint, int idImg){
		super(geoPoint, idImg);
	}
	
	
	@Override
	public void onLocationChanged(Location location) {
		setGeoPoint(new Ponto(location));
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
