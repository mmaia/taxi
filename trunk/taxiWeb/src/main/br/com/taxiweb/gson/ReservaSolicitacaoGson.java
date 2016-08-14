package br.com.taxiweb.gson;

import com.google.gson.Gson;

public class ReservaSolicitacaoGson extends DeviceAutenticadoGson{
	
	private long idSolicitacao;

	public long getIdSolicitacao() {
		return idSolicitacao;
	}
	
	public void setIdSolicitacao(long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	
	public static final ReservaSolicitacaoGson fromJSON(String json)
	{
		Gson gson = new Gson();
		ReservaSolicitacaoGson rsg = gson.fromJson(json, ReservaSolicitacaoGson.class);
		return rsg;
	}
	
	public static final  String toJSON(ReservaSolicitacaoGson rsGson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(rsGson);
		return result;
	}
}
