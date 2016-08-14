package br.com.taxiandroid.servidor;

import java.io.IOException;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiandroid.R;
import br.com.taxiandroid.SolicitaTaxiTab;
import br.com.taxiandroid.bean.SolicitacaoTaxistaMobile;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiandroid.util.HttpProxy;
import br.com.taxiandroid.util.Utilitario;
import br.com.taxiweb.gson.ReservaSolicitacaoGson;
import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.gson.UsuarioGson;

import com.google.gson.Gson;


/////
 // Faz a conexao com o servidor e consulta Login do cliente
 // @param progress
 ///
public class ReservaSolicitacaoThread extends AsyncTask <Void, Void, String>{
	
	private ProgressDialog dialog;
	public Context applicationContext;
	public ReservaSolicitacaoGson reservaSolicitacaoGson;
	public SolicitacaoGson solicitacaoGson;
	public Activity tabGeral;
	private DataBase database;
	private final String SOLICITACOES_RESERVADA = "taxiWeb/seam/resource/rest/solicitacaoTaxi/reservaSolicitacao/";
	private UsuarioGson ug = null;
	
	@Override
	protected void onPreExecute(){
		this.dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), "Reservando solicitação...", true);
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String result = null;
		try {
			result = HttpProxy.httpPut(GlobalTaxi.getGlobalExpress().getUrl()+SOLICITACOES_RESERVADA+GlobalTaxi.getGlobalExpress().getUsuarioBean().getLogin(), new DefaultHttpClient(), new Gson().toJson(reservaSolicitacaoGson));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result){
		this.dialog.dismiss();
		DataBase database = new DataBase(applicationContext);
		if(result != null){
			ug = UsuarioGson.fromJSON(result);
			GlobalTaxi.getGlobalExpress().setUsuarioGson(ug);
			GlobalTaxi.getGlobalExpress().setSolicitacaoGson(solicitacaoGson);
			GlobalTaxi.getGlobalExpress().setStatusCorrida(true);//coloca o taxista como true na corrida, ou seja, está ocupado
			SolicitacaoTaxistaMobile stm = preencheSolicitacaoTaxistaMobile(solicitacaoGson);
			GlobalTaxi.getGlobalExpress().setSolicitacaoTaxistaMobile(stm);
			database.insert_reserva(stm);
			((SolicitaTaxiTab)tabGeral.getParent()).getTabHost().setCurrentTab(1);//Faz a mudanca para aba solicitação
    	}else{
    		database.deleteUsuario(GlobalTaxi.getGlobalExpress().getUsuarioBean().getId());
    		Toast.makeText(applicationContext, "Erro ao validar usuário!!! \n Tente novamente!!!", 6000);
    	}
		database.close();
	}

	/**
	 * Preenche Bean SolicitacaoTaxistaMobile
	 * @param solicitacaoGson
	 * @return
	 */
    private SolicitacaoTaxistaMobile preencheSolicitacaoTaxistaMobile(SolicitacaoGson solicitacaoGson) {
//		   " CREATE TABLE IF NOT EXISTS "+DataBase.TABELA_SOLICITACAO+" ("+DataBase.COLUNA_ID_SOL+" int, "+DataBase.COLUNA_ID_SOLICITACAO+" long, "+DataBase.COLUNA_ORIGEM+" VARCHAR(250) NOT NULL, "+DataBase.COLUNA_DESTINO+" VARCHAR(250), "+DataBase.COLUNA_DATAHORA +" String, "+DataBase.COLUNA_NUM_PASSAGEIROS +" INTEGER, "+DataBase.COLUNA_INFOR_ADICIONAIS +" VARCHAR(100), "+DataBase.COLUNA_IS_FINALIZADA +" INTEGER))";
 	
 	SolicitacaoTaxistaMobile stm = new SolicitacaoTaxistaMobile();
 	stm.setIdSolicitacaoTaxistaMobile(solicitacaoGson.getIdSolicitacao());
 	stm.setEndereco(solicitacaoGson.getOrigem());
 	stm.setDestino(solicitacaoGson.getDestino());
 	stm.setData(Utilitario.transformaDataString(solicitacaoGson.getDataHora(), "dd'/'MM'/'yyyy' - 'HH':'mm'h'"));
 	stm.setNumPassageiros(solicitacaoGson.getNumeroPassageiros());
 	stm.setAdicional(solicitacaoGson.getInformacoesAdicionais());
 	stm.setCorridaJaFinalizada(false);
 	stm.setNome(GlobalTaxi.getGlobalExpress().getUsuarioGson().getNome());
 	stm.setSobreNome(GlobalTaxi.getGlobalExpress().getUsuarioGson().getSobreNome());
 	stm.setCelular(GlobalTaxi.getGlobalExpress().getUsuarioGson().getCelular());
 	
 	return stm;
 }
    public UsuarioGson getUsuarioGson(){
    	return this.ug;
    }
	
}
