package br.com.taxiandroid.bean;

public class UsuarioBean {
	private String id;
	private String login;
	private String senha;
	private String idDevice;
	private String modelo;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
}
