package br.com.taxiweb.gson;

import java.util.Date;

import com.google.gson.Gson;

public class UsuarioGson {
	private String nome;
	private String sobreNome;
	private String sexo;
	private String celular;
	private Date dataCadastro;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobreNome() {
		return sobreNome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	public static final UsuarioGson fromJSON(String json)
	{
		Gson gson = new Gson();
		UsuarioGson ug = gson.fromJson(json, UsuarioGson.class);
		return ug;
	}
	
	public static final  String toJSON(UsuarioGson uGson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(uGson);
		return result;
	}

}
