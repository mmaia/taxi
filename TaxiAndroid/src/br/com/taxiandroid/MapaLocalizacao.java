 package br.com.taxiandroid;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import br.com.taxiandroid.localizacao.Coordenada;
import br.com.taxiandroid.localizacao.ImagemOverlay;
import br.com.taxiandroid.localizacao.Ponto;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapaLocalizacao extends MapActivity implements LocationListener{
	private MapView 			mapView 	 	= null;
	private MapController 		mapController	= null;
	private ImagemOverlay		carroTaxi;
	private ImagemOverlay		cliente;
	private int 				zoonAutomatico 	= 15;
	
	private Double 				latitudeCliente    = null;
	private Double 				longitudeCliente   = null;
	
	private Location			loc 			= null;
	private ProgressDialog 		dialog			= null;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		//Dados dever�o ser passados pelo sistema.
		String latit  = GlobalTaxi.getGlobalExpress().getLatitCliente();
        String longi  = GlobalTaxi.getGlobalExpress().getLongiCliente();
        if(latit != null){
	        latitudeCliente = Double.parseDouble(latit) ; 
	        longitudeCliente = Double.parseDouble(longi) ;
        }
        loc = GlobalTaxi.getGlobalExpress().getLocalizacao();
		mapView = new MapView(this, "0Wardm-o8dCnZkXvMHfJsJsMiY-S3uivyap_90w");
//		mapView.setSatellite(true);
		mapView.setStreetView(isRouteDisplayed());
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d("loc", ""+loc);
		if(loc != null){
			criaOverLays();
		}else{
//			Utilitario.alert();
			if(dialog == null)
				this.dialog = ProgressDialog.show(this, "Aguarde...", "Fixando GPS...", false, true, new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface dialog) {
						try {
							finish();
						} catch (Throwable e) {
							if(GlobalTaxi.getGlobalExpress().isDebug())
								Log.d("", ""+e.getMessage());
						}
					}
				});
		}
//		getLocationManager().requestLocationUpdates(LocationManager.GPS_ PROVIDER, (long)3000, (float) 10.0, this);
		setContentView(mapView);
