package br.com.taxiweb.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManagementException;
import org.jboss.seam.security.management.IdentityManager;

import br.com.taxiweb.action.UserRegistroBean;

@Path("cadastroUsuario")
@Name("cadastroUsuarioRest")
public class CadastroUsuarioRest {
	
	@Logger
	Log log;
	
	@In(create = true)
	UserRegistroBean userRegistroBean;
	
	@In
	private IdentityManager identityManager;
	
	/**
	 * Método para cadastrar um cliente pelo celular com o mínimo de informações necessárias.
	 * @param login - o email do usuario que será utilizado também como login
	 * @param senha - a senha deste usuário. A confirmação de senha deve ser tratada antes de executar este método no código do device.
	 * @return - HTTP Status code 200 Ok se o cadastro for realizado com sucesso
	 */
	@POST
	@Path("/cadastrarSimplificado")
	@Produces(MediaType.TEXT_PLAIN)
	public Response cadastrar(@FormParam("login") final String login, @FormParam("senha") final String senha) {
		log.info("Executando LoginRest.cadastrar recebeu login e senha ==>> " + login + " | " + senha);
		try {
			 new RunAsOperation() {
				 public void execute() {
					 identityManager.createUser(login, senha, "", "");
					 identityManager.grantRole(login, "CLIENTE");//por default só cadastra como cliente
				 }
			 }.addRole("admin").run();
	         
		  } catch(IdentityManagementException e) {
			 e.printStackTrace();
		  }
		return Response.ok().build();
	}
	
	@POST
	@Path("/cadastrar")
	@Produces(MediaType.TEXT_PLAIN)
	public Response cadastrar(String jsonString) {
		log.info("Executando LoginRest.cadastrar recebeu String Json para processar");
		
		return Response.ok().build();
	}
}
