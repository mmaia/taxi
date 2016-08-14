package br.com.taxiandroid.localizacao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ImagemOverlay extends Overlay {
	private Paint paint = new Paint();
	//Recurso da imagem (R.drawable.?)
	private final int imagemId;
	private GeoPoint geoPoint;
	String fraseImagemOverlay;
	
	public ImagemOverlay(GeoPoint geoPoint, int resId){
		this.geoPoint = geoPoint;
		this.imagemId = resId;
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		super.draw(canvas, mapView, shadow);
		setFraseImagemOverlay("Clicou sobre o Overlay");
		
		//Converte as coordenadas para pixels
		Point p = mapView.getProjection().toPixels(geoPoint, null);
		Bitmap bitmap = BitmapFactory.decodeResource(mapView.getResources(), this.imagemId);
		RectF r = new RectF(p.x, p.y, p.x+bitmap.getWidth(), p.y+bitmap.getHeight());
		canvas.drawBitmap(bitmap, null, r, paint);
	}
	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView){
		//Cria o retangulo
		Bitmap bitmap = BitmapFactory.decodeResource(mapView.getResources(), this.imagemId);
		Point p = mapView.getProjection().toPixels(geoPoint, null);
		RectF r = new RectF(p.x, p.y, p.x+bitmap.getWidth(), p.y+bitmap.getHeight());
		System.out.println("posicoes x: "+p.x+ " y: "+ p.y);
		
		//Converte para ponto em pixels
		Point newPoint = mapView.getProjection().toPixels(geoPoint, null);
		//Verifica se o ponto esta contido no retangulo
		boolean clicado = r.contains(newPoint.x, newPoint.y);
		if(clicado){
			Toast.makeText(mapView.getContext(), getFraseImagemOverlay() + geoPoint, Toast.LENGTH_LONG).show();
			return true;
		}
	
		return super.onTap(geoPoint, mapView);
	}

//	@Override
//	public boolean onTouchEvent(android.view.MotionEvent e, MapView mapView){
//		Log.i("onTouchEvent", ""+e.getAction());
//			Toast.makeText(mapView.getContext(), getFraseImagemOverlay() + geoPoint, Toast.LENGTH_LONG).show();
//		return super.onTouchEvent(e, mapView);
//	}
	
	public String getFraseImagemOverlay() {
		return fraseImagemOverlay;
	}
	
	public void setFraseImagemOverlay(String fraseImagemOverlay) {
		this.fraseImagemOverlay = fraseImagemOverlay;
	}
	

}
