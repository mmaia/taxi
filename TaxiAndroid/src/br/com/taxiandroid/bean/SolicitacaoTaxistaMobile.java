package br.com.taxiandroid.bean;

import java.util.Date;

/**
 * Classe utilizada para recuperar a solicitação atual do taxista caso o programa dê algum problema 
 * no momento da corrida. 
 * @author linhadiretalipe
 *
 */
public class SolicitacaoTaxistaMobile  extends SolicitaTaxiBean{
	
	
	private boolean isCorridaJaFinalizada;
	private long	idSolicitacaoTaxistaMobile;
	//Usa dados da tela de solicitacao
	private String nome;
	private String sobreNome;
	private String celular;
	private String valorCorrida;

	
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

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public void setCorridaJaFinalizada(boolean isCorridaJaFinalizada) {
		this.isCorridaJaFinalizada = isCorridaJaFinalizada;
	}

	public boolean isCorridaJaFinalizada() {
		return isCorridaJaFinalizada;
	}

	public void setIdSolicitacaoTaxistaMobile(long idSolicitacaoTaxistaMobile) {
		this.idSolicitacaoTaxistaMobile = idSolicitacaoTaxistaMobile;
	}

	public long getIdSolicitacaoTaxistaMobile() {
		return idSolicitacaoTaxistaMobile;
	}

	public void setValorCorrida(String valorCorrida) {
		this.valorCorrida = valorCorrida;
	}

	public String getValorCorrida() {
		return valorCorrida;
	}
}
