package br.com.taxiandroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import br.com.taxiandroid.bean.SolicitacaoTaxistaMobile;
import br.com.taxiandroid.bean.UsuarioBean;
import br.com.taxiandroid.servidor.ConsultaSolicitacaoTaxi;
import br.com.taxiandroid.servidor.ConsultaSolicitacaoTaxiThread;
import br.com.taxiandroid.servidor.ReservaSolicitacaoThread;
import br.com.taxiandroid.util.Conexao;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiandroid.util.FinalizaCorrida;
import br.com.taxiandroid.util.HttpProxy;
import br.com.taxiandroid.util.Utilitario;
import br.com.taxiweb.gson.PosicaoTaxistaGson;
import br.com.taxiweb.gson.ReservaSolicitacaoGson;
import br.com.taxiweb.gson.SolicitacaoGson;

@SuppressLint("ParserError")
public class TabGeral extends Activity implements LocationListener, OnItemClickListener{
	final Handler mHandler = new Handler();
	private Bundle mySavedInstanceState 				= null;

	private String urlInsereLatLongTaxista = "taxiWeb/seam/resource/rest/solicitacaoTaxi/atualizaPosicaoTaxista/";
//	private long tempoConexao = 5000;
//	private float distanciaConexao = 100.0f;

	private long tempoConexao = 0;
	private float distanciaConexao = 0.0f;
	
	//Menu
	public static int 				menuStatusLivre 			= 5;
	private ListView				listview 					= null;
	private List<SolicitacaoGson> 	listaSolicitacao 			= null;
	private String [] 				dadosListaSolicitacoes 		= null;
	private ReservaSolicitacaoGson	reservaSolicitacaoGson 		= null;
	private SolicitacaoGson 		solicitacaoGson 			= null;
	private Menu 					menuGlobal 					= null;
	private ProgressDialog 			dialogConsultaSolicitacao 	= null; 
	
	// Create runnable for posting
//    final Runnable mUpdateResults = new Runnable() {
//        public void run() {
//            updateResultadosNaTela();
//        }
//    };

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySavedInstanceState = savedInstanceState;
        iniciaTela();
        getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, tempoConexao, distanciaConexao, this);
