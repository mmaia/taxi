package br.com.taxiandroid;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.taxiandroid.bean.UsuarioBean;
import br.com.taxiandroid.servidor.ConsultaLoginLocal;
import br.com.taxiandroid.util.Conexao;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiandroid.util.Utilitario;

public class TaxiAndroidActivity extends Activity implements OnClickListener{
	public static int 		menuLogin 					= 1;
	public static int 		menuSolicitaTaxi 			= 2;	
	public static int 		menuCadastro 				= 3;
	public static int 		menuAjuda 					= 4;
	public static int 		controleIdInt 				= 11;	
	public static int 		larguraTela 				= 0;	 
	private EditText 		edTxtLoginAbertura 			= null;//Usado no metodo onCreateDialog 
	
	private Button btn_corridas, btn_login, btn_ajuda, btn_Historico;

	/*Para tela de login*/
	public ProgressDialog dialog;
	private final String dadosTela[] = new String[2];
	public Dialog dialogGlobal;
	private final int TELA_LOGIN = 1;
	private Spinner listviewEmails = null;

	private int resultGPS_Habilitado	= 0;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iniciaAplicacao();
    }
	
	private void iniciaAplicacao() {
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("GPSTaxista");
		startService(serviceIntent);

		ConsultaLoginLocal cll = new ConsultaLoginLocal();
		if(cll.consultaLoginLocal(this) != null){
			consultaSolicitacaoTaxi(GlobalTaxi.getGlobalExpress().getUsuarioBean());
		}
		carregaBotao();

		Utilitario.verificandoNotification(TaxiAndroidActivity.this);
	}

	private void carregaBotao() {
		//Se não tiver usuário local, abre tela para digitar dados usuario/senha
		btn_login    = (Button)findViewById(R.id.btn_login);
		btn_ajuda    = (Button)findViewById(R.id.btn_ajuda);
		btn_corridas = (Button)findViewById(R.id.btn_corridas);
		btn_Historico= (Button)findViewById(R.id.btn_historico);
		
		//Habilita e desabilita botões da tela de abertura
		if(GlobalTaxi.getGlobalExpress().isLogado()){
			btn_login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_login_disable));
			btn_corridas.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_corridas));
			btn_Historico.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_historico));
			btn_login.setEnabled(false);
			btn_corridas.setEnabled(true);
			btn_Historico.setEnabled(true);
		}else{
			btn_login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_login));
			btn_corridas.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_corridas_disable));
			btn_Historico.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_historico_disable));
			btn_login.setEnabled(true);
			btn_corridas.setEnabled(false);
			btn_Historico.setEnabled(false);
			
		}
		btn_login.setOnClickListener(this);
		btn_ajuda.setOnClickListener(this);
		btn_corridas.setOnClickListener(this);
		btn_Historico.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			showDialog(TELA_LOGIN);
			break;
		case R.id.btn_ajuda:
			break;
		case R.id.btn_corridas:
			chamaSolicitaTaxiTab();
			break;
		default:
			finish();
			break;
		}
	}	
	
    private void chamaSolicitaTaxiTab() {
		Utilitario.verificandoNotification(TaxiAndroidActivity.this);
		Intent i = new Intent(getApplicationContext(), SolicitaTaxiTab.class);
		startActivity(i);
    }
	@Override
    protected final Dialog onCreateDialog(int id) {
        switch (id) {
        case TELA_LOGIN:
            dialogGlobal = new Dialog(TaxiAndroidActivity.this);
            dialogGlobal.setContentView(R.layout.logindialog);
            Button btnLogin 		= (Button)dialogGlobal.findViewById(R.id.btnLogin);
            Button btnCadastro 		= (Button)dialogGlobal.findViewById(R.id.btnCadastro);
            Button btnEsqueciSenha 	= (Button)dialogGlobal.findViewById(R.id.btnEsqueciSenha);
            edTxtLoginAbertura 		= (EditText)dialogGlobal.findViewById(R.id.edTxtLoginAbertura);
            
    		listviewEmails 			= (Spinner)dialogGlobal.findViewById(R.id.spinner1);
    		String[] dados = Utilitario.getUsername(getApplicationContext());
    		String[] emails = null;
    		if(dados.length > 0){
    			emails = new String[dados.length+1];
    			for(int i = 0; i < dados.length; i++){
    				emails[i] = dados[i];	
    			}
    		}else{
    			emails = new String[1];
    		}
    		emails[emails.length-1] = "Entrar com novo email";
    		
    		ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.textview_spinner, emails);
//    		adapter.setDropDownViewResource(R.layout.textview_spinner);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		listviewEmails.setAdapter(adapter);
    		
    		listviewEmails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
    				String text = listviewEmails.getItemAtPosition(position).toString();
    				if(text.equals("Entrar com novo email")){
    					edTxtLoginAbertura.setVisibility(View.VISIBLE);
    					edTxtLoginAbertura.setText("");
    					edTxtLoginAbertura.requestFocus();	
    				}else{
    					edTxtLoginAbertura.setVisibility(View.GONE);
    					edTxtLoginAbertura.setText(text);
    				}
    			}
    			public void onNothingSelected(AdapterView<?> parent){
    			}
			});

            
            btnLogin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					fazLogin() ;
				}
			});
            btnCadastro.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getBaseContext(), WebViewTaxi.class);
					startActivity(i);
				}
			});
            btnEsqueciSenha.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					esqueciSenha();
				}
			});
            
            return dialogGlobal;
        }
        return null;
    }
    
    
	//TODO implementar metodo esqueci senha    
	private void esqueciSenha() {
		
	}

	/**
	 * Consulta senha do usuário na base local
	 */
	public void fazLogin(){
		if (validaCamposLogin()) {
			ConsultaLoginServidorThread clst = new ConsultaLoginServidorThread();
			clst.applicationContext = this;
			clst.dadosTela 			= dadosTela;
			clst.execute();
		}
	}    

	/**
	 * Valida campos formlário login
	 * @return boolean, false caso tenha algum problema nos dados digitados nos campos da tela.
	 *                  true caso esteja sem problemas.
	 */
	public boolean validaCamposLogin() {
		EditText edtxt = (EditText) dialogGlobal.findViewById(R.id.edTxtLoginAbertura);
		dadosTela[0] = edtxt.getText().toString();
		if (!Utilitario.isPreenchidoEditText(edtxt)) {
//	        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//	        edtxt.startAnimation(shake);
			Utilitario.alert(R.string.alertTituloErroValidacao, R.string.alertPreencherCampo,R.string.alertBotaoOk, -1, TaxiAndroidActivity.this);
			edtxt.requestFocus();
			return false;
		}
		edtxt = (EditText)  dialogGlobal.findViewById(R.id.edTxtSenhaAbertura);
		dadosTela[1] = edtxt.getText().toString();
		if (!Utilitario.isPreenchidoEditText(edtxt)) {
//	        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//	        edtxt.startAnimation(shake);
			Utilitario.alert(R.string.alertTituloErroValidacao, R.string.alertPreencherCampo,R.string.alertBotaoOk, -1, TaxiAndroidActivity.this);
			edtxt.requestFocus();
			return false;
		}
		return true;
	}

    public void consultaSolicitacaoTaxi(UsuarioBean result) {
    	DataBase db = new DataBase(getApplicationContext());
    	//Busca todos as solicitacoes gravadas na base
    	GlobalTaxi.getGlobalExpress().setSolicitacaoTaxistaMobile(db.selectSolicitacaoTaxistaMobile(-1));
    	db.close();
    	chamaSolicitaTaxiTab();
    }

    public void onRestart(){
        super.onRestart();
        iniciaAplicacao();
    }
    
    public void onDestroy(){
    	super.onDestroy();
    }
    
    /**
     * Faz a conexao com o servidor e consulta Login do cliente
     * @param progress
     */
    private class ConsultaLoginServidorThread extends AsyncTask <Void, Void, String>{
    	private 	ProgressDialog 	dialog;
    	public 		Context 		applicationContext;
    	public 		String[]		dadosTela;
    	private 	DataBase 		dataBase = null;
    	private final String 		loginUrl = "taxiWeb/seam/resource/rest/login/autenticar";
    	private 	UsuarioBean 	usu		 = null; 

    	@Override
    	protected void onPreExecute(){
    		this.dialog = ProgressDialog.show(applicationContext, applicationContext.getString(R.string.aguarde), applicationContext.getString(R.string.conectando), true);
    	}
    	
    	@Override
    	protected String doInBackground(Void... params) {
    		 String resposta = null;
    		try {
    			//Inserindo nome de campos para o HttpPost
    			dadosTela[0] = "login=" + dadosTela[0];
    			dadosTela[1] = "senha=" + dadosTela[1];
    			resposta = Conexao.httpPost(GlobalTaxi.getGlobalExpress().getUrl() + loginUrl, dadosTela);
    			//limpando o String[] com nome do campo.
    			if(resposta != null){
    				dadosTela[0]= dadosTela[0].replace("login=", "");
    			}
    			
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
    		long res = 0;
    		usu = new UsuarioBean();
    		dataBase = GlobalTaxi.getGlobalExpress().getDatabase(applicationContext);
    		if(result != null){
        		usu.setLogin(dadosTela[0]);
        		usu.setSenha(result);
        		usu.setModelo(android.os.Build.MODEL);
        		res = dataBase.insert_usuario(usu);
        		GlobalTaxi.getGlobalExpress().setUsuarioBean(usu);
        		consultaSolicitacaoTaxi(usu);//Chama metodo da classe que iniciou a Thread
    		}
    		dataBase.close();
    		this.dialog.dismiss();
    		if(result != null && res > 0){
    			GlobalTaxi.getGlobalExpress().setStatusCorrida(false);//coloca o taxista como false na corrida, ou seja, está livre
        	}else{
    			Utilitario.alert(R.string.alertTituloErro, R.string.alertLoginSemSucesso, R.string.alertBotaoOk, -1, (Activity)applicationContext);
        	}
    	}
    	
    	public UsuarioBean getUsuarioBean(){
    		return usu;
    	}
    }//end ConsultaLogin
    
}
