package br.com.taxiweb.gson;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SolicitacaoGson {
	
	private long idSolicitacao;
	private String origem;
	private String destino;
	private Date dataHora = new Date();
	private int numeroPassageiros;
	private String informacoesAdicionais;
	private int status;
	
	public long getIdSolicitacao() {
		return idSolicitacao;
	}
	public void setIdSolicitacao(long idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Date getDataHora() {
		return dataHora;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public int getNumeroPassageiros() {
		return numeroPassageiros;
	}
	public void setNumeroPassageiros(int numeroPassageiros) {
		this.numeroPassageiros = numeroPassageiros;
	}
	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}
	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * Transforma String JSON em lista de objetos Gson(SolicitacaoGson) para ser utilizado
	 * pelo device.
	 * @param json - A string JSON que foi recebida do serviço REST
	 * @return Coleção de objetos SolicitacaoGson.
	 */
	public Collection<SolicitacaoGson> recuperaSolicitacoes(String json)
	{
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<SolicitacaoGson>>(){}.getType();
		Collection<SolicitacaoGson> listaSolGson = gson.fromJson(json, collectionType);
		return listaSolGson;
	}
	
	/**
	 * Transforma lista de solicitações para padrão Gson e depois transforma 
	 * na String JSON que será enviada para o device.
	 * @param solicitacoes - Uma lista com objetos do tipo Solicitacao
	 * @return String JSON para ser utilizada nas interfaces REST
	 */
	public String listaSolicitacoesGson(List<SolicitacaoGson> solGson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(solGson);
		return result;
	}
	
	/** 
	 * Transforma uma Objeto Solicitacao para padrão Gson e depois transforma
	 * na String JSON que será enviada apra o device.
	 * @param uGson - um objeto do tipo Solicitacao
	 * @return String JSON para ser utilzada nas interfaces REST
	 * @author rafael 28.08.2012
	 */
	public static final  String toJSON(SolicitacaoGson uGson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(uGson);
		return result;
	}
	
}
