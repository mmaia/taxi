package br.com.taxiandroid.bean;

/**
 * Usado dentro do Bean ControleFinancas
 * @author linhadiretalipe
 *
 */
public class TabelaControleFinanca {
	private String data;
	private String horaInicio;
	private String horaFim;
	private String valorCorrida;

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraFim() {
		return horaFim;
	}
	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}
	public String getValorCorrida() {
		return valorCorrida;
	}
	public void setValorCorrida(String valorCorrida) {
		this.valorCorrida = valorCorrida;
	}
}
