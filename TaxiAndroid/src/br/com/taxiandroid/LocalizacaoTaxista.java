package br.com.taxiandroid;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import br.com.taxiandroid.localizacao.Coordenada;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * Atualiza as coordenadas do GPS "mock" no Mapa
 * 
 */

public class LocalizacaoTaxista extends MapActivity implements LocationListener {

	private static final String logAndroid = "localiza_taxista";
	private MapController controlador;
	private MapView mapa;
	private MyLocationOverlay meuLocal;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
//		mapa = new MapView(this, ""+R.string.id_mapa);
		mapa = new MapView(this, "");
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d("LocalizacaoTaxista", ""+mapa); 
		controlador = mapa.getController();
		controlador.setZoom(16);
		
		// Centraliza o mapa na ultima localiza�ao conhecida
		Location loc = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		// Se existe ultima localizacao converte para GeoPoint
		if (loc != null) {
			GeoPoint ponto = new Coordenada(loc);
			Log.i(logAndroid, "Ultima localizacao: " + ponto);
			controlador.setCenter(ponto);
		}
		meuLocal = new MyLocationOverlay(this, mapa);
		GeoPoint geop = meuLocal.getMyLocation();
//		double lat = (geop.getLatitudeE6() / 1E6);
//		double log = (geop.getLongitudeE6() / 1E6);
//		Log.i(logAndroid, "latitude: "+ lat + "longitude "+log);
		
		//Metodo que acerta a posicao do objeto
		meuLocal.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				GeoPoint geop = meuLocal.getMyLocation();
				double lat = (geop.getLatitudeE6() / 1E6);
				double log = (geop.getLongitudeE6() / 1E6);
				
				//Log.i(CATEGORIA, "MyOverlay runOnFirstFix: "+ ondeEstou.getMyLocation());
				Log.i(logAndroid, "latitude: "+ lat + "longitude "+log);
			}
		});
		mapa.getOverlays().add(meuLocal);
		getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		//getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 25, this);
		setContentView(mapa);		
	}

	private LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Registra o listener
		meuLocal.enableMyLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Remove o listener
		meuLocal.disableMyLocation();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Remove o listener para nao ficar atualizando mesmo depois de sair
		getLocationManager().removeUpdates(this);
	}

	public void onLocationChanged(Location location) {
		Log.i(logAndroid, "onLocationChanged: latitude: "+ location.getLatitude() + " - longitude: "+ location.getLongitude());

		GeoPoint point = new Coordenada(location);
		controlador.animateTo(point);
		mapa.invalidate();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
