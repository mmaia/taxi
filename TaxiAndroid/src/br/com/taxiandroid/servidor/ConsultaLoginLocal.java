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
public class ConsultaLoginLocal {
	private ProgressDialog dialog;

	public UsuarioBean consultaLoginLocal(Context applicationContext) {
		this.dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), applicationContext.getString(R.string.conectando), true);
		DataBase dataBase = new DataBase(applicationContext);
		GlobalTaxi.getGlobalExpress().setUsuarioBean(dataBase.selectUsuario());
		
		if(GlobalTaxi.getGlobalExpress().isDebug()){
			Log.d(GlobalTaxi.getTag(), "Consultando login local"+GlobalTaxi.getGlobalExpress().getUsuarioBean());
		}
		
		dataBase.close();

	    this.dialog.dismiss();
		GlobalTaxi.getGlobalExpress().setLogado(GlobalTaxi.getGlobalExpress().getUsuarioBean() != null);
		if(GlobalTaxi.getGlobalExpress().isLogado()){//Caso já tenha um usuário e senha cadastrado já valida abre direto sem precisar digitar.
//			consultaSolicitacaoTaxi(result);
    	}
		return GlobalTaxi.getGlobalExpress().getUsuarioBean();
	}

}//end ConsultaLogin
