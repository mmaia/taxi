package br.com.taxiandroid.servidor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiandroid.util.HttpProxy;
import br.com.taxiweb.gson.DeviceAutenticadoGson;
import br.com.taxiweb.gson.SolicitacaoGson;

/**
 * Faz a conexao com o servidor e consulta Login do cliente
 * @param progress
 */
public class ConsultaSolicitacaoTaxi{
	private final String SOLICITACOES_PENDENTES = "taxiWeb/seam/resource/rest/solicitacaoTaxi/solicitacoesPendentes/";
	private List<SolicitacaoGson> 	listaSolicitacao 	= null;
	
	public List consultaSolicitacaoTaxi(Context applicationContext, boolean naoMostraDialog) {
		GlobalTaxi.getGlobalExpress().setConsultaSolicitacaoTaxiExecutando(true);
//		if(!naoMostraDialog){
////			dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), "Consultando Corridas...", true);
//			dialog = ProgressDialog.show(applicationContext, "Aguarde", "Consultando Corridas...", true);
//		}
		String emailUser = ""; 
		String result = null;
		DeviceAutenticadoGson sg = new DeviceAutenticadoGson();

		if(GlobalTaxi.getGlobalExpress().getUsuarioBean() != null){
			emailUser = URLEncoder.encode(GlobalTaxi.getGlobalExpress().getUsuarioBean().getLogin());
			sg.setSenhaDevice(GlobalTaxi.getGlobalExpress().getUsuarioBean().getSenha());
			try {
	    		if(GlobalTaxi.getGlobalExpress().isDebug())
	    			Log.d(GlobalTaxi.getGlobalExpress().getTag(), "Buscando solicitacoes");
				result = HttpProxy.httpPost(GlobalTaxi.getGlobalExpress().getUrl()+SOLICITACOES_PENDENTES+emailUser, new DefaultHttpClient(), DeviceAutenticadoGson.toJSON(sg));
			} catch (IOException e) {
				e.printStackTrace();
			}
			DataBase dataBase = new DataBase(applicationContext);
			GlobalTaxi.getGlobalExpress().setUsuarioBean(dataBase.selectUsuario());
			if(GlobalTaxi.getGlobalExpress().isDebug() && GlobalTaxi.getGlobalExpress().getUsuarioBean() != null)
				Log.d(GlobalTaxi.getTag(), ""+GlobalTaxi.getGlobalExpress().getUsuarioBean().getLogin());
			dataBase.close();
		
//			if(!naoMostraDialog)
//				dialog.dismiss();
		}
    	if(result != null){
    		if(!result.equals("403")){
				listaSolicitacao = (List<SolicitacaoGson>)new SolicitacaoGson().recuperaSolicitacoes(result);
	    	}else{
	    		DataBase database = new DataBase(applicationContext);
	    		if(GlobalTaxi.getGlobalExpress().getUsuarioBean() != null){
	    			long res = database.deleteUsuario(GlobalTaxi.getGlobalExpress().getUsuarioBean().getId());
	    			if(res > 0){
	    				GlobalTaxi.getGlobalExpress().setUsuarioBean(null);
	    			}
	    		}
	    		database.close();
	    	}
		}else{
			Toast.makeText(applicationContext, "Não existe solicitacae no momento. Aguarde!!!", Toast.LENGTH_LONG);
		}
    	GlobalTaxi.getGlobalExpress().setConsultaSolicitacaoTaxiExecutando(false);
    	return listaSolicitacao;
	}
}//end ConsultaLogin
