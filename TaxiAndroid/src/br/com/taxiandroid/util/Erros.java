package br.com.taxiandroid.util;

public class Erros {
	private final static int _403 = 403;
	private final static int _409 = 400;
	private final static int _200 = 200;
	
	
	public static String getMensagemErro(int codigoErro){
		String erro = "Favor tente mais tarde!!!";
		switch(codigoErro){
		case _403:
			erro =  "Dados do device n‹o autenticarado com sucesso no servidor";
			break;
		case _409:
			erro =  "Dados do device n‹o autenticarado com sucesso no servidor";
			break;
		case _200:
			erro =  "Dados do device n‹o autenticarado com sucesso no servidor";
			break;
		}
		
		return erro;
	}
}
