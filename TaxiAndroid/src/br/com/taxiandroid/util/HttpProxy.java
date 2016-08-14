package br.com.taxiandroid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import br.com.taxiandroid.GlobalTaxi;

public class HttpProxy {

	private static final String TAG= "HttpProxy";

	private final static int HTTP_STATUS_OK = 200;
	private final static int HTTP_STATUS_NOCONTENT = 204;
	private final static int HTTP_STATUS_UNAUTHORIZED = 401;


	/**
	 * Método que executa uma chamada GET
	 * 
	 * @return uma string com o resultado da chamada em formato texto.
	 */
	public static final String httpGet(String uri, HttpClient httpClient) {
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Entrou HttpProxy.httpGet");

		String resultado = null;

		HttpGet requisicao = new HttpGet();
		HttpResponse resposta = null;

		try {
			requisicao.setURI(new URI(uri));
			resposta = httpClient.execute(requisicao);
			resultado = HttpProxy.leRespostaServidor(resposta);
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Retorno da chamada ==>> " + resultado);

		} catch (URISyntaxException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "URISyntaxException" + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "ClientProtocolException" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())	
				Log.e(TAG, "IOException" + e.getMessage());
			e.printStackTrace();
		}
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Executou HttpProxy.httpGet, retornando com resultado.");
		return resultado;
	}

	/**
	 * Método que executa uma chamada POST passando a lista de parametros como objetos de Form para o servidor. Utilizar quando quiser 
	 * passar os atributos como atributos de form. Para passar JSon utilizar o método que sobrecarrega este e passa uma string json no lugar
	 * de uma lista de parâmetros.
	 * 
	 * @return uma string com o resultado da chamada em formato texto.
	 */
	public static final String httpPost(String uri, HttpClient httpClient, List<NameValuePair> parametros) throws IOException{
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Entrou HttpProxy.httpPost");

		String resultado = null;
//		if (AnototudoApp.USER_AGENT == null)
//			throw new IllegalStateException("User agent não recuperado. Não é possível continuar requisição!");

		try {
			HttpPost requisicao = new HttpPost();
			requisicao.setURI(new URI(uri));
//			requisicao.setHeader("User-Agent", AnototudoApp.USER_AGENT);
			requisicao.setHeader("User-Agent", "Android");
			
			HttpResponse resposta = null;

			// passa os parametros para enviar para o servidor e seta na requisicao.
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parametros);
			requisicao.setEntity(formEntity);

			resposta = httpClient.execute(requisicao);
			resultado = HttpProxy.leRespostaServidor(resposta);
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Retorno da chamada ==>> " + resultado);

		} catch (URISyntaxException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())	
				Log.e(TAG, "URISyntaxException: " + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "ClientProtocolException: " + e.getMessage());
			e.printStackTrace();
		}
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Executou HttpProxy.httpPost, retornando com resultado.");
		return resultado;
	}

	/**
	 * Metodo que faz uma chamada post passando uma string JSon no corpo da chamada.
	 * @param uri a URI a ser chamada no servidor do anototudo.
	 * @param httpClient o cliente httpClient que será responsável por estabelecer a conexão com o servidor.
	 * @param jsonString uma string de formato JSon que seja válido para reconstruir um objeto java no servidor.
	 * @return Uma String com o resultado da chamada no servidor em formato JSon válido.
	 * @throws IOException
	 */
	public static final String httpPost(String uri, HttpClient httpClient, String jsonString) throws IOException{
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Entrou HttpProxy.httpPost");

		String resultado = null;
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Fazendo requisição POST no servidor uri ==>> " + uri);
		try{
			
			HttpPost requisicao =  new HttpPost();
			requisicao.setURI(new URI(uri));
			requisicao.setHeader("User-Agent", "Android");
			
			//TODO - A linha abaixo informa que espera uma string em formato json válido como resposta do servidor.
			// este método deve ser evoluído para receber string json como resposta do servidor.
			//requisicao.setHeader("Accept", "application/json");
			
			//informa que ira enviar ao servidor JSon.
			requisicao.setHeader("Content-type", "application/json");
			HttpResponse resposta = null;
			
			//envia o json para o servidor.
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "JSon String logo antes de fazer a requisição ==>> " + jsonString);
			StringEntity sEntity = new StringEntity(jsonString, "UTF-8");
			requisicao.setEntity(sEntity);
			
			resposta = httpClient.execute(requisicao);
			resultado = HttpProxy.leRespostaServidor(resposta);
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Retorno da chamada ==>> " + resultado);

		} catch (URISyntaxException e) {
			Log.e(TAG, "URISyntaxException: " + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException: " + e.getMessage());
			e.printStackTrace();
		}
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Executou HttpProxy.httpPost, retornando com resultado.");
		return resultado;
	}

	/**
	 * Metodo que faz uma chamada put passando uma string JSon no corpo da chamada.
	 * @param uri a URI a ser chamada no servidor do anototudo.
	 * @param httpClient o cliente httpClient que será responsável por estabelecer a conexão com o servidor.
	 * @param jsonString uma string de formato JSon que seja válido para reconstruir um objeto java no servidor.
	 * @return Uma String com o resultado da chamada no servidor em formato JSon válido.
	 * @throws IOException
	 */
	public static final String httpPut(String uri, HttpClient httpClient, String jsonString) throws IOException{
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Entrou HttpProxy.httpPut");

		String resultado = null;
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Fazendo requisição Put no servidor uri ==>> " + uri);
		try{
			
			HttpPut requisicao =  new HttpPut();
			requisicao.setURI(new URI(uri));
			requisicao.setHeader("User-Agent", "Android");
			
			//informa que ira enviar ao servidor JSon.
			requisicao.setHeader("Content-type", "application/json");
			HttpResponse resposta = null;
			
			//envia o json para o servidor.
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "JSon String logo antes de fazer a requisição ==>> " + jsonString);
			StringEntity sEntity = new StringEntity(jsonString, "UTF-8");
			requisicao.setEntity(sEntity);
			
			resposta = httpClient.execute(requisicao);
			resultado = HttpProxy.leRespostaServidor(resposta);
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Retorno da chamada ==>> " + resultado);

		} catch (URISyntaxException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "URISyntaxException: " + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "ClientProtocolException: " + e.getMessage());
			e.printStackTrace();
		}
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Executou HttpProxy.httpPost, retornando com resultado.");
		return resultado;
	}
	
	/**
	 * Metodo que faz uma chamada DELETE passando uma string JSon no corpo da chamada.
	 * @param uri a URI a ser chamada no servidor do anototudo.
	 * @param httpClient o cliente httpClient que será responsável por estabelecer a conexão com o servidor.
	 * @param jsonString uma string de formato JSon que seja válido para reconstruir um objeto java no servidor.
	 * @return Uma String com o resultado da chamada no servidor em formato JSon válido.
	 * @throws IOException
	 */
	public static final String httpDelete(String uri, HttpClient httpClient, String jsonString) throws IOException{
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Entrou HttpProxy.httpDELETE");

		String resultado = null;
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Fazendo requisição DELETE no servidor uri ==>> " + uri);
		try{
			
			HttpDelete requisicao =  new HttpDelete();
			requisicao.setURI(new URI(uri));
			requisicao.setHeader("User-Agent", "Android");
			
			//informa que ira enviar ao servidor JSon.
			requisicao.setHeader("Content-type", "application/json");
			HttpResponse resposta = null;
			
			//envia o json para o servidor.
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "JSon String logo antes de fazer a requisicao ==>> " + jsonString);
			
			resposta = httpClient.execute(requisicao);
			resultado = HttpProxy.leRespostaServidor(resposta);
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Retorno da chamada ==>> " + resultado);

		} catch (URISyntaxException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "URISyntaxException: " + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "ClientProtocolException: " + e.getMessage());
			e.printStackTrace();
		}
		if(GlobalTaxi.getGlobalExpress().isDebug())
			Log.d(TAG, "Executou HttpProxy.httpPost, retornando com resultado.");
		return resultado;
	}
	
	/**
	 * Método que efetivamente transforma a resposta do servidor em uma string para ser processada no device. Checa
	 * também status da resposta do servidor.
	 * Se status code for 200 retorna como plain text a senha gerada para sincronização do device. Se status
	 * code for diferente de 200 retorna null e portanto retorno deve ser verificado.
	 * 
	 * @param resposta
	 *            objeto HttpResponse para tratar a resposta.
	 * @return Uma string com a resposta enviada pelo servidor ou null se o resultado tiver um header 401 ou 204 ou 403.
	 * @throws IOException
	 *             - Se não conseguir processar a resposta do servidor.
	 */
	private static String leRespostaServidor(HttpResponse resposta) throws IOException {
		final StatusLine status = resposta.getStatusLine();
		if (status.getStatusCode() == HTTP_STATUS_UNAUTHORIZED) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Resposta do servidor HTTP 401 - NÃO AUTORIZADO");
			return null;
		} else if (status.getStatusCode() == HTTP_STATUS_NOCONTENT) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Resposta do servidor HTTP 204 - NO CONTENT");
			return null;
		} else if (status.getStatusCode() == 403) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Resposta do servidor HTTP 403 - PROIBIDO");
			return "403";
		}else if (status.getStatusCode() == HTTP_STATUS_OK) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(TAG, "Status retornou ok, usuario existe e senha confere, retornando senha de sincronizacao do device");
			StringBuffer sBuffer = leBuffer(resposta);
			String resultado = sBuffer.toString();
			return resultado;
		}
		else
		{
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "Resposta do servidor inesperada, analisar problema!!! STATUS RETORNADO ==>> " + status.getStatusCode());
			StringBuffer sBuffer = leBuffer(resposta);
			String resultado = sBuffer.toString();
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "Corpo da resposta do servidor com a resposta inesperada ==>> " + resultado);
			return null;
		}
	}

	private static StringBuffer leBuffer(HttpResponse resposta)
			throws IOException {
		BufferedReader bufferResposta = null;
		bufferResposta = new BufferedReader(new InputStreamReader(resposta.getEntity().getContent()));
		StringBuffer sBuffer = new StringBuffer("");
		String linha = "";
		while ((linha = bufferResposta.readLine()) != null) {
			sBuffer.append(linha);
		}
		return sBuffer;
	}

	/**
	 * Garante que o user agent será recuperado para enviar uma requisição válida para o servidor. Este método deve ser
	 * chamado sempre antes de tentar uma conexão com o servidor.
	 * 
	 * @param context
	 */
	public static void recuperaUserAgent(Context context) {
		try {
			// Read package name and version number from manifest
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//			AnototudoApp.USER_AGENT = String.format(context.getString(br.com.anototudo.R.string.user_agent), info.packageName, info.versionName);
			
//			Log.d(TAG, "User agent recuperado do device para ser enviado ao servidor ==>> " + AnototudoApp.USER_AGENT);

		} catch (NameNotFoundException e) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(TAG, "Não encontrou informação do pacote!", e);
		}
	}
}
