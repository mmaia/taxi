package br.com.taxiweb.seguranca;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManager;

@Scope(ScopeType.CONVERSATION)
@Name("administracaoUsuario")
public class AdministracaoUsuario implements java.io.Serializable {

	private static final long serialVersionUID = -5755884234776706891L;

	@In
	private IdentityManager identityManager;

	@Logger
	Log log;
	
	@In(create=true)
	UsuarioAction usuarioAction;
	
	
	/**
	 * Pefis
	 * Administrador = 1;
	 * Cliente = 2;
	 * Taxista = 3;
	 */
	int perfil = 0;//sem perfil nenhum default
	
	String loginUsuarioSelecionado;

	String roleAtual;
	
	public void desabilitaUsuario(final String login) {
		log.debug("desabilitando usuario ==>> " + login);
		if (login == null)
			return;
		new RunAsOperation() {
			public void execute() {
				identityManager.disableUser(login);
			}
		}.addRole("admin").run();
		usuarioAction.refresh();
	}

	public void habilitaUsuario(final String login) {
		log.debug("habilitando usuario ==>> " + login);
		if (login == null)
			return;
		new RunAsOperation() {
			public void execute() {
				identityManager.enableUser(login);
			}
		}.addRole("admin").run();
		usuarioAction.refresh();
	}

	public void promoveParaAdministrador(final String login) {
		log.debug("Adicionando perfil de ADMIN para o usuário ==>> " + login);
		if (login == null)
			return;
		new RunAsOperation() {
			public void execute() {
				identityManager.grantRole(login, "ADMIN");
			}
		}.addRole("admin").run();
		log.debug("Adicionou perfil de ADMIN para o usuário ==>> " + login);
		usuarioAction.refresh();
	}

	public void removeAdministrador(final String login) {
		log.debug("Revogando perfil de ADMIN para o usuário ==>> " + login);
		if (login == null)
			return;
		new RunAsOperation() {
			public void execute() {
				identityManager.revokeRole(login, "ADMIN");
			}
		}.addRole("admin").run();
		usuarioAction.refresh();
	}
	
	public void mudarPerfil()
	{
		if(perfil == 0)
		{
			return;
		}
		if(loginUsuarioSelecionado == null || loginUsuarioSelecionado.equals(""))
		{
			log.debug("AdministracaoUsuario.mudarPerfil ==> saindo sem fazer nada! ");
			return;//não faz nada pois não alterou nada no usuario
		}
		
		new RunAsOperation() {
			public void execute() {
				List<String> listaRoles = identityManager.getGrantedRoles(loginUsuarioSelecionado);
				roleAtual = listaRoles.get(0);
				//revoga role atual
				identityManager.revokeRole(loginUsuarioSelecionado, roleAtual);
			}
		}.addRole("admin").run();
		
		//atualiza role do usuário para nova selecionada
		new RunAsOperation() {
			public void execute() {
				//revoga role atual
				if(perfil == 1)
					identityManager.grantRole(loginUsuarioSelecionado, "ADMIN");
				if(perfil == 2)
					identityManager.grantRole(loginUsuarioSelecionado, "CLIENTE");
				if(perfil == 3)
					identityManager.grantRole(loginUsuarioSelecionado, "TAXISTA");
			}
		}.addRole("admin").run();
		
		log.debug("Resetando perfilSelecionado!");
		perfil = 0;
	}

	public int getPerfil() {
		return perfil;
	}

	public void setPerfil(int perfil) {
		this.perfil = perfil;
	}

	public String getLoginUsuarioSelecionado() {
		return loginUsuarioSelecionado;
	}

	public void setLoginUsuarioSelecionado(String loginUsuarioSelecionado) {
		this.loginUsuarioSelecionado = loginUsuarioSelecionado;
	}
	
	

}
