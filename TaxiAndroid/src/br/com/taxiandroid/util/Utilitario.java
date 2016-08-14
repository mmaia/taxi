package br.com.taxiandroid.util;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import br.com.taxiandroid.GlobalTaxi;
import br.com.taxiandroid.R;
import br.com.taxiandroid.TaxiAndroidActivity;


public class Utilitario {
	public static String CONTROLE_ID = "idSessao";

	public static boolean isPreenchidoEditText(EditText testEditText){
	 	if(testEditText.getText().toString().trim().length() < 1){
	 		return false;
	 	}
		return true;
	}
	public static boolean isEmailValido(EditText testEditText){
	 	if(testEditText.getText().equals("") || 
	 			testEditText.getText().toString().indexOf("@") < 0 ||
	 			testEditText.getText().toString().indexOf(".") < 0){
	 		return false;
	 	}
		return true;
	}
	
	public static void alert(int titulo, int mensagem, int labelButtonPositivo, int labelButtonNegativo, Activity suaClasse){
    	AlertDialog.Builder  alert = new AlertDialog.Builder(suaClasse);
    	alert.setMessage(mensagem);
    	chamaAlert(alert, titulo, labelButtonPositivo, labelButtonNegativo);
    }

	public static void alert(int titulo, String mensagem, int labelButtonPositivo, int labelButtonNegativo, Activity suaClasse){
    	AlertDialog.Builder  alert = new AlertDialog.Builder(suaClasse);
    	alert.setMessage(mensagem);
    	chamaAlert(alert, titulo, labelButtonPositivo, labelButtonNegativo);
    }

	private static void chamaAlert(Builder alert, int titulo, int labelButtonPositivo, int labelButtonNegativo) {
    	alert.setTitle(titulo);
    	if(labelButtonPositivo > 0)
	    	alert.setPositiveButton(labelButtonPositivo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
    	if(labelButtonNegativo > 0)
	    	alert.setNegativeButton(labelButtonPositivo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
    	alert.show();		
	}
	//Caso seja ok a resposta vinda do servidor
	public static boolean isRepostaOk(int respostaHTTP) {
    	return respostaHTTP == HttpURLConnection.HTTP_OK;
	}
	public String splitExpress(String caracter, String linha){
		String texto = linha;
		do{
			texto.substring(0, texto.indexOf(caracter));
		}while(texto.indexOf(caracter) > -1);
		
		return null;
	}
	
	/**
	 * M�todo para recupera��o da data e hora do sistema 
	 * @return YYYY/MM/DD hh:mm:ss
	 */
	public static String getDataHora() {
		//recupera data e hora atual do sistema
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String ano = String.valueOf(cal.get(Calendar.YEAR));
		String horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String minutos = String.valueOf(cal.get(Calendar.MINUTE));
		String segundos = String.valueOf(cal.get(Calendar.SECOND));
		//
		cal = null;
		date = null;
		//
		//formata a data de modo que o tamanho do resultado seja sempre fixo
		//dia
		if (dia.length() < 2) { dia = "0" + dia; } //mes
		if (mes.length() < 2) { mes = "0" + mes; } //horas
		if (horas.length() < 2) { horas = "0" + horas; } //minutos
		if (minutos.length() < 2) { minutos = "0" + minutos; } //segundos
		if (segundos.length() < 2) { segundos = "0" + segundos; } //

		return ano + "-" + mes + "-" + dia + " " + horas + ":" + minutos + ":" + segundos;
	}
	
	/**
	 * Transforma uma String para um objeto Date
	 * @param data
	 * @param padrao
	 * @return
	 */
	public static Date transformaStringData(String data, String padrao){
//		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat curFormater = new SimpleDateFormat(padrao);
		Date dateObj = null;
		try {
			dateObj = (Date)curFormater.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return dateObj;
	}

	/**
	 * Transforma um Date para String 
	 * @param dtData
	 * @param padrao
	 * @return
	 */
	public static String transformaDataString(java.util.Date dtData, String padrao){  
	       SimpleDateFormat formatBra = new SimpleDateFormat(padrao);
//	       try {  
//	          java.util.Date newData = formatBra.parse(dtData.toString());  
	          return (formatBra.format(dtData));  
//	       } catch (ParseException Ex) {  
//	          return null;  
//	       }  
	    }  	

	/**
     * Notifica o status da aplicação
     */
	public static void verificandoNotification(Context contexto) {
		NotificationManager notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = null;
		if(GlobalTaxi.getGlobalExpress().getUsuarioBean() != null){
			notification =  new Notification(R.drawable.icon_notification, contexto.getString(R.string.app_name), System.currentTimeMillis());
		}else{
			notification =  new Notification(R.drawable.icon_notification_no, contexto.getString(R.string.app_name), System.currentTimeMillis());
		}
		Intent it = new Intent(contexto, TaxiAndroidActivity.class);
		it.setClass(contexto, TaxiAndroidActivity.class);
//		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		it.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		
		PendingIntent contentIntent = PendingIntent.getActivity(contexto, R.string.app_name, it, Notification.FLAG_NO_CLEAR);        
	    notification.flags = Notification.DEFAULT_ALL | Notification.FLAG_NO_CLEAR; 
	    notification.setLatestEventInfo(contexto, contexto.getString(R.string.app_name), "",contentIntent);
	    notificationManager.notify(R.string.app_name, notification);		
	}

	
//	/**
//	 * Retorna um array de String filtrado pelo domínio do email.
//	 * @param context
//	 * @param dominioEmail
//	 * @return
//	 */
//	public static String[] getUsername(Context context, String dominioEmail){
//		String emailsString = getUsername(context);
//		String listaDados[] =  null;
//		Hashtable<String, String> listaUs = new Hashtable<String, String>();
//		
//		String usuarioEmail[] =  null;
//		if(emailsString != null){
//			listaDados=emailsString.split("#");
//			for(int i =0; i < listaDados.length;i++){
//				if(listaDados[i].indexOf(dominioEmail) > -1){
//					listaUs.put(listaDados[i], listaDados[i]);
//				}
//			}
//			usuarioEmail = new String[listaUs.size()];
//			int contUsu = 0;
//			Enumeration<String> emailUs = listaUs.elements();
//			while(emailUs.hasMoreElements()){
//				usuarioEmail[contUsu]=(String)emailUs.nextElement();
//				contUsu += 1;
//			}
//			return usuarioEmail;
//		}
//		return null;
//	}

	public static String[] getUsername(Context context){
	    AccountManager manager = AccountManager.get(context); 
	    //Account[] accounts = manager.getAccountsByType("com.google");
	    Account[] accounts = manager.getAccounts();
	    List<String> possibleEmails = new LinkedList<String>();
	    StringBuffer emails = new StringBuffer();

	    for (Account account : accounts) {
	      // Check possibleEmail against an email regex or treat
	      // account.name as an email address only for certain account.type values.
	    	if(account.name.indexOf("@") > -1){
		      possibleEmails.add(account.name);
		      emails.append(account.name);
		      emails.append("#");
	    	}
	    }
        return emails.toString().split("#");	    
	}
	
}
