package br.com.taxiandroid.bean;

public class SolicitaTaxiBean {
	public SolicitaTaxiBean(){
		
	}
	
	public SolicitaTaxiBean(String dados){
		String info[] = dados.split("@");
		if(info.length > 2){
			setEndereco(info[0]);
			setAdicional(info[1]);
			if(!info[2].trim().equalsIgnoreCase(""))
				setNumPassageiros(new Integer(info[2]).intValue());
			setDestino(info[3]);
			setLat(info[4]);
			setLog(info[5]);
		}else{
			setEndereco("");
			setAdicional("");
			setNumPassageiros(0);
			setDestino("");
			setLat("");
			setLog("");
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getAdicional() {
		return adicional;
	}
	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}
	public int getNumPassageiros() {
		return numPassageiros;
	}
	public void setNumPassageiros(int numPassageiros) {
		this.numPassageiros = numPassageiros;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	private int id;
	private String endereco;
	private String data;
	private String adicional;
	private int numPassageiros;
	private String destino;
	private String lat;
	private String log;
	private String bairro;
}
