package br.com.taxiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import br.com.taxiandroid.bean.SolicitacaoTaxista;
import br.com.taxiandroid.bean.SolicitacaoTaxistaMobile;
import br.com.taxiandroid.service.GPSTaxista;
import br.com.taxiandroid.util.Conexao;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiandroid.util.FinalizaCorrida;
import br.com.taxiandroid.util.Utilitario;
import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.gson.UsuarioGson;

public class SolicitaTaxiTab extends TabActivity implements OnTabChangeListener, LocationListener{

	public final static int 		menuDesconectar			= 1;
	public final static int 		menuDesligaGps 			= 2;
    private TabHost 				tabHost 				= null;
	private ProgressDialog 			dialog					= null;
	private LocationManager 		locationManager 		= null; 
	private int 					resultGPS_Habilitado	= 0;
	private static String 			RESPONDE_SOLIC_URL 		= "";
	private boolean					trocaTela				= false;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
          String numTab = "0";	
	      Intent parametrosHash = getIntent();
	      Bundle parametros = parametrosHash.getExtras();
	      if (parametros!=null){
	    	  numTab = parametros.getString("numTab");
	      }
        
        tabHost 			= getTabHost();

        tabHost.addTab(tabHost.newTabSpec("Geral")
                .setIndicator("Geral", getResources().getDrawable(R.drawable.taxi1))
                .setContent(new Intent(this, TabGeral.class)));

        tabHost.addTab(tabHost.newTabSpec("Solicitação")
                .setIndicator("Solicitação", getResources().getDrawable(R.drawable.clientetab))
                .setContent(new Intent(this, TabSolicitacao.class))	);

        tabHost.addTab(tabHost.newTabSpec("Direcao")
                .setIndicator("Direção", getResources().getDrawable(R.drawable.segue_taxista))
                .setContent(new Intent(this, TabDirecao.class)));

//      tabHost.addTab(tabHost.newTabSpec("Controle")
//      .setIndicator("Controle", getResources().getDrawable(R.drawable.cifrao2))
//      .setContent(new Intent(this, TabFinancas.class)));
        
        tabHost.getTabWidget().setCurrentTab(Integer.parseInt(numTab));
		getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		GlobalTaxi.getGlobalExpress().setTabHost(tabHost);
		
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
		       @Override
		       public void onTabChanged(String arg0) {
		           Activity currentActivity = getCurrentActivity();
		           if (currentActivity instanceof TabSolicitacao) {
		               ((TabSolicitacao)currentActivity).updateResultadosNaTela();
		           }
//		           if (currentActivity instanceof messages) { 
//		               ((messages) currentActivity).aPublicMethodFromClassMessages();
//		           }
		       }
		});
		if(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile() != null){
			//Mostra dialog mostrando ao taxista que tem uma solicitaçao em aberto.
			GlobalTaxi.getGlobalExpress().setStatusCorrida(true);//coloca o taxista como true na corrida, ou seja, está ocupado
			showDialog(6);
		}
    }

    
    
	/**
	 * Cria menus na aplicação
	 */
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
	    	MenuItem item1 = menu.add(0, menuDesconectar, 0, "Desconectar");
	    	item1.setIcon(R.drawable.sugestao_endereco);
