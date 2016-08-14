package br.com.taxiandroid;

import org.json.JSONArray;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TabHost;
import br.com.taxiandroid.bean.SolicitacaoTaxista;
import br.com.taxiandroid.bean.SolicitacaoTaxistaMobile;
import br.com.taxiandroid.bean.UsuarioBean;
import br.com.taxiandroid.service.GPSTaxista;
import br.com.taxiandroid.util.DataBase;
import br.com.taxiweb.gson.SolicitacaoGson;
import br.com.taxiweb.gson.UsuarioGson;

public class GlobalTaxi extends Application {
	private boolean debug							= true;
	
	private static String tag						= "taxi";
	private String idSessao 						= null;
	private String controle 						= null;
	private static GlobalTaxi globalExpress 		= null;
	private Location localizacao 					= null;
	private String latitCliente 					= null;
	private String longiCliente						= null;
	private boolean isTabMap 						= false;
	private LocationManager locationManager 		= null;
	
	private String posicaoFila						= null;
	private String posicaoPL						= "-1";
	private boolean statusCorrida					= false;
	private boolean isLogado						= false;
	
	private SolicitacaoTaxista solicitacaoTaxista 	= null;
	
	//Usados como parametro na conexão com GPS
	private long tempoReconexaoGPS					= 0;
	private float distanciaReconexaoGPS				= 0f;
	public static long TEMPO_RECONEXAO_GPS			= 0;//1000
	public static float DISTANCIA_RECONEXAO_GPS		= 0; //300f
	
	private int filtroTabela 						= 0;//Filtro por PL
	private JSONArray linhasTabGeral				= new JSONArray();
	
	
	private boolean mostraSolicitacaoTeste 			= false;
	private GPSTaxista gpsTaxista					= null;

	private boolean tocaMusicaTrocaPL				= false;
	private String idSolicitacao 					= "0";
	private UsuarioBean usuarioBean					= null;
	private DataBase database						= null;
	private TabHost tabHost							= null;
	private SolicitacaoGson solicitacaoGson = null;
	private UsuarioGson usuarioGson 				= null;
//	private String url								="http://192.168.43.246:8080/";
	private String url								="http://bytaxi.homedns.org:8080/";
//	private String url								="http://10.0.2.2:8080/";//simulador
//	private String url								="http://192.168.1.103/";
	private SolicitacaoTaxistaMobile solicitacaoTaxistaMobile = null;
	private boolean isConsultaSolicitacaoTaxiExecutando = false;	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TabHost getTabHost() {
		return tabHost;
	}

	public void setTabHost(TabHost tabHost) {
		this.tabHost = tabHost;
	}


	public UsuarioGson getUsuarioGson() {
		return usuarioGson;
	}

	public void setUsuarioGson(UsuarioGson usuarioGson) {
		this.usuarioGson = usuarioGson;
	}

	
	public SolicitacaoGson getSolicitacaoGson() {
		return solicitacaoGson;
	}

	public void setSolicitacaoGson(SolicitacaoGson solicitacaoGson) {
		this.solicitacaoGson = solicitacaoGson;
	}

	public DataBase getDatabase(Context context) {
		if(database == null){
			database = new DataBase(context);
		}
		return database;
	}

	public void setDatabase(Context context) {
		database = null;
	}
	
	public UsuarioBean getUsuarioBean() {
		return usuarioBean;
	}

