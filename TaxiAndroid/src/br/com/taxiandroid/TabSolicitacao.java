package br.com.taxiandroid;

import android.app.Activity;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.gson.UsuarioGson;

public class TabSolicitacao extends Activity{
	private final static Handler mHandler = new Handler();
	public static int 		menuLimpaSolicitacao 	= 3;
	public static int 		menuMostraSolicitacao 	= 4;
	private TextView nomeCliente = null;
	private TextView telefone = null;
	private TextView complemento = null;
	private TextView origem = null;
	private TextView qntPassageiros = null;

	
	// Create runnable for posting
    public final Runnable mUpdateResults = new Runnable() {
        public void run() {
        	updateResultadosNaTela();
        }
    };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_solicitacao);

        UsuarioGson usg = GlobalTaxi.getGlobalExpress().getUsuarioGson();
		SolicitacaoGson rsg = GlobalTaxi.getGlobalExpress().getSolicitacaoGson();
		if(usg != null){
			mHandler.post(mUpdateResults);
		}else{
			limpaTela();
		}
    }

	public void updateResultadosNaTela() {
		UsuarioGson usg = GlobalTaxi.getGlobalExpress().getUsuarioGson();
		SolicitacaoGson rsg = GlobalTaxi.getGlobalExpress().getSolicitacaoGson();
		if(usg != null){
		 	nomeCliente = (TextView)findViewById(R.id.txtNomeCliente);
			nomeCliente.setText(usg.getNome());
			telefone = (TextView)findViewById(R.id.txtTelefone);
			telefone.setText(usg.getCelular());
			complemento = (TextView)findViewById(R.id.txtComplemento);
			complemento.setText(rsg.getInformacoesAdicionais());
			origem = (TextView)findViewById(R.id.txtOrigem);
			origem.setText(rsg.getOrigem());
			qntPassageiros = (TextView)findViewById(R.id.txtQntPassageiros);
			qntPassageiros.setText(""+rsg.getNumeroPassageiros());
		}else{
			limpaTela();
		}
    }
	
	@Override
	public void onResume(){
		super.onResume();
		if(GlobalTaxi.getGlobalExpress().getSolicitacaoTaxista() != null){
			mHandler.post(mUpdateResults);
		}
	}
	private void limpaTela(){
		if(nomeCliente != null){
			nomeCliente.setText("");
			telefone.setText("");
			complemento.setText("");
			origem.setText("");
			qntPassageiros.setText("");
		}
	}
	
	/**
	 * Cria menus na aplicação
	 */
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
//	    	MenuItem item1 = menu.add(0, menuLimpaSolicitacao, 0, "Limpar Solicita��o");
//	    	item1.setIcon(R.drawable.sugestao_endereco);
//	    	
//	    	MenuItem item2 = menu.add(0, menuMostraSolicitacao, 0, "Mostra Solicita��o");
////	    	item2.setIcon(R.drawable.segue_taxista);
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
				case (3):{
					limpaTela();
					GlobalTaxi.getGlobalExpress().setPosicaoPL("0");
					return true;
				}
				case (4):{
					GlobalTaxi.getGlobalExpress().setMostraSolicitacaoTeste(!GlobalTaxi.getGlobalExpress().isMostraSolicitacaoTeste());
					return true;
				}
				default:{
				}
			}
		return false;
	}
}
