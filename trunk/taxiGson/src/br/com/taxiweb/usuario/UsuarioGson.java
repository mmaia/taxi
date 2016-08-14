package br.com.taxiweb.usuario;

import java.util.Date;

public class UsuarioGson {
	private String nome;
	private String sobreNome;
	private String sexo;
	private EnderecoGson endereco;
	private String email;
	private String telefone1;
	private String celular;
	private String senha;
	
	private TaxistaGson taxista;
	private boolean ehTaxista = false;
	
	private boolean enabled = true;
	
	private Date dataCadastro;
	
	private String senhaDevice;

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

	public EnderecoGson getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoGson endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TaxistaGson getTaxista() {
		return taxista;
	}

	public void setTaxista(TaxistaGson taxista) {
		this.taxista = taxista;
	}

	public boolean isEhTaxista() {
		return ehTaxista;
	}

	public void setEhTaxista(boolean ehTaxista) {
		this.ehTaxista = ehTaxista;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getSenhaDevice() {
		return senhaDevice;
	}

	public void setSenhaDevice(String senhaDevice) {
		this.senhaDevice = senhaDevice;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