//	    	
//	    	MenuItem item2 = menu.add(0, menuDesligaGps, 0, "Parar GPS");
//	    	item2.setIcon(R.drawable.segue_taxista);
	    	return true;
	    }
    	
	/**
	/* Ação menu
	 * 
	 */
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
		super.onOptionsItemSelected(item);
			switch (item.getItemId()) {
				case (1):{
		    		DataBase database = new DataBase(getApplicationContext());
		    		int result = database.deleteUsuario(GlobalTaxi.getGlobalExpress().getUsuarioBean().getId());
		    		database.close();
		    		if(result > 0){
		    			GlobalTaxi.getGlobalExpress().setUsuarioBean(null);
		    			Utilitario.verificandoNotification(SolicitaTaxiTab.this);
		    		}
		    		finalizaActivity();
					return true;
				}
				case (2):{
//					stopService(new Intent("GPS_TAXISTA"));
					return true;
				}
				default:{
				}
			}
		return false;
	}
	
	@Override
	public void onTabChanged(String tabId) {
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d("", ""+tabId + " "+tabHost.getCurrentTab());
		if(tabId.equals("Mapa")){
			GlobalTaxi.getGlobalExpress().setTabMap(true);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if(dialog != null){
			if(dialog.isShowing()){
				dialog.cancel();
			}
		}
		SolicitacaoTaxista st =  GlobalTaxi.getGlobalExpress().getSolicitacaoTaxista();
//		if(st != null && GlobalTaxi.getGlobalExpress().isMostraSolicitacaoTeste()){
		if(st != null ){
//        	GlobalTaxi.getGlobalExpress().setMostraSolicitacaoTeste(false);
			showDialog(2);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		if(locationManager == null || !locationManager.isProviderEnabled("gps")){
			showDialog(1);
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		if(dialog != null){
			if(dialog.isShowing()){
				dialog.cancel();
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	public LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	//Chama menu para habilitar o GPS do Sistema Operacional
	private void habilitaGPS() {
		startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), resultGPS_Habilitado); 
	}
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 1:
            LayoutInflater factory = LayoutInflater.from(this);
            final View loginView = factory.inflate(R.layout.gps_desabilitado, null);
            return new AlertDialog.Builder(SolicitaTaxiTab.this)
                .setIcon(R.drawable.segue_taxista)
                .setTitle("Localização")
                .setView(loginView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	habilitaGPS();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	finalizaActivity();
                    }
                })
                .create();
        case 2:
//	    	//Confirma��o de solicita��o
//            factory = LayoutInflater.from(this);
//            final View solicitcaoView = factory.inflate(R.layout.solicitacao_pedido, null);
//	        return new AlertDialog.Builder(SolicitaTaxiTab.this)
//	        .setIcon(R.drawable.confirmacao)
//	        .setTitle("Confirma��o Solicita��o")
//	        .setView(solicitcaoView)
//	        .setPositiveButton(R.string.alertBotaoOk, new DialogInterface.OnClickListener() {
//	            public void onClick(DialogInterface dialog, int whichButton) {
//	            	respondeSolicitacao(1);
//	            	trocaTela = true;
//	            }
//	        })
//	        .setNegativeButton(R.string.alertBotaoCancelar, new DialogInterface.OnClickListener() {
//	            public void onClick(DialogInterface dialog, int whichButton) {
//	            	respondeSolicitacao(0);
//	            	GlobalTaxi.getGlobalExpress().setSolicitacaoTaxista(null);
//	            	trocaTela = false;	            	
//	            }
//	        })
//	        .create();
        case 6:
	    	//Mostrando dialog de reserva ainda nao finalizada
            factory = LayoutInflater.from(this);
            final View solicitcaoView = factory.inflate(R.layout.solicitacao_pedido, null);
	        return new AlertDialog.Builder(SolicitaTaxiTab.this)
	        .setIcon(R.drawable.confirmacao)
	        .setTitle("Solicitação não finalizada")
	        .setView(solicitcaoView)
	        .setPositiveButton(R.string.btnRecuperarSolicitacao, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	    			UsuarioGson us = new UsuarioGson();
	    			us.setNome(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getNome());
	    			us.setSobreNome(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getSobreNome());
	    			us.setCelular(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getCelular());
	    			
	    			SolicitacaoGson sg = new SolicitacaoGson();
	    			sg.setOrigem(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getEndereco());
	    			sg.setDestino(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getDestino());
	    			sg.setNumeroPassageiros(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getNumPassageiros());
	    			sg.setInformacoesAdicionais(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getAdicional());
	    			
	    			GlobalTaxi.getGlobalExpress().setUsuarioGson(us);
	    			GlobalTaxi.getGlobalExpress().setSolicitacaoGson(sg);
	    			getTabHost().setCurrentTab(1);
	            }

	        })
	        .setNegativeButton(R.string.alertBotaoCancelar, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	        		String dadosTela [] = {"idTaxista="+GlobalTaxi.getGlobalExpress().getUsuarioBean().getId(), "v="+GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile().getValorCorrida()};
	    			JSONObject respostaJson = Conexao.conecta(FinalizaCorrida.ENDERECO_URL, dadosTela);
	    			try {
	    				//TODO esperar url para finalizar a corrida pela web
	    				if(respostaJson != null){
	    					Log.d(GlobalTaxi.getGlobalExpress().getTag(), ""+respostaJson);
	    					if(respostaJson.has("Erro")){
	    							JSONObject dadosTaxista = (JSONObject)respostaJson.getJSONObject("Erro");
	    					}else{
	    						DataBase database = new DataBase(getApplicationContext());
	    						SolicitacaoTaxistaMobile stm = GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile();
	    						stm.setCorridaJaFinalizada(true);
	    						database.atualizaSolicitacaoTaxistaMobile(stm);
	    						database.close();

	    						GlobalTaxi.getGlobalExpress().setUsuarioGson(null);
	    						GlobalTaxi.getGlobalExpress().setSolicitacaoGson(null);
	    						GlobalTaxi.getGlobalExpress().setStatusCorrida(false);//coloca o taxista como false na corrida, ou seja, esta livre
	    					}
	    				}
	    				//TODO retirar depois que tiver a url para finalizar
						DataBase database = new DataBase(getApplicationContext());
						SolicitacaoTaxistaMobile stm = GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile();
						stm.setCorridaJaFinalizada(true);
						database.atualizaSolicitacaoTaxistaMobile(stm);
						database.close();

						GlobalTaxi.getGlobalExpress().setUsuarioGson(null);
						GlobalTaxi.getGlobalExpress().setSolicitacaoGson(null);
						GlobalTaxi.getGlobalExpress().setStatusCorrida(false);//coloca o taxista como false na corrida, ou seja, esta livre
	    				//até aqui
	    			} catch (JSONException e) {
	    				e.printStackTrace();
	    			}
	            }
	        })
	        .create();
        }
        return null;
    }

    //
    private void finalizaActivity() {
		startActivity(new Intent(SolicitaTaxiTab.this, TaxiAndroidActivity.class));
	}
    
    @Override
    public void onRestart(){
    	super.onRestart();
    }
}