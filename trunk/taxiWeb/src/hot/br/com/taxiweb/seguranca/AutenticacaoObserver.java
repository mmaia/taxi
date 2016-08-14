package br.com.taxiweb.seguranca;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.management.JpaIdentityStore;

import br.com.taxiweb.model.Usuario;

@Name("autenticacaoObserver")
public class AutenticacaoObserver {
	@Logger
	private Log log;

	@In
	private Credentials credentials;

	@In
	EntityManager entityManager;

	@In
	private StatusMessages statusMessages;

	@Observer(Credentials.EVENT_INIT_CREDENTIALS)
	public void credentialsInicializadas() {
		log.info("Credenciais foram inicializadas");
	}

	@Observer(Identity.EVENT_LOGIN_SUCCESSFUL)
	public void loginSuccessful() {
		log.info("O login foi efetuado com sucesso para o Usuario: #0",
				credentials.getUsername());
	}

	@Observer(Identity.EVENT_LOGIN_FAILED)
	public void loginFailed(Exception e) {
		log.info("Erro na autenticacao do usuario: #0, Excecao: #1, Causa: #2",
				credentials.getUsername(), e, e.getCause());
		Query query = entityManager
				.createQuery("select usuario from Usuario usuario where usuario.email like '"
						+ credentials.getUsername() + "'");
		try {
			Usuario user = (Usuario) query.getSingleResult();
			boolean habilitado = user.isEnabled();
			statusMessages.clearGlobalMessages();
			statusMessages.clear();
			if (!habilitado) {

				statusMessages.add("Seu usuário não está habilitado. Procure um Administrador do sistema.");
			}
			else{
				statusMessages.add("Senha não confere, tente novamente!");
			}
		} catch (NoResultException nre) {
			statusMessages.add("Não existe nenhum usuário com este login na base de dados do sistema.");
			nre.printStackTrace();
		}

	}

	/**
	 * Coloca o objeto User na sessao ao fazer o login.
	 * 
	 * @param user
	 */
	@Observer(JpaIdentityStore.EVENT_USER_AUTHENTICATED)
	public void usuarioAutenticado(Usuario usuario) {
		log.info("Login do usuario: " + usuario.getEmail());
		Contexts.getSessionContext().set("usuario", usuario);
	}

	@Observer(Identity.EVENT_LOGGED_OUT)
	public void logOff() {
		log.info("Usuario em processo de logoff ");
		Redirect redirect = Redirect.instance();
		redirect.setViewId("/home.xhtml");
		redirect.execute();
	}
	
	public void limpaConversacao()
	{
		log.info("Capturou evento qeu termina uma conversacao");
	}
}
