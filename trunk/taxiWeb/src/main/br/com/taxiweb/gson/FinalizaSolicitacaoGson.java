package br.com.taxiweb.gson;

import com.google.gson.Gson;

public class FinalizaSolicitacaoGson extends DeviceAutenticadoGson{
	
	private long idSolicitacao;

	public long getIdSolicitacao() {
		return idSolicitacao;
	}
	
	public void setIdSolicitacao(long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	
	public static final FinalizaSolicitacaoGson fromJSON(String json)
	{
		Gson gson = new Gson();
		FinalizaSolicitacaoGson rsg = gson.fromJson(json, FinalizaSolicitacaoGson.class);
		return rsg;
	}
	
	public static final  String toJSON(FinalizaSolicitacaoGson rsGson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(rsGson);
		return result;
	}
}
