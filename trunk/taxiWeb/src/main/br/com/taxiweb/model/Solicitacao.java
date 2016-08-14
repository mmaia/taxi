package br.com.taxiweb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Entity
@Table(name="Solicitacao")
@Name("solicitacao")
public class Solicitacao implements Serializable {

	private static final long serialVersionUID = -5177946639176176750L;
	private Long id;
	private String origem;
	private String destino;
	private Date dataHora = new Date();
	private int numeroPassageiros;
	private String informacoesAdicionais;
	private Usuario passageiro;
	private Usuario taxista;
	
	/** STATUS_PENDENTE  =0 */
	public static final int STATUS_PENDENTE  =0;
	/** STATUS_RESERVADA =1 */
	public static final int STATUS_RESERVADA =1;
	/** STATUS_FINALIZADA=2 */
	public static final int STATUS_FINALIZADA=2;
	/** STATUS_CANCELADA =3 */
	public static final int STATUS_CANCELADA =3;
	
	
	private PosicaoTaxista posicaoTaxista;
	
	
	//definição de status da solicitação 
	//0 - Pendente
	//1 - Reservada por taxista.
	//2 - Finalizada
	private int status;
	
	@Logger
	Log log;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	@Column
	@NotNull
	public String getOrigem() {
		return origem;
	}
	
	@Column
	public String getDestino() {
		return destino;
	}
	
	@Column
	public Date getDataHora() {
		return dataHora;
	}
	
	@Column
	public int getNumeroPassageiros() {
		return numeroPassageiros;
	}
	
	@Column
	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}
	
	@ManyToOne	
	@JoinColumn(name="passageiro_id")
	public Usuario getPassageiro() {
		return passageiro;
	}
	
	@ManyToOne	
	@JoinColumn(name="taxista_id")
	public Usuario getTaxista() {
		return taxista;
	}
	
	@Column
	public int getStatus() {
		return status;
	}
	
	@Embedded
	public PosicaoTaxista getPosicaoTaxista() {
		return posicaoTaxista;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}
	public void setNumeroPassageiros(int numeroPassageiros) {
		this.numeroPassageiros = numeroPassageiros;
	}
	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}
	public void setPassageiro(Usuario passageiro) {
		this.passageiro = passageiro;
	}
	public void setTaxista(Usuario taxista) {
		this.taxista = taxista;
	}
	
	public void setPosicaoTaxista(PosicaoTaxista posicaoTaxista) {
		this.posicaoTaxista = posicaoTaxista;
	}

//	@Override
//	public String toString()
//	{
//		log.debug("Executando toString de Solicitacao");
//		StringBuffer result = new StringBuffer(
//				
//				"Passageiro: " + this.getPassageiro().getNome() +
//				"\nOrigem: " + this.getOrigem() + 
//				"\nDestino: " + this.getDestino());
//				
//		if(! (this.getTaxista() == null))
//		{
//			result.append("\nTaxista: " + this.getTaxista().getNome()); 
//		}
//		result.append(
//				"\nNumero Passageiros: " + this.getNumeroPassageiros());
//		if(! (this.getInformacoesAdicionais() == null))
//		{		
//			result.append("\nInformacoes Adicionais: " + this.getInformacoesAdicionais());
//		}
//		log.debug("Executou toString de Solicitacao");
//		return result.toString();
//	}
	
}
