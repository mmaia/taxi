package br.com.taxiandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.com.taxiandroid.bean.ControleFinancas;
import br.com.taxiandroid.bean.TabelaControleFinanca;
import br.com.taxiandroid.util.Conexao;

public class TabFinancas extends Activity{
	public static int 		menuTotalMes 			= 6;
	public static int 		menuTotalSemana 		= 7;
	private ControleFinancas cf = null;
	private List<TableLayout> linhasAdded = new ArrayList<TableLayout>();
	private Bundle mySavedInstanceState 				= null;
	private ProgressDialog dialogTabFinancas				=null;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_financas);
        mySavedInstanceState = savedInstanceState;
		fazConexao();
    }
	
	public void fazConexao(){
		dialogTabFinancas = ProgressDialog.show(this, "Aguarde...", "Calculando corridas", true);
		AtualizaLocalizacaoTaxista alt = new AtualizaLocalizacaoTaxista();
		alt.execute();
		dialogTabFinancas.dismiss();
	}
	public void montaTela(List dados) {
		if(cf != null){ 
			TextView totalValorDiaCorrida = (TextView)findViewById(R.id.txtTotalDoDia);
			totalValorDiaCorrida.setText(cf.getValorCorridaDia());
			TextView qtnCorridaDia = (TextView)findViewById(R.id.txtQntCorridaDia);
			qtnCorridaDia.setText(cf.getQntCorridaDia());
//			TextView valorCooperativa = (TextView)findViewById(R.id.txtValorCooper);
//			valorCooperativa.setText(cf.getValorTotalCooperativa());
			TextView data = (TextView)findViewById(R.id.txtDataFinancas);
			data.setText(cf.getDataAtual());
			cf = null;
			carregaTabela(dados);
		}
    }
	
	@Override
	public void onResume(){
		super.onResume();
		fazConexao();
	}

	/**
	 * Cria menus na aplica��o
	 */
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
	    	MenuItem item1 = menu.add(0, menuTotalMes, 0, "Controle Mensal");
//	    	item1.setIcon(R.drawable.sugestao_endereco);
	    	
	    	MenuItem item2 = menu.add(0, menuTotalSemana, 0, "Controle Semanal");
//	    	item2.setIcon(R.drawable.segue_taxista);
	    	return true;
	    }
	
	/**
	/* A��o menu
	 * 
	 */
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
		super.onOptionsItemSelected(item);
			switch (item.getItemId()) {
				case (6):{
//					limpaTela();
//					GlobalExpress.getGlobalExpress().setPosicaoPL("0");
					return true;
				}
				case (7):{
//					GlobalExpress.getGlobalExpress().setMostraSolicitacaoTeste(!GlobalExpress.getGlobalExpress().isMostraSolicitacaoTeste());
					return true;
				}
				default:{
				}
			}
		return false;
	}
	
	
	/**
	 * Busca Finan�as do taxista
	 */
	private class AtualizaLocalizacaoTaxista extends AsyncTask <Void, Void, JSONObject>{
	    	protected Context applicationContext;
	    	private Location locationInner;

	    	@Override
	    	protected void onPreExecute(){
	    	}
	    	
			@Override
			protected JSONObject doInBackground(Void... params) {
	    		JSONObject respostaJson = null;
	    		String idSessao = "";
		    	String dadosTela [] = {"taxistaID=100", "periodo=24"};
		    	if(GlobalTaxi.getGlobalExpress().isDebug())
		    		Log.d("", "taxistaID=100 periodo=24");
//		    	respostaJson = Conexao.conecta(URL_financas, dadosTela);

				return respostaJson;
			}
			
			@Override
			protected void onPostExecute(JSONObject result){
				try {
					if(result != null){
						if(GlobalTaxi.getGlobalExpress().isDebug())
							Log.d("", ""+result);
						JSONObject infoTabelaFinancas = null;
						if(result.has("infoTabelaFinancas")){
							List dados = null;
							cf = new ControleFinancas();
							if(result.has("infoTabelaFinancas")){
								infoTabelaFinancas = (JSONObject)result.getJSONObject("infoTabelaFinancas");
								cf.setQntCorridaDia(infoTabelaFinancas.getString("totalCorridas"));
								cf.setValorCorridaDia(infoTabelaFinancas.getString("totalDinheiro"));
								cf.setDataAtual(infoTabelaFinancas.getString("dataDeHoje"));
							}
							if(infoTabelaFinancas.has("lista")){
								JSONArray linhas = infoTabelaFinancas.getJSONArray("lista");
								dados = new ArrayList();
									if(linhas.length() > 0){
										for (int i = 0; i < linhas.length(); i++) {
											TabelaControleFinanca tcf = new TabelaControleFinanca();
												tcf.setHoraInicio(linhas.getJSONObject(0).getString("inicio"));
												tcf.setHoraFim(linhas.getJSONObject(0).getString("fim"));
												tcf.setValorCorrida(linhas.getJSONObject(0).getString("valor"));
												dados.add(tcf);
										}
									}
									linhas = null;
								}
							montaTela(dados);
							}	
							infoTabelaFinancas = null;
						}
	//						System.gc();
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }		
	}
	
	private void carregaTabela(List dados) {
		TableLayout table = (TableLayout)findViewById(R.id.tblFinancas);
		LayoutInflater inflater = getLayoutInflater();
		Iterator<TabelaControleFinanca> iterat = dados.listIterator();
		while (iterat.hasNext()) {
			TabelaControleFinanca tabelaControleFinanca = (TabelaControleFinanca) iterat.next();

		    TableRow row = (TableRow)inflater.inflate(R.layout.tablerow, table, false);
		    View v = (View)inflater.inflate(R.layout.textview, row, false);
		    ((TextView)v).setText(tabelaControleFinanca.getHoraInicio());
		    row.setBaselineAligned(true);
		    row.addView(v,0);
		    
		    View v1 = (View)inflater.inflate(R.layout.textview, row, false);
		    ((TextView)v1).setText(tabelaControleFinanca.getHoraFim());
		    row.setBaselineAligned(true);
		    row.addView(v1,1);
		    
		    View v2 = (View)inflater.inflate(R.layout.textview, row, false);
		    ((TextView)v2).setText(tabelaControleFinanca.getValorCorrida());
		    row.setBaselineAligned(true);
		    row.addView(v2, 2);
		    
		    table.addView(row);
		    linhasAdded.add(table);
		}
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d("tab financeira","----------");
	}	
	
}
