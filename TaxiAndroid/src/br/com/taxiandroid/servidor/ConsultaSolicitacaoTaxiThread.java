package br.com.taxiandroid.servidor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiandroid.R;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiandroid.util.HttpProxy;
import br.com.taxiweb.gson.DeviceAutenticadoGson;
import br.com.taxiweb.gson.SolicitacaoGson;

/**
 * Faz a conexao com o servidor e consulta Login do cliente
 * @param progress
 */
public class ConsultaSolicitacaoTaxiThread extends AsyncTask <Void, Void, String>{
	private ProgressDialog dialog;
	public Context applicationContext;
	public boolean naoMostraDialog;
	private final String SOLICITACOES_PENDENTES = "taxiWeb/seam/resource/rest/solicitacaoTaxi/solicitacoesPendentes/";
	private List<SolicitacaoGson> 	listaSolicitacao 	= null;
	
	@Override
	public void onPreExecute(){
//		if(!naoMostraDialog)
			this.dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), "Consultando Corridas...", true);
	}
	
	@Override
	public String doInBackground(Void... params) {
		String emailUser = URLEncoder.encode(GlobalTaxi.getGlobalExpress().getUsuarioBean().getLogin());
		DeviceAutenticadoGson sg = new DeviceAutenticadoGson();
		sg.setSenhaDevice(GlobalTaxi.getGlobalExpress().getUsuarioBean().getSenha());
//		String dadosPost [] = {"json="+DeviceAutenticadoGson.toJSON(sg)};
//		JSONObject solicitacao = Conexao.conecta(SolicitacaoTaxistaUrl+emailUser, dadosPost, null);
//	    paramServer.put("json", DeviceAutenticadoGson.toJSON(sg));
//	    String res = wb.webInvoke("solicitacoesPendentes/"+emailUser, DeviceAutenticadoGson.toJSON(sg), "application/json");
		String result = null;
		try {
    		if(GlobalTaxi.getGlobalExpress().isDebug())
    			Log.d(GlobalTaxi.getGlobalExpress().getTag(), "Buscando solicitações");
			result = HttpProxy.httpPost(GlobalTaxi.getGlobalExpress().getUrl()+SOLICITACOES_PENDENTES+emailUser, new DefaultHttpClient(), DeviceAutenticadoGson.toJSON(sg));
//			result = 
//				"[{\"idSolicitacao\":1,\"origem\":\"df1\",\"destino\":\"df2\",\"dataHora\":\"Jan 7, 2012 6:34:00 PM\",\"numeroPassageiros\":1,\"informacoesAdicionais\":\"df3\"},"+
//				"{\"idSolicitacao\":2,\"origem\":\"df4\",\"destino\":\"df5\",\"dataHora\":\"Jan 7, 2012 6:47:00 PM\",\"numeroPassageiros\":1,\"informacoesAdicionais\":\"df6\"}]";
				
//			result =
//				"[{\"idSolicitacao\":1,\"origem\":\"Shin Qi 6 Conjunto 9, Brasília - Distrito Federal, Brasil\",\"destino\":\"Sqn 203 Bloco J, Brasília - Distrito Federal, Brasil\",\"dataHora\":\"May 19, 2012 10:11:00 AM\",\"numeroPassageiros\":4,\"informacoesAdicionais\":\"teste1\"}," +
//				"{\"idSolicitacao\":4,\"origem\":\"Shin Qi 11 Conjunto 12, Brasília - Distrito Federal, Brasil\",\"destino\":\"Aeroporto Internacional de Brasília Presidente Juscelino Kubitschek, Brasilia - Distrito Federal, Brazil\",\"dataHora\":\"May 19, 2012 11:02:00 AM\",\"numeroPassageiros\":1,\"informacoesAdicionais\":\"\"}]";
		} catch (IOException e) {
			e.printStackTrace();
		}
//		JSONObject solicitacao = null;
		DataBase dataBase = new DataBase(applicationContext);
		GlobalTaxi.getGlobalExpress().setUsuarioBean(dataBase.selectUsuario());
		if(GlobalTaxi.getGlobalExpress().isDebug() && GlobalTaxi.getGlobalExpress().getUsuarioBean() != null)
			Log.d(GlobalTaxi.getTag(), ""+GlobalTaxi.getGlobalExpress().getUsuarioBean().getLogin());
		dataBase.close();
	    return result;
	}
	
	@Override
	public void onPostExecute(String result){
//		if(!naoMostraDialog)
			this.dialog.dismiss();
		
    	if(result != null && !result.equals("403")){
			if(result != null){//Caso já tenha um usuário e senha cadastrado já valida abre direto sem precisar digitar.
				listaSolicitacao = (List<SolicitacaoGson>)new SolicitacaoGson().recuperaSolicitacoes(result);
	    	}
		}else{
			Toast.makeText(applicationContext, "Não existe solicitações no momento. Aguarde!!!", Toast.LENGTH_LONG);
			DataBase database = new DataBase(applicationContext);
			if(GlobalTaxi.getGlobalExpress().getUsuarioBean() != null){
				database.deleteUsuario(GlobalTaxi.getGlobalExpress().getUsuarioBean().getId());
			}
			database.close();
		}
	}
	
	public List getListaSolicitacao(){
		return this.listaSolicitacao;
	}
}