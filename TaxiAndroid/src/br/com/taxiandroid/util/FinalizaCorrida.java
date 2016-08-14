package br.com.taxiandroid.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.com.taxiandroid.GlobalTaxi;

/**
 * Envia localizacao taxista
 */
public class FinalizaCorrida extends AsyncTask <Void, Void, JSONObject>{
    	protected Context applicationContext;
    	private String valor;
    	public static String ENDERECO_URL = "/expresstaxi/extras/taxista/finalizaCorrida.php";
    	private boolean isCorridaFinalizada = true;

    	@Override
    	protected void onPreExecute(){
    	}
    	
		@Override
		protected JSONObject doInBackground(Void... params) {
    		JSONObject respostaJson = null;
    		//TODO verificar se a corrida é liberada no server, verifica id do usuario.
    		String dadosTela [] = {"idTaxista="+GlobalTaxi.getGlobalExpress().getUsuarioBean().getId(), "v="+valor};
		    respostaJson = Conexao.conecta(ENDERECO_URL, dadosTela);

			return respostaJson;
		}
		
		@Override
		protected void onPostExecute(JSONObject result){
			//TODO retirar depois que for feita a transacao no servidor
			setCorridaFinalizada(true);
			try {
				if(result != null){
					Log.d(GlobalTaxi.getGlobalExpress().getTag(), ""+result);
					if(result.has("Erro")){
							JSONObject dadosTaxista = (JSONObject)result.getJSONObject("Erro");
					}
					setCorridaFinalizada(!result.has("Erro"));//Não foi finalizada se tiver erro
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		public void setValor(String valor){
			this.valor = valor;
		}
		public String getValor(){
			return this.valor;
		}

		public void setCorridaFinalizada(boolean isCorridaFinalizada) {
			this.isCorridaFinalizada = isCorridaFinalizada;
		}

		public boolean isCorridaFinalizada() {
			return isCorridaFinalizada;
		}
    }	