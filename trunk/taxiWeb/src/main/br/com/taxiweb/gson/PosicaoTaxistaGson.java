package br.com.taxiweb.gson;

import com.google.gson.Gson;

public class PosicaoTaxistaGson extends DeviceAutenticadoGson
{
	private long idSolicitacao;
	private double latitude;
	private double longitude;
	
	
	public long getIdSolicitacao() {
		return idSolicitacao;
	}


	public void setIdSolicitacao(long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}


	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public static final PosicaoTaxistaGson fromJSON(String json)
	{
		Gson gson = new Gson();
		PosicaoTaxistaGson ptgson = gson.fromJson(json, PosicaoTaxistaGson.class);
		return ptgson;
	}
	
	
	public static final String toJSON(PosicaoTaxistaGson ptgson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(ptgson);
		return result;
	}
}