//		pintaLocalizacaoTaxista();
	}
	
	private void pintaLocalizacaoTaxista() {
		do {
			loc = GlobalTaxi.getGlobalExpress().getLocalizacao();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (loc == null);
		if(dialog != null){
			dialog.dismiss();
		}
		new Thread().start();
	}
	
	/**
	 * Envia localizacao taxista
	 */	
	private void run(){
		do{
		}while(GlobalTaxi.getGlobalExpress().isTabMap());
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
	    	// Grupo ID 
	    	int groupId = 0; 
	    	// A ordem da posi��o dos menus 
	    	int menuItemOrder = Menu.NONE; 
	    	//teste
	    	menu.add(groupId, 0, menuItemOrder, "Navegador");
	    	menu.add(groupId, 1, menuItemOrder, "Sair");

	    	return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case (0):{
				abreNavegador();
				return true;
			}
			default:{
				finish();
			}
		}
		return false;
	}		
	
	private void abreNavegador() {
//		Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+loc.getLatitude()+","+loc.getLongitude()));
//		Intent navigation = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=saddr"+loc.getLatitude()+","+loc.getLongitude()+"(Taxista)&daddr=-15.7251,-47.8877"));
//		Intent navigation = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+loc.getLatitude()+","+loc.getLongitude()+"(Taxista)?z=4"));
//		Intent navigation = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(""http://maps.google.com/maps?saddr="+loc.getLatitude()+","+loc.getLongitude()+"(Taxista)&daddr=-15.7251,-47.8877"));
		Intent navigation = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=-15.7251,-47.8877"));
//		Intent navigation = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+loc.getLatitude()+","+loc.getLongitude()+"(Taxista)&daddr=SHIN QI 2 CONJUNTO 5 CASA 10, LAGO NORTE, BRASILIA"));
		startActivity(navigation);

		
	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event){
		if(KeyCode == KeyEvent.KEYCODE_S){
			mapView.setSatellite(true);
			mapView.setStreetView(false);
			mapView.setTraffic(false);
		}else if(KeyCode == KeyEvent.KEYCODE_R){
			mapView.setSatellite(false);
			mapView.setStreetView(true);
			mapView.setTraffic(false);
		}else if(KeyCode == KeyEvent.KEYCODE_T){
			mapView.setSatellite(false);
			mapView.setStreetView(false);
			mapView.setTraffic(true);
		}
//		else if(KeyCode == KeyEvent.KEYCODE_Z){
//			mapController.zoomIn();
//		}else if(KeyCode == KeyEvent.KEYCODE_X){
//			mapController.zoomOut();
//		}  
		return super.onKeyDown(KeyCode, event);
	}
	
	
	//Retorna o pr�ximo ponto para mover o mapa
	private GeoPoint getPontoCliente(){
//		double latitude =  -15.7251; 
//		double longitude = -47.8877;
		GeoPoint p = new Ponto(latitudeCliente, longitudeCliente);
		return p;
	}	
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public Double[] mediaDoisPontos(){
		double _latitude = 0.0;
		double _longitude = 0.0;
		double latitudeTaxista = GlobalTaxi.getGlobalExpress().getLocalizacao().getLatitude();
		double longitudeTaxista = GlobalTaxi.getGlobalExpress().getLocalizacao().getLongitude();

		if(latitudeTaxista > latitudeCliente){
			_latitude = latitudeTaxista - latitudeCliente;
			_latitude = latitudeTaxista - _latitude;
		}
		else{
			_latitude = latitudeCliente - latitudeTaxista;
			_latitude = latitudeCliente - _latitude;
		}
		if(longitudeTaxista > longitudeCliente){
			_longitude = longitudeTaxista - longitudeCliente;
			_longitude = longitudeTaxista - _longitude;
		}
		else{
			_longitude = longitudeCliente - longitudeTaxista;
			_longitude = longitudeCliente - _longitude;
		}
		Double dados [] = {_latitude, _longitude};
		return dados;
	}

	@Override
	public void onLocationChanged(Location location) {
		if(dialog != null)
			dialog.dismiss();
//		loc = GlobalExpress.getGlobalExpress().getLocalizacao();
//		Double [] latLong = mediaDoisPontos();
//		GeoPoint point = new Coordenada(latLong[0].doubleValue(), latLong[1].doubleValue());
		GeoPoint point = new Coordenada(location.getLatitude(), location.getLongitude());
		Log.i("MapaLocalizacao", "GeoPoint > " + point);
		Log.i("MapaLocalizacao", "onLocationChanged: latitude: "+ location.getLatitude() + " - longitude: "+ location.getLongitude());
		//Centraliza o Map no ponto
		if(loc == null){
			loc = location;
			criaOverLays();
		}
		mapController.animateTo(point);
		carroTaxi.setGeoPoint(point);
		mapView.invalidate();
		Toast.makeText(mapView.getContext(), "onLocation lat:"+ location.getLatitude() + " long" +location.getLongitude(), Toast.LENGTH_LONG).show();

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
	public LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	
	private void criaOverLays(){
		//Criando ponto com os parametros
		GeoPoint pontoCliente = new Ponto(latitudeCliente, longitudeCliente);
		
		carroTaxi   = new ImagemOverlay(((GeoPoint)new Coordenada(loc)), R.drawable.taxi);
		cliente 	= new ImagemOverlay(pontoCliente, R.drawable.cliente);
		
		ArrayList<ImagemOverlay> mOverlays = new ArrayList<ImagemOverlay>();
		mOverlays.add(carroTaxi);
		mOverlays.add(cliente);

		mapController = mapView.getController();
		//Para fazer zoom (valores de 1 a 21)
		mapController.setZoom(zoonAutomatico);
		mapController.setCenter(pontoCliente);

		Double [] latLong = mediaDoisPontos();
		GeoPoint pointCentral = new Coordenada(latLong[0].doubleValue(), latLong[1].doubleValue());
		mapController.animateTo(pointCentral);
		mapView.getOverlays().addAll(mOverlays);
	}
}
