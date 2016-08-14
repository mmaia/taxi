package br.com.taxiweb.action;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Solicitacao;
import br.com.taxiweb.model.Usuario;

@Scope(ScopeType.CONVERSATION)
@Name("solicitacaoAction")
public class SolicitacaoAction implements Serializable {

	private static final long serialVersionUID = 5615211972311591322L;

	@Logger
	Log log;

	@In
	EntityManager entityManager;

	@In
	private FacesMessages facesMessages;
	
	@DataModel
	List<Solicitacao> listaSolicitacoes;
	
	@In(create=false, scope=ScopeType.SESSION)
	Usuario usuario;

	
	// @In
	// Usuario passageiro;

	/**
	 * Método que recupera todas as solicitações e carrega a listaSolicitacoes para ser utilizada em front end facelets(DataMoel).
	 */
	@SuppressWarnings("unchecked")
	@Create
	public void recuperaSolicitacoes(){	
		log.debug("Recuperando ultimas 5 solicitacoes do passageiro para mostrar na tela de solicitacoes de taxi.");
		Query query = entityManager.createQuery("select solicitacao from Solicitacao solicitacao where solicitacao.passageiro.id = :idUsuario order by solicitacao.dataHora desc");
		query.setParameter("idUsuario", usuario.getId());
		query.setMaxResults(5);
		listaSolicitacoes = query.getResultList();
	}
	

	/**
	 * atualiza listaSolicitacoes para ser utilizada em front end facelets(DataMoel).
	 */
	public void refresh()
	{
		recuperaSolicitacoes();
	}
}