//		mHandler.post(mUpdateResults);
        updateResultadosNaTela();
    }

	@Override
	public void onLocationChanged(Location location) {
		if(GlobalTaxi.getGlobalExpress().isDebug()){
			Log.d("tabGeral","passou onLocationChanged");
    			Log.d("GPSTaxista", "onLocation");
		}
		GlobalTaxi.getGlobalExpress().setLocalizacao(location);
		//TODO Verificar se o taxista ficará polando o tempo todo no servidor ou se só quando tiver uma solicatação aceita.
		//Só alimenta a base com lat/long caso o taxista tenha aceitado uma solicitação
		if(GlobalTaxi.getGlobalExpress().getSolicitacaoGson() != null){
			AtualizaLocalizacaoTaxista alt = new AtualizaLocalizacaoTaxista();
			alt.locationInner = location;
			alt.execute();
		}
//		mHandler.post(mUpdateResults);
		updateResultadosNaTela();
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

	private synchronized void updateResultadosNaTela() {
 		if(GlobalTaxi.getGlobalExpress().getLinhasTabGeral().length() > 0){
			onCreate(mySavedInstanceState);
		}
 		//Se estiver livre será atualizada a lista de solicitacao
 		if(!GlobalTaxi.getGlobalExpress().getStatusCorrida() &&  !GlobalTaxi.getGlobalExpress().isConsultaSolicitacaoTaxiExecutando()){
// 			new Thread(){public void run(){
// 				dialogConsultaSolicitacao = ProgressDialog.show(TabGeral.this, getString(R.string.aguarde), "Consultando Corridas...", true);
// 			}}.start();
			
// 			ConsultaSolicitacaoTaxi cll = new ConsultaSolicitacaoTaxi();
//			if((listaSolicitacao = cll.consultaSolicitacaoTaxi(this, true)) != null){
//				carregaTabela();
//				fechaDialog();
//			}else{
//				fechaDialog();	
//				if(GlobalTaxi.getGlobalExpress().getUsuarioBean() == null){//Setado caso o retorno seja 403
//					//TODO  testar senha com prazo vencido.
//					finish();
//				}
//			}
 	        
 			ConsultaSolicitacaoTaxi cll = new ConsultaSolicitacaoTaxi();
 			if((listaSolicitacao = cll.consultaSolicitacaoTaxi(this, true)) != null){
 				carregaTabela();
 			}
 			
 		}
 		if(menuGlobal != null){
	 		if(!GlobalTaxi.getGlobalExpress().getStatusCorrida()){
		    	menuGlobal.removeItem(menuStatusLivre);
	 		}else{
	 			if(menuGlobal.size() < 2){
	 				menuGlobal.add(0, menuStatusLivre, 0, "Livre");
	 			}
	 		}
 		}
    }

	private void fechaDialog() {
		if(dialogConsultaSolicitacao != null){
			dialogConsultaSolicitacao.dismiss();
		}
	}

	private void iniciaTela() {
        setContentView(R.layout.tab_geral);
		ConsultaSolicitacaoTaxiThread cstt = new ConsultaSolicitacaoTaxiThread();
		cstt.applicationContext = this;
//		cstt.naoMostraDialog = false;
		cstt.execute();
		if(cstt.getListaSolicitacao() != null){
			carregaTabela();
		}
	}
	
	
	private void carregaTabela() {
		listview = (ListView)findViewById(R.id.listView1);
		listview.setOnItemClickListener(this);
		// SimpleListAdapter is designed for binding to a Cursor.
//        ListAdapter adapter = new SimpleCursorAdapter(
//                this, // Context.
//                android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
//                mCursor,                                              // Pass in the cursor to bind to.
//                new String[] {People.NAME, People.COMPANY},           // Array of cursor columns to bind to.
//                new int[] {android.R.id.text1, android.R.id.text2});  // Parallel array of which template objects to bind to those columns.
//
//        // Bind to our new adapter.
//        setListAdapter(adapter);
//		String [] dados = {"Solicitacao 1","Solicitacao 2","Solicitacao 3","Solicitacao 4","Solicitacao 5","Solicitacao 6"};
		if(listaSolicitacao != null && !GlobalTaxi.getGlobalExpress().getStatusCorrida()){
			dadosListaSolicitacoes = montaLista();
			if(dadosListaSolicitacoes == null){
				dadosListaSolicitacoes = new String[1];
				showDialog(2);
			}else{
				SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
						getDadosSolicitacaoGson(getListaSolicitacaoGson()),
						R.layout.simple_list_item3,
						new String[] {"origem", "qntPassageiros", "textoEspaco"},
						new int[] {
							R.id.text1,
							R.id.text2,
							R.id.text3});		
				
//				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, dadosListaSolicitacoes);
				listview.setAdapter(adapter);
				//TODO retirar para uso da vers�o free
		//		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener (){ 
		//			@Override 
		//			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		//				lei_id = pos;
		////				onLongListItemClick(v,pos,id);
		//				registerForContextMenu(v);
		//				return false; 
		//			} 
		//		}); 
			}
		}
	}
	
	   public List<Map<String, String>>getDadosSolicitacaoGson(SolicitacaoGson[] solicitacaoes){
   		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		    	for(int i = 0; i < solicitacaoes.length; i++){
		    	    Map<String, String> datum = new HashMap<String, String>(2);
		    	    datum.put("origem", ""+solicitacaoes[i].getOrigem());
		    	    datum.put("qntPassageiros", "Qnt Passageiros: "+solicitacaoes[i].getNumeroPassageiros());
		    	    datum.put("textoEspaco", "");
		    		
		    		data.add(datum);	
		    	}
	    return data;
   }
	
	private void fazReserva(int position) {
		reservaSolicitacaoGson = new ReservaSolicitacaoGson();
		reservaSolicitacaoGson.setSenhaDevice(GlobalTaxi.getGlobalExpress().getUsuarioBean().getSenha());
		solicitacaoGson = getSolicitacaoBeanFromLista(dadosListaSolicitacoes[position]);
		reservaSolicitacaoGson.setIdSolicitacao(solicitacaoGson.getIdSolicitacao());
		showDialog(3);
	}
	
	private SolicitacaoGson getSolicitacaoBeanFromLista(String nome) {
		Iterator<SolicitacaoGson> i = listaSolicitacao.iterator();
		int cont = 0;
		while(i.hasNext()){
			SolicitacaoGson sg = (SolicitacaoGson)i.next();
			if(nome.equals(sg.getOrigem())){
				return sg;
			}
			cont++;
		}
		return null;
	}

	
	private String[] montaLista() {
		String [] dadosLista = null;
		if(listaSolicitacao != null){
			dadosLista = new String[listaSolicitacao.size()];
			Iterator<SolicitacaoGson> i = listaSolicitacao.iterator();
			int cont = 0;
			while(i.hasNext()){
				dadosLista[cont] = ((SolicitacaoGson)i.next()).getOrigem();
				cont++;
			}
		}
		return dadosLista;
	}

	private SolicitacaoGson[] getListaSolicitacaoGson() {
		SolicitacaoGson [] solicitacaoGson = null;
		if(listaSolicitacao != null){
			solicitacaoGson = new SolicitacaoGson[listaSolicitacao.size()];
			Iterator<SolicitacaoGson> i = listaSolicitacao.iterator();
			int cont = 0;
			while(i.hasNext()){
				solicitacaoGson[cont] = ((SolicitacaoGson)i.next());
				cont++;
			}
		}
		return solicitacaoGson;
	}
	
	/**
	 * Remove item da lista local.
	 * @param id
	 * @return
	 */
	private String[] removeItemLista(int id) {
		String [] dadosLista = new String[listaSolicitacao.size()];
		Iterator<SolicitacaoGson> i = listaSolicitacao.iterator();
		int cont = 0;
		while(i.hasNext()){
			SolicitacaoGson sg = (SolicitacaoGson)i.next();
			if(sg.getIdSolicitacao() != id)
				dadosLista[cont] = (sg).getOrigem();
			cont++;
		}
		return dadosLista;
	}
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
	    case 2:
	    	//Confirmacaoo de solicitação
	    	LayoutInflater factoryStatus = LayoutInflater.from(this);
	        final View statusLivre = factoryStatus.inflate(R.layout.status_livre_dialog, null);
	        return new AlertDialog.Builder(TabGeral.this)
	        .setIcon(R.drawable.confirmacao)
	        .setTitle("Mudança para Status - Livre")
	        .setView(statusLivre)
	        .setPositiveButton(R.string.alertBotaoOk, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		AlertDialog alertDialog = (AlertDialog)dialog;
	        		EditText edtxt = (EditText) alertDialog.findViewById(R.id.edTxtValorCorrida);
	        		String valor = edtxt.getText().toString();
	        		finalizaCorrida(valor);
	            }
	        })
	        .setNegativeButton(R.string.alertBotaoCancelar, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {	            	
	            }
	        })
	        .create();
	    case 3:
	    	//Confirmacaoo de solicitacao
	    	LayoutInflater factoryReserva = LayoutInflater.from(this);
	        final View reservaSolic = factoryReserva.inflate(R.layout.reserva_solicitacao_dialog, null);
	        return new AlertDialog.Builder(TabGeral.this)
	        .setIcon(R.drawable.confirmacao)
	        .setTitle(R.string.titReservaSolicitacao)
	        .setView(reservaSolic)
	        .setPositiveButton(R.string.alertBotaoOk, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        		ReservaSolicitacaoThread rs = new ReservaSolicitacaoThread();
	        		rs.applicationContext = TabGeral.this;
	        		rs.reservaSolicitacaoGson = reservaSolicitacaoGson;
	        		rs.solicitacaoGson = solicitacaoGson;
	        		rs.tabGeral = TabGeral.this;
	        		rs.execute();
	        		if(rs.getUsuarioGson() != null){
//	        			menuGlobal.getItem(1).setVisible(true);//menuStatusLivre
//	        			menuGlobal.findItem(menuStatusLivre).setVisible(true);
	        			abreAbaSolicitacao();
	        		}
	            }
	        })
	        .setNegativeButton(R.string.alertBotaoCancelar, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {	            	
	            }
	        })
	        .create();
        }

        return null;
    }

    private void abreAbaSolicitacao() {
		((SolicitaTaxiTab)getParent()).getTabHost().setCurrentTab(1);
		GlobalTaxi.getGlobalExpress().getTabHost().getTabWidget().setCurrentTab(1);
    }
    
	private void finalizaCorrida(String vlr) {
		FinalizaCorrida alt = new FinalizaCorrida();
		alt.setValor(vlr);
		alt.execute();
		
		if(alt.isCorridaFinalizada()){
			DataBase db = new DataBase(getApplicationContext());
			SolicitacaoTaxistaMobile stm = GlobalTaxi.getGlobalExpress().getSolicitacaoTaxistaMobile();
			stm.setCorridaJaFinalizada(true);
			stm.setValorCorrida(vlr);
			long result = db.atualizaSolicitacaoTaxistaMobile(stm);
			db.close();
			if(result > 0){
				FinalizaCorridaServidor fcs = new FinalizaCorridaServidor();
				fcs.applicationContext = this;
				fcs.execute();
			}
			
			if(GlobalTaxi.getGlobalExpress().isDebug()){
				Log.d(GlobalTaxi.getGlobalExpress().getTag(), "SolicitacaoTaxistaMobile alterada para finalizada");
			}
//			menuGlobal.findItem(menuStatusLivre).setVisible(true);
//			menuGlobal.getItem(1).setVisible(false);//menuStatusLivre
		}
	}
    

	/**
	 * Cria menus na aplicação
	 */
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
	    	menuGlobal = menu;
	    	//Usado para incluir/retirar do menu o item  
	    	if(!GlobalTaxi.getGlobalExpress().getStatusCorrida()){
		    	menuGlobal.removeItem(menuStatusLivre);
	 		}else{
	 			if(menuGlobal.size() < 2){
	 				menuGlobal.add(0, menuStatusLivre, 0, "Livre");
	 			}
	 		}

	    	
