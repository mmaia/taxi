package br.com.taxiandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import br.com.taxiandroid.util.Utilitario;

public class GPSTaxista extends Service {

	@Override
	public void onCreate(){
		Log.i("", "onCreate");		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int stardId){
		Log.i("", "onStart");
		Utilitario.verificandoNotification(GPSTaxista.this);
	}

    public void onDestroy(){
    	super.onDestroy();
    }
}
