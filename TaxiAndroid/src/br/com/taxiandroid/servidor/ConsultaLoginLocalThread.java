package br.com.taxiandroid.servidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiandroid.R;
import br.com.taxiandroid.bean.UsuarioBean;
import br.com.taxiandroid.util.DataBase;

/**
 * Faz a conexao com o servidor e consulta Login do cliente
 * @param progress
 */
public class ConsultaLoginLocalThread extends AsyncTask <Void, Void, UsuarioBean>{
	private ProgressDialog dialog;
	protected Context applicationContext;

	@Override
	protected void onPreExecute(){
		this.dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), applicationContext.getString(R.string.conectando), true);
	}
	
	@Override
	protected UsuarioBean doInBackground(Void... params) {
		DataBase dataBase = new DataBase(applicationContext);
		GlobalTaxi.getGlobalExpress().setUsuarioBean(dataBase.selectUsuario());
		
		if(GlobalTaxi.getGlobalExpress().isDebug()){
			Log.d(GlobalTaxi.getTag(), "Consultando login local"+GlobalTaxi.getGlobalExpress().getUsuarioBean());
		}
		
		dataBase.close();
	    return GlobalTaxi.getGlobalExpress().getUsuarioBean();
	}
	
	@Override
	protected void onPostExecute(UsuarioBean result){
		this.dialog.dismiss();
		GlobalTaxi.getGlobalExpress().setLogado(result != null);
		if(result != null){//Caso já tenha um usuário e senha cadastrado já valida abre direto sem precisar digitar.
//			consultaSolicitacaoTaxi(result);
    	}
	}

}//end ConsultaLogin