	public void setUsuarioBean(UsuarioBean usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

	public static String getTag() {
		return tag;
	}

	
	public String getIdSolicitacao() {
		return idSolicitacao;
	}
	public void setIdSolicitacao(String idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	public boolean isTocaMusicaTrocaPL() {
		return tocaMusicaTrocaPL;
	}
	public void setTocaMusicaTrocaPL(boolean tocaMusicaTrocaPL) {
		this.tocaMusicaTrocaPL = tocaMusicaTrocaPL;
	}
	public GPSTaxista getGpsTaxista() {
		return gpsTaxista;
	}
	public void setGpsTaxista(GPSTaxista gpsTaxista) {
		this.gpsTaxista = gpsTaxista;
	}
	public boolean isMostraSolicitacaoTeste() {
		return mostraSolicitacaoTeste;
	}
	public void setMostraSolicitacaoTeste(boolean mostraSolicitacaoTeste) {
		this.mostraSolicitacaoTeste = mostraSolicitacaoTeste;
	}

	
	public JSONArray getLinhasTabGeral() {
		return linhasTabGeral;
	}
	public void setLinhasTabGeral(JSONArray linhasTabGeral) {
		this.linhasTabGeral = linhasTabGeral;
	}
	

	public int getFiltroTabela() {
		return filtroTabela;
	}
	public void setFiltroTabela(int filtroTabela) {
		this.filtroTabela = filtroTabela;
	}

	boolean teste 							= true;
	
	public long getTempoReconexaoGPS() {
		return tempoReconexaoGPS;
	}
	public void setTempoReconexaoGPS(long tempoReconexaoGPS) {
		this.tempoReconexaoGPS = tempoReconexaoGPS;
	}
	public float getDistanciaReconexaoGPS() {
		return distanciaReconexaoGPS;
	}
	public void setDistanciaReconexaoGPS(float distanciaReconexaoGPS) {
		this.distanciaReconexaoGPS = distanciaReconexaoGPS;
	}
	public SolicitacaoTaxista getSolicitacaoTaxista() {
		return solicitacaoTaxista;
	}
	public void setSolicitacaoTaxista(SolicitacaoTaxista solicitacaoTaxista) {
		this.solicitacaoTaxista = solicitacaoTaxista;
	}
	public String getPosicaoFila() {
		return posicaoFila;
	}
	public void setPosicaoFila(String posicaoFila) {
		this.posicaoFila = posicaoFila;
	}
	public String getPosicaoPL() {
		return posicaoPL;
	}
	public void setPosicaoPL(String posicaoPL) {
		this.posicaoPL = posicaoPL;
	}
	public boolean getStatusCorrida() {
		return statusCorrida;
	}
	public void setStatusCorrida(boolean statusCorrida) {
		this.statusCorrida = statusCorrida;
	}

	public LocationManager getLocationManager(LocationListener act) {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GlobalTaxi.getGlobalExpress().getTempoReconexaoGPS(), GlobalTaxi.getGlobalExpress().getDistanciaReconexaoGPS(), act);
		return lm; 
	}
	
	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}
	public boolean isTabMap() {
		return isTabMap;
	}
	public void setTabMap(boolean isTabMap) {
		this.isTabMap = isTabMap;
	}
	public String getLatitCliente() {
		return latitCliente;
	}
	public void setLatitCliente(String latitCliente) {
		this.latitCliente = latitCliente;
	}
	public String getLongiCliente() {
		return longiCliente;
	}
	public void setLongiCliente(String longiCliente) {
		this.longiCliente = longiCliente;
	}
	
	public Location getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(Location localizacao) {
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d("localizacao",""+localizacao.getLatitude()+ " "+localizacao.getLongitude());
		this.localizacao = localizacao;
	}
	public String getIdSessao() {
		return idSessao;
	}
	public void setIdSessao(String idSessao) {
		this.idSessao = idSessao;
	}
	public String getControle() {
		return controle;
	}
	public void setControle(String controle) {
		this.controle = controle;
	}

	public static GlobalTaxi getGlobalExpress(){
		if(globalExpress == null){
			globalExpress = new GlobalTaxi();
		}
		return globalExpress;
	}
	
	public LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean isLogado() {
		return isLogado;
	}

	public void setLogado(boolean isLogado) {
		this.isLogado = isLogado;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setSolicitacaoTaxistaMobile(SolicitacaoTaxistaMobile solicitacaoTaxistaMobile) {
		this.solicitacaoTaxistaMobile = solicitacaoTaxistaMobile;
	}

	public SolicitacaoTaxistaMobile getSolicitacaoTaxistaMobile() {
		return solicitacaoTaxistaMobile;
	}

	public void setConsultaSolicitacaoTaxiExecutando(
			boolean isConsultaSolicitacaoTaxiExecutando) {
		this.isConsultaSolicitacaoTaxiExecutando = isConsultaSolicitacaoTaxiExecutando;
	}

	public boolean isConsultaSolicitacaoTaxiExecutando() {
		return isConsultaSolicitacaoTaxiExecutando;
	}
}
