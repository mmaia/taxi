package br.com.taxiandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TabDirecao extends Activity{

	private String latitudeCliente = null;
	private String longitudeCliente = null;
	private Button btnDadosUsuario = null;
	private boolean mostraDirecao = true;

	@Override
	public void onCreate(Bundle savedInstanceState){
		latitudeCliente = "-15.752875408759547";
		longitudeCliente = "-47.87046361745077";
		super.onCreate(savedInstanceState);
		montaTela();
	}
	
	private void montaTela() {
//		if(GlobalTaxi.getGlobalExpress().getLatitCliente() != null){
//		if(latitudeCliente != null){
		if(GlobalTaxi.getGlobalExpress().getSolicitacaoGson() != null 
				&& GlobalTaxi.getGlobalExpress().getSolicitacaoGson().getOrigem() != null
				&& mostraDirecao){
//		if(mostraDirecao){
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+GlobalTaxi.getGlobalExpress().getSolicitacaoGson().getOrigem()));
//			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+latitudeCliente+","+longitudeCliente));
			startActivity(intent);
			mostraDirecao = false;
		}else{
	        setContentView(R.layout.tab_direcao);
	        btnDadosUsuario = (Button)findViewById(R.id.btnDadosUsuario);
	        btnDadosUsuario.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((SolicitaTaxiTab)getParent()).getTabHost().setCurrentTab(1);
				}
			});
		}
	}

	public void onResume(){
		super.onResume();
	}
	
	public void onRestart(){
		super.onRestart();
			((SolicitaTaxiTab)getParent()).getTabHost().setCurrentTab(0);
	}
	
}