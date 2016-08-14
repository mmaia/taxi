package br.com.taxiweb.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Taxista {
	private String cpf;
	private String cnh;
	private String permissao;
	private String matricla;
	
	@Column
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	@Column
	public String getCnh() {
		return cnh;
	}
	public void setCnh(String cnh) {
		this.cnh = cnh;
	}
	
	@Column
	public String getPermissao() {
		return permissao;
	}
	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}
	
	@Column
	public String getMatricla() {
		return matricla;
	}
	public void setMatricla(String matricla) {
		this.matricla = matricla;
	}
	
}
