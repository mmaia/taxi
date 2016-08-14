package br.com.taxiandroid.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import android.util.Log;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiweb.gson.DeviceAutenticadoGson;


public class Conexao {
	private URLConnection 		connection 		= null;
	private HttpURLConnection	httpConnection 	= null;
	private URL 				url 			= null;
	public static String 		cookie 			= null;
	
	public Conexao(String urlInternet,String _cookie){
		this(urlInternet);
		cookie = _cookie; 
	}
	
	public Conexao(String urlInternet){
		try{
			//Usado na leitura do response
			url = new URL(urlInternet);
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d("URL ", urlInternet);
			connection = url.openConnection();
			connection.setAllowUserInteraction(true);
			httpConnection = (HttpURLConnection)connection;
		}catch (MalformedURLException e) { 
			Log.i("Erro ao conectar com a Servlet ",e.getMessage());
		} catch (IOException e) {
			Log.i("Erro ao conectar com a Servlet ",e.getMessage());
		}	
	}

	/**
	 * M√©todo que executa uma chamada POST passando a lista de parametros como objetos de Form para o servidor. Utilizar quando quiser 
	 * passar os atributos como atributos de form. Para passar JSon utilizar o m√©todo que sobrecarrega este e passa uma string json no lugar
	 * de uma lista de par√¢metros.
	 * 
	 * @return uma string com o resultado da chamada em formato texto.
	 */
	public static final String httpPost(String uri, String dados[]) throws IOException{
		String resultado = null;

		try {
			HttpPost requisicao = new HttpPost();
			requisicao.setURI(new URI(uri));

			HttpResponse resposta = null;

			// passa os parametros para enviar para o servidor e seta na requisicao.
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(montaDadosPost(dados));
			requisicao.setEntity(formEntity);

			resposta = new DefaultHttpClient().execute(requisicao);
			resultado = leRespostaServidor(resposta);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	private static StringBuffer leBuffer(HttpResponse resposta)	throws IOException {
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
	 * Método que efetivamente transforma a resposta do servidor em uma string para ser processada no device. Checa
	 * também status da resposta do servidor.
	 * Se status code for 200 retorna como plain text a senha gerada para sincroniza��o do device. Se status
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
		if (status.getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(GlobalTaxi.getTag(), "Resposta do servidor HTTP 401 - NÃO AUTORIZADO");
			return null;
		} else if (status.getStatusCode() == HttpURLConnection.HTTP_NOT_ACCEPTABLE) {
			if(GlobalTaxi.getGlobalExpress().isDebug())//tipo de parametro put/post/get errado
				Log.d(GlobalTaxi.getTag(), "Resposta do servidor HTTP 405 - NO CONTENT");
			return null;
		}else if (status.getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(GlobalTaxi.getTag(), "Resposta do servidor HTTP 204 - NO CONTENT");
			return null;
		} else if (status.getStatusCode() == HttpURLConnection.HTTP_OK) {
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.d(GlobalTaxi.getTag(), "Status retornou ok, usuario existe e senha confere, retornando senha de sincronizacao do device");
			StringBuffer sBuffer = leBuffer(resposta);
			String resultado = sBuffer.toString();
			return resultado;
		}
		else
		{
			if(GlobalTaxi.getGlobalExpress().isDebug())	
				Log.e(GlobalTaxi.getTag(), "Resposta do servidor inesperada, analisar problema!!! STATUS RETORNADO ==>> " + status.getStatusCode());
			StringBuffer sBuffer = leBuffer(resposta);
			String resultado = sBuffer.toString();
			if(GlobalTaxi.getGlobalExpress().isDebug())
				Log.e(GlobalTaxi.getTag(), "Corpo da resposta do servidor com a resposta inesperada ==>> " + resultado);
			return null;
		}
	}
	
	
	/**
	 * Envia os dados via Post para a url passada por parametro
	 * se o returno for 0 deu Exception
	 * @param urlInternet
	 * @return
	 */
	public int enviaDadosPost(String dados[]){
		int responseCode = 0; 
		try {
			StringBuffer dataB = new StringBuffer();
			if(dados != null){
				for (int i = 0; i < dados.length && dados[i] != null; i++) {
					dataB.append(URLEncoder.encode(dados[i]));
					if(i<dados.length-1){
						dataB.append("&");
					}
				}
			}
			
			//Envio de parametros
			httpConnection.setDoOutput(true); 
			httpConnection.setRequestProperty("METHOD", "POST");

			if(cookie != null)
				httpConnection.setRequestProperty("cookie", cookie);
			
			OutputStreamWriter wr = new OutputStreamWriter(httpConnection.getOutputStream()); 
			wr.write(dataB.toString()); 
			wr.flush(); 
			responseCode = httpConnection.getResponseCode(); 
		} catch (Exception e) {
			Log.i("", e.getMessage());
			return responseCode;
		}
		return responseCode;
	}
	/**
	 * Retorna um String[ ]
	 * String[0] - Código de Retorno HTTP
	 * JSONArray[1] - Retorno obtivo pelo servidor WEB.
	 * @return
	 */
	public Object[] getResposta(String [] dados){
		Object resposta[] = {"Erro", "Erro na conexão tente mais tarde."};
		int numResp = 0;
		if((numResp = enviaDadosPost(dados)) == HttpURLConnection.HTTP_OK){
	        try {
				InputStream is = connection.getInputStream();
		        BufferedInputStream bis = new BufferedInputStream(is);
		        ByteArrayBuffer baf = new ByteArrayBuffer(50);
		        int current = 0;
					while((current = bis.read()) != -1){
					    baf.append((byte)current);
					}
				String rep = new String(baf.toByteArray());
				Log.d("resposta HTTP", rep);
				JSONObject  jsonResp= new JSONObject(rep);
		        
		        // Capturando as respostas do JSON
                JSONArray arrayNomes=jsonResp.names();
                JSONArray arrayValores=jsonResp.toJSONArray(arrayNomes);
				resposta[0] = arrayNomes.getString(0);	

                
                HashMap<String, String> respostaMap = new HashMap<String, String>();
                for(int i=0;i<arrayValores.length();i++)
                {
                	respostaMap.put(arrayNomes.getString(i), arrayValores.getString(i));
                }
                resposta[1] = respostaMap;
			} catch (IOException e) {
				resposta[0] = "Erro";				
				e.printStackTrace();
			} 
			catch (JSONException e) {
				e.printStackTrace();
				resposta[0] = "Erro";				
			}
		}
		return resposta;
	}
	
	public static JSONObject conecta(String urlInternet, String [] dadosPost, int i){
		JSONObject rep = null;

//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setDoOutput(true);
//		connection.setRequestMethod("POST");
////		connection.setRequestProperty("Cookie", "userid=XXXX; password=XXXX");
//		connection.setRequestProperty("Cookie", dadosPost[0]+";"+ dadosPost[1]);
//		connection.connect();
		
		return null;
		
	}
	public static JSONObject conecta(String urlInternet, String [] dadosPost){
		return conecta(urlInternet, dadosPost, null);
	}
	
	public static JSONObject conecta(String urlInternet, String [] dadosPost, StringEntity jSon){
		JSONObject rep = null;
	    try 
	    {
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        HttpPost httpPost = new HttpPost(urlInternet);
//	        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	        if(cookie != null)
	        	httpPost.setHeader("Cookie",cookie);

	        if(dadosPost== null && dadosPost.length > 0){
//	        if(jSon != null){
	        		httpPost.setEntity(jSon);
	        		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
	        }else{
	        	httpPost.setEntity(new UrlEncodedFormEntity(montaDadosPost(dadosPost), HTTP.UTF_8));	        	
	        }
	        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
	        HttpResponse response = httpclient.execute(httpPost);
	        HttpEntity entity = response.getEntity();
	        try
			{
	        	InputStream is = entity.getContent();
		        BufferedInputStream bis = new BufferedInputStream(is);
		        ByteArrayBuffer baf = new ByteArrayBuffer(50);
		        int current = 0;
					while((current = bis.read()) != -1){
					    baf.append((byte)current);
					}
					String resp = new String(baf.toByteArray());
					if(GlobalTaxi.getGlobalExpress().isDebug())
						Log.d("resp", ""+resp);
					if(resp != null && resp.length() > 0){
						rep = new JSONObject(resp);
						if(GlobalTaxi.getGlobalExpress().isDebug())
							Log.d("passou no http length", ""+rep.length());
					}
					if(GlobalTaxi.getGlobalExpress().isDebug())
						Log.d("",""+rep);
			}
	        catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
	    return rep;
	}
	
	
	public static JSONObject conectaPost(String urlInternet, String [] dadosPost, String jSon){
		JSONObject rep = null;
	    try 
	    {
	    	
    	 	HttpClient httpclient = new DefaultHttpClient();
    	    HttpPost request = new HttpPost(urlInternet);
    	    HttpEntity entity;
    	    
//    		StringEntity se = null;
//			try {
//				se = new StringEntity(jSon);
//				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//    	    entity = se;
    	    HttpParams params = new BasicHttpParams(); 
    	    params.setParameter("json", jSon.toString());
    	    request.setParams(params);
    	    request.setHeader(HTTP.CONTENT_TYPE, "application/json");
    	    HttpResponse response;
    	    response = httpclient.execute(request);
	    	
	        entity = response.getEntity();
	        
	        try
			{
	        	InputStream is = entity.getContent();
		        BufferedInputStream bis = new BufferedInputStream(is);
		        ByteArrayBuffer baf = new ByteArrayBuffer(50);
		        int current = 0;
					while((current = bis.read()) != -1){
					    baf.append((byte)current);
					}
					String resp = new String(baf.toByteArray());
					if(GlobalTaxi.getGlobalExpress().isDebug())
						Log.d("resp", ""+resp);
					if(resp != null && resp.length() > 0 && resp.indexOf("body") < 0){
						rep = new JSONObject(resp);
						if(GlobalTaxi.getGlobalExpress().isDebug())
							Log.d("passou no http length", ""+rep.length());
					}else{
						JSONObject jo = new JSONObject();
						String codigoErro = resp.substring(resp.indexOf("HTTP Status")+12, resp.indexOf("HTTP Status")+16); 
						jo.put("Erro", codigoErro);
						jo.put("Mensagem", Erros.getMensagemErro(Integer.parseInt(codigoErro)));
						
						return jo;
					}
					if(GlobalTaxi.getGlobalExpress().isDebug())
						Log.d("",""+rep);
			}
	        catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
	    return rep;
	}	

	/**
	 * Retorna lista de parametros que serao enviados via POST
	 * @param urlInternet
	 * @return
	 */
	private static List<NameValuePair> montaDadosPost(String dados[]){
        List <NameValuePair> listaParam = null;
		try {
			if(dados != null){
				listaParam = new ArrayList <NameValuePair>();
				for (int i = 0; i < dados.length && dados[i] != null; i++) {
					String [] campoValor = dados[i].split("=");
					listaParam.add(new BasicNameValuePair(campoValor[0], campoValor[1]));
				}
			}
		} catch (Exception e) {
			Log.i("", e.getMessage());
		}
		return listaParam;
	}	
}