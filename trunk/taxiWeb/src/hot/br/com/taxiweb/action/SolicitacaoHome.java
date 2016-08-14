package br.com.taxiweb.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Solicitacao;
import br.com.taxiweb.model.Usuario;
import br.com.taxiweb.negocio.SolicitacaoWebRemote;
import br.com.taxiweb.negocio.StatusSolicitacao;

@Scope(ScopeType.CONVERSATION)
@Name("solicitacaoHome")
public class SolicitacaoHome extends EntityHome<Solicitacao> {

	private static final long serialVersionUID = 2005128643709685127L;
	
	@In(create=false, scope=ScopeType.SESSION)
	Usuario usuario;
	
	@Logger
	Log log;
	
	@In(create=true)
	SolicitacaoAction solicitacaoAction;
	
	@In(create=true)
	SolicitacaoWebRemote solicitacaoWebRemote;
	
	Usuario taxista;
	
	
	public void setSolicitacaoId(Long id)
	{
		setId(id);
	}
	
	public Long getSolicitacaoId()
	{
		return (Long) getId();
	}
	
	@Override
	public String persist(){
		log.debug("Salvando solicitacao SolicitacaoHome.persist");
		try{
			getInstance().setPassageiro(usuario);
			String result = super.persist();
			setSolicitacaoId(null);
			if(result.equalsIgnoreCase("persisted"))
			{
				log.debug("SolicitacaoHome setando persisted true");
				solicitacaoAction.refresh();
			}
			solicitacaoWebRemote.setStatusSolicitacao(StatusSolicitacao.AGUARDANDO_TAXISTA);
			return result;
		}
		catch(IllegalStateException e){
			return "error";
		}
	}
	
	
	public String reset()
	{
		log.debug("SolicitacaoHome.reset");
		solicitacaoWebRemote.setStatusSolicitacao(StatusSolicitacao.LIVRE);
		return "reset";
	}
	
	
	@Override
	protected Solicitacao createInstance() {
		log.debug("Solicitacao sendo instanciada em SolicitacaoHome.createInstance");
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

	public Usuario getTaxista() {
		return taxista;
	}

	public void setTaxista(Usuario taxista) {
		this.taxista = taxista;
	}
	
}
