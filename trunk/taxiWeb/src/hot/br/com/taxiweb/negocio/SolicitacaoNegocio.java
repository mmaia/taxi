package br.com.taxiweb.negocio;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Solicitacao;
import br.com.taxiweb.model.Usuario;

@Scope(ScopeType.EVENT)
@Name("solicitacaoNegocio")
public class SolicitacaoNegocio extends EntityHome<Solicitacao>  implements Serializable{
	

	private static final long serialVersionUID = -3765179550027965341L;

	@Logger
	Log log;
	
	/**
	 * Recupera ultimas solicitações respondidas pelo taxista
	 * @param quantidadeSolicitacoes o número de solicitações que devem ser recuperadas em ordem descendente de data.(Da última para trás).
	 * @param taxista o taxista do qual as solicitações devem ser recuperadas.
	 * @return uma lista com o histórico de solicitações atendidos por aquele taxista.
	 */
	@SuppressWarnings("unchecked")
	public List<Solicitacao> recuperaUltimasSolicitacoesTaxista(int quantidadeSolicitacoes, Usuario taxista)
	{
		log.debug("Entrou SolicitacaoNegocio.recuperaUltimasSolicitacoesTaxista");
		List<Solicitacao> sols = null;
		Query query = getEntityManager().createQuery("select solicitacao from Solicitacao solicitacao where solicitacao.taxista = :taxista" +
				" order by solicitacao.dataHora desc");
		query.setParameter("taxista", taxista);
		query.setMaxResults(quantidadeSolicitacoes);
		try
		{
			log.debug("Executando query para recuperar solicitacao...");
			sols = query.getResultList();
			log.debug("Qtde registros retornados ==>> " + sols.size());
		}
		catch(NoResultException nre)
		{
			log.error("Tentativa de atualizar coordenada de taxista para solicitação de corrida que não é do taxista passado!");
		}
		catch(Exception e)
		{
			log.error("Erro ao tentar recuperar solicitacao de taxista: " + e.getMessage());
			e.printStackTrace();
		}
		return sols;
	}
	
	/**
	 * Recupera a lista de todas as solicitações pendentes ainda não reservadas por nenhum taxista.
	 * @return lista de solicitações existentes não reservadas por taxistas.
	 */
	@SuppressWarnings("unchecked")
	public List<Solicitacao> listaSolicitacoes()
	{
		log.debug("Executando SolicitacaoNegocio.listaSolicitacoes");
		List<Solicitacao> solicitacoes;
		Query query = getEntityManager().createQuery("select solicitacao from Solicitacao solicitacao where solicitacao.taxista is empty");
		solicitacoes = query.getResultList();
		return solicitacoes;
	}
	
	/**
	 * Método que pesquisa na base uma solicitação com id passado de um determinado taxista.
	 * @param idSolicitacao o id da solicitação a ser pesquisada.
	 * @param taxista o taxista que tem a solicitação já previamente reservada.
	 * @return Solicitacao pesquisada ou null se não recuperar a solicitação na base de dados.
	 */
	public Solicitacao recuperaSolicitacaoDeTaxista(long idSolicitacao, Usuario taxista)
	{
		log.debug("Entrou SolicitacaoNegocio.recuperaSolicitacaoDeTaxista");
		Solicitacao sol = null;
		Query query = getEntityManager().createQuery("select solicitacao from Solicitacao solicitacao where solicitacao.taxista = :taxista" +
				" and solicitacao.id = :idSolicitacao");
		query.setParameter("idSolicitacao", idSolicitacao);
		query.setParameter("taxista", taxista);
		try
		{
			log.debug("Executando query para recuperar solicitacao...");
			List<Solicitacao> sols = query.getResultList();
			log.debug("Qtde registros retornados ==>> " + sols.size());
			sol = sols.get(0);
		}
		catch(NoResultException nre)
		{
			log.error("Tentativa de atualizar coordenada de taxista para solicitação de corrida que não é do taxista passado!");
		}
		catch(Exception e)
		{
			log.error("Erro ao tentar recuperar solicitacao de taxista: " + e.getMessage());
			e.printStackTrace();
		}
		log.debug("Solicitacao Recuperada ==> " + sol.getOrigem());
		return sol;
	}
	
	public void setSolicitacaoId(Long id)
	{
		setId(id);
	}
	
	public Long getSolicitacaoId()
	{
		return (Long) getId();
	}
	
	@Override
	protected Solicitacao createInstance() {
		Solicitacao solicitacao = new Solicitacao();
		return solicitacao;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Solicitacao getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
}
