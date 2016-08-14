package br.com.taxiandroid;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Atividade responsavel em carregar o WebViewToolKit
 * 
 */
public class WebViewTaxi extends Activity{
	private WebView webView;
    private WebSettings settings;
    private WebView urlLoading;
    
    private String postData = "";
    private String userAgent;

	@Override
	public void onCreate(final Bundle savedInstance){
		super.onCreate(savedInstance);
		carregaWindowView();
	}
	/**
	 * Carrega webview na activity
	 */
	private void carregaWindowView() {
	      CookieSyncManager.createInstance(this);
	      CookieSyncManager.getInstance().startSync();
	      
//	  	  getWindow().requestFeature(Window.FEATURE_PROGRESS);
	  	  setContentView(R.layout.webview);
	  	  
	  	  webView = (WebView)findViewById(R.id.webview1);
	  	  webView.setSaveEnabled(true);
	      
//	      webView.post(URL_AJUDA);

//	  	  String urllocal = ArquivoLocalContentProvider.constructUri(URL_AJUDA);
	      //webView.loadUrl(urllocal);
//	  	  webView.loadDataWithBaseURL(urllocal, null, "text/html", "UTF-8", null);
	  	  webView.loadUrl("http://bytaxi.homedns.org:8080/taxiWeb/cadastroUsuario.seam");
	      
	      //Configuracoes do WebView
	      settings = getSettings();
	      settings.setUseWideViewPort(true);
	      settings.setJavaScriptEnabled(true);
	      settings.setCacheMode(WebSettings.LOAD_NORMAL);
	      settings.setNavDump(true);
	      settings.setSavePassword(false);
	      settings.setUserAgentString(userAgent);
	      settings.setSupportZoom(false);
	      settings.setSupportMultipleWindows(false);
	      webView.requestFocus(View.FOCUS_DOWN);
	   
		}	
	
	public WebSettings getSettings(){
		if(settings == null)
			settings = webView.getSettings();
		return settings;
	}
    
 

	//Mantem a aplicacao ao rotacionar o device
	@Override
	public void onConfigurationChanged(Configuration newConfig){        
	    super.onConfigurationChanged(newConfig);
	}
	
	
	@Override 
	protected void onSaveInstanceState(Bundle outState) { 
		super.onSaveInstanceState(outState);
//		webView.saveState(outState);	
	}	

	@Override
	protected void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//	    	Log.d("", ""+keyCode);
	        webView.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override 
	public void onRestart(){
		super.onRestart();		
	}
	
	@Override 
	public void onPause(){
		super.onPause();		
	}
//	//Usando html nativo
//	public android.os.ParcelFileDescriptor openFile(android.net.Uri uri, java.lang.String mode) throws java.io.FileNotFoundException{
//		uri = URI.create("file:///data/data/com.techjini/files/myImage.jpeg");
//		File file = new File(uri);
//		ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//		return parcel;
//	}
}
