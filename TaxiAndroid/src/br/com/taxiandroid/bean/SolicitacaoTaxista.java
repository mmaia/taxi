package br.com.taxiandroid.bean;

public class SolicitacaoTaxista {
	
	private String nomeCliente;
	private String telefone;
	private String complemento;
	private String endereco;
	private String latitudeCliente;
	private String longitudeCliente;

	public String getLatitudeCliente() {
		return latitudeCliente;
	}
	public void setLatitudeCliente(String latitudeCliente) {
		this.latitudeCliente = latitudeCliente;
	}
	public String getLongitudeCliente() {
		return longitudeCliente;
	}
	public void setLongitudeCliente(String longitudeCliente) {
		this.longitudeCliente = longitudeCliente;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
}