//			menuGlobal.findItem(menuStatusLivre).setVisible(false);
//    	menuGlobal.getItem(1).setVisible(false);//menuStatusLivre
	    	//item1.setIcon(R.drawable.sugestao_endereco);
	    	
//	    	MenuItem item2 = menu.add(0, menuMostraSolicitacao, 0, "Mostra Solicita��o");
//	    	item2.setIcon(R.drawable.segue_taxista);
	    	return true;
	    }
	/**
	/* Ação menu
	 * 
	 */
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
			switch (item.getItemId()) {
				case (5):{
					showDialog(2);
					return true;
				}
				default:{
					super.onOptionsItemSelected(item);
				}
			}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    return super.onPrepareOptionsMenu(menu);
	}
	
    private void desabilitaTab() {
    	//GlobalTaxi.getGlobalExpress().getTabHost().getChildAt(0).setEnabled(false);
    	int idTabSolic = GlobalTaxi.getGlobalExpress().getTabHost().getCurrentTab();
    	GlobalTaxi.getGlobalExpress().getTabHost().getChildAt(idTabSolic).setEnabled(false);
    	GlobalTaxi.getGlobalExpress().getTabHost().setCurrentTabByTag("Solicitação");
    	GlobalTaxi.getGlobalExpress().getTabHost().getTabWidget().setCurrentTab(1);//Solicitacao	
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position , long id) {
		//se estiver false no status corrida, ou seja, está livre
		if(!GlobalTaxi.getGlobalExpress().getStatusCorrida()){
			fazReserva(position);
		}else{
			Toast.makeText(getApplicationContext(), R.string.toastStatusOcupado, 7000).show();
		}
	}
	
	/**
	 * Envia localizacao taxista
	 */
	private class AtualizaLocalizacaoTaxista extends AsyncTask <Void, Void, JSONObject>{
	    	private Location locationInner;

	    	@Override
	    	protected void onPreExecute(){
	    	}
	    	
			@Override
			protected JSONObject doInBackground(Void... params) {
	    		JSONObject respostaJson = null;

		    	PosicaoTaxistaGson ptg = new PosicaoTaxistaGson();
		    	ptg.setLatitude(locationInner.getLatitude());
		    	ptg.setLongitude(locationInner.getLongitude());
		    	//tem que esperar o servidor implementar o rest para localizacao do taxista
//		    	if(GlobalTaxi.getGlobalExpress().getSolicitacaoGson() != null){
		    		ptg.setIdSolicitacao(GlobalTaxi.getGlobalExpress().getSolicitacaoGson().getIdSolicitacao());
//		    	}else{
//		    		ptg.setIdSolicitacao(0);
//		    	}
		    	ptg.setSenhaDevice(GlobalTaxi.getGlobalExpress().getUsuarioBean().getSenha());
    				
	    		Log.d("", "dadosTela "+"lat="+locationInner.getLatitude()+" lng="+locationInner.getLongitude()+" --"+ " dtDevice="+Utilitario.getDataHora());
	    	
		    	try{
		    		HttpProxy.httpPut(GlobalTaxi.getGlobalExpress().getUrl() + urlInsereLatLongTaxista+GlobalTaxi.getGlobalExpress().getUsuarioBean().getLogin(), new DefaultHttpClient(), PosicaoTaxistaGson.toJSON(ptg));
		    	}catch(Exception ex){
		    		
		    	}

	    	
	    	return respostaJson;
			}
			
			@Override
			protected void onPostExecute(JSONObject result){
		    	//tem que esperar o servidor implementar o rest para localizacao do taxista				
//				updateResultadosNaTela();
			}
	    }	
	
	
    /**
     * Faz a conexao com o servidor e finaliza a corrida no server, muda status para 3(finalizada)
     * @param progress
     */
	private class FinalizaCorridaServidor extends AsyncTask <Void, Void, String>{
    	private 	ProgressDialog 	dialog;
    	public 		Context 		applicationContext;
    	private 	DataBase 		dataBase = null;
    	private final String 		finalizaUrl = "taxiWeb/seam/resource/rest/solicitacaoTaxi/finalizaSolicitacao/";
    	private 	UsuarioBean 	usu		 = GlobalTaxi.getGlobalExpress().getUsuarioBean(); 

    	@Override
    	protected void onPreExecute(){
    		this.dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), applicationContext.getString(R.string.finalizandoCorrida), true);
    	}
    	
    	@Override
    	protected String doInBackground(Void... params) {
    		 String resposta = null;
    		try {
    			String dados = "emailTaxista="+usu.getLogin();
    			//Inserindo nome de campos para o HttpPost
	    		HttpProxy.httpPut(GlobalTaxi.getGlobalExpress().getUrl() + finalizaUrl + dados, new DefaultHttpClient(), ReservaSolicitacaoGson.toJSON(reservaSolicitacaoGson));    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		if(GlobalTaxi.getGlobalExpress().isDebug())
    			Log.d(GlobalTaxi.getTag(), ""+resposta);
//    		}
    		return resposta;
    	}
    	
    	@Override
    	protected void onPostExecute(String result){
    		this.dialog.dismiss();
    		if(result != null){
				GlobalTaxi.getGlobalExpress().setSolicitacaoTaxistaMobile(null);
		        GlobalTaxi.getGlobalExpress().setUsuarioGson(null);
				GlobalTaxi.getGlobalExpress().setSolicitacaoGson(null);
				GlobalTaxi.getGlobalExpress().setStatusCorrida(false);				
        	}else{
    			Utilitario.alert(R.string.alertTituloErro, R.string.erroAoFinalizarCorrida, R.string.alertBotaoOk, -1, (Activity)applicationContext);
        	}
    	}
    }
}