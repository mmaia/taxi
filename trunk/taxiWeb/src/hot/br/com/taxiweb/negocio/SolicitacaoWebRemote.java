package br.com.taxiweb.negocio;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import br.com.taxiweb.gson.PosicaoTaxistaGson;
import br.com.taxiweb.model.PosicaoTaxista;
import br.com.taxiweb.model.Solicitacao;
import br.com.taxiweb.model.Usuario;

@Scope(ScopeType.CONVERSATION)
@Name("solicitacaoWebRemote")
public class SolicitacaoWebRemote {

	@Logger
	Log log;
	
	@In
	EntityManager entityManager;
	
	@In
	private StatusMessages statusMessages;
	
	StatusSolicitacao statusSolicitacao = StatusSolicitacao.LIVRE;
	
	public StatusSolicitacao getStatusSolicitacao() {
		return statusSolicitacao;
	}


	public void setStatusSolicitacao(StatusSolicitacao statusSolicitacao) {
		this.statusSolicitacao = statusSolicitacao;
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}


	@WebRemote
	public PosicaoTaxistaGson recuperaPosicaoTaxista(long idSolicitacao)
	{
		PosicaoTaxista pt = recuperaPosicaoAtualTaxista(idSolicitacao);
		PosicaoTaxistaGson result = new PosicaoTaxistaGson();
		//garante que não retorna null
		if(pt == null)
		{
			result.setLatitude(0);
			result.setLongitude(0);
			return result;
		}
		log.debug("Executou SolicitacaoWebRemote.recuperaPosicaoTaxista ");
		result.setLatitude(pt.getLatitude());
		result.setLongitude(pt.getLongitude());
		setStatusSolicitacao(StatusSolicitacao.TAXISTA_CHEGANDO);
		log.debug("Recuperou posicao da base latitude ==>> " + result.getLatitude() +" longitude ==>> " + result.getLongitude());
		return result;
	}
	
	
	
	/**
	 * @author rafael
	 * Método confirma o cancelamento de uma solicitação de táxi
	 * @param idSolicitacao
	 * @return
	 */
	@WebRemote
	public boolean cancelaPedidoEmAndamento(long idSolicitacao)
	{
		log.debug("Entrou SolicitacaoWebRemote.confirmaCancelaPedidoEmAndamento"+idSolicitacao);
		
		Solicitacao sol = getEntityManager().find(Solicitacao.class, idSolicitacao);
		sol.setStatus(Solicitacao.STATUS_CANCELADA);

		//verifica se a solicitação foi aceita ou não por
		//alguns taxista
		if(sol.getTaxista() == null ){
			//criar um novo Objeto taxista para poder ser inserido na base o id=2 (id ficticio) 
			Usuario taxista = new Usuario();
	        taxista.setEhTaxista(true);
	        //usuario usado na base para teste
	        taxista.setId(2L);
	        sol.setTaxista(taxista);
		}
		
		//atualiza a base com status cancelado e o ID do taxista como 99999
		getEntityManager().persist(sol);
		sol = getEntityManager().find(Solicitacao.class, idSolicitacao);
		
		//se o retorno for confirmado o status cancelado 
		if(sol.getStatus() == Solicitacao.STATUS_CANCELADA){
			log.debug("Entrou SolicitacaoWebRemote.cancelaPedidoEmAndamento retornou true para a solicitacao "+idSolicitacao);
			statusMessages.add("Pedido cancelado!");
			return true;
		}else{
			log.debug("Entrou SolicitacaoWebRemote.cancelaPedidoEmAndamento retornou false para a solicitacao "+idSolicitacao);
			statusMessages.add("Erro ao tentar cancelar!");
			return false;
		}

	}
	
	
	/**
	 * Recupera o id da ultima solicitacao feita pelo usuario passado
	 * @param usuario o username (email) do usuario para que a última solicitação que ele efetuou seja recuperada.
	 * @return o id da ultima solicitação de taxi feita pelo usuario passado como parâmetro.
	 */
	@WebRemote
	public long recuperaIdUltimaSolicitacaoUsuario(long idUsuario)
	{
		long result = 0;
		log.debug("Entrou SolicitacaoWebRemote.recuperaIdUltimaSolicitacaoUsuario");
		result = recuperaIdSolicitacaoUsuario(idUsuario);
		log.debug("id ultima solicitacao deste usuario " + result);
		return result;
	}
	
	
	/**
	 * Recupera a posição atual do taxista enviada pelo device.
	 * @param idSolicitacao
	 * @return objeto PosicaoTaxista com a latitude e longitude informados ou null se não houver registro de posicionamento do taxista.
	 */
	private PosicaoTaxista recuperaPosicaoAtualTaxista(long idSolicitacao)
	{
		log.debug("Entrou SolicitacaoWebRemote.recuperaPosicaoAtualTaxista");
		PosicaoTaxista posTax = null;
		
		
		try
		{
			Solicitacao sol = getEntityManager().find(Solicitacao.class, idSolicitacao);
			getEntityManager().refresh(sol);
			posTax = sol.getPosicaoTaxista();
		}
		catch(NoResultException nre)
		{
			log.error("Tentativa de atualizar coordenada de taxista para solicitação de corrida que não é do taxista passado!");
		}
		return posTax;
	}
	
	private long recuperaIdSolicitacaoUsuario(long idUsuario)
	{
		log.debug("Entrou SolicitacaoWebRemote.recuperaIdUltimaSolicitacaoUsuario");
		long result = 0;
		Query query = getEntityManager().createQuery("select solicitacao.id from Solicitacao solicitacao where solicitacao.passageiro.id = :idUsuario order by solicitacao.dataHora desc");
		query.setParameter("idUsuario", idUsuario);
		query.setMaxResults(1);
		result = (Long)query.getSingleResult();
		return result;
	}
	

}


