package br.com.taxiandroid.bean;

import java.util.List;

public class ControleFinancas {

	private String valorCorridaDia;
	private String valorCorridaMes;
	private String qntCorridaDia;
	private String qntCorridaMes;
	private String valorTotalCooperativa;
	private List<TabelaControleFinanca> dadosDia;
	private String dataAtual;

	
	public String getDataAtual() {
		return dataAtual;
	}
	public void setDataAtual(String dataAtual) {
		this.dataAtual = dataAtual;
	}
	public String getValorCorridaDia() {
		return valorCorridaDia;
	}
	public void setValorCorridaDia(String valorCorridaDia) {
		this.valorCorridaDia = valorCorridaDia;
	}
	public String getValorCorridaMes() {
		return valorCorridaMes;
	}
	public void setValorCorridaMes(String valorCorridaMes) {
		this.valorCorridaMes = valorCorridaMes;
	}
	public String getQntCorridaDia() {
		return qntCorridaDia;
	}
	public void setQntCorridaDia(String qntCorridaDia) {
		this.qntCorridaDia = qntCorridaDia;
	}
	public String getQntCorridaMes() {
		return qntCorridaMes;
	}
	public void setQntCorridaMes(String qntCorridaMes) {
		this.qntCorridaMes = qntCorridaMes;
	}
	public String getValorTotalCooperativa() {
		return valorTotalCooperativa;
	}
	public void setValorTotalCooperativa(String valorTotalCooperativa) {
		this.valorTotalCooperativa = valorTotalCooperativa;
	}
	public List<TabelaControleFinanca> getDadosDia() {
		return dadosDia;
	}
	public void setDadosDia(List<TabelaControleFinanca> dadosDia) {
		this.dadosDia = dadosDia;
	}
}
