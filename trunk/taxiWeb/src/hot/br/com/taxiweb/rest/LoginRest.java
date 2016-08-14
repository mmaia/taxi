package br.com.taxiweb.rest;


import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import br.com.taxiweb.action.UserHome;
import br.com.taxiweb.action.UserPesquisa;
import br.com.taxiweb.model.Usuario;
import br.com.taxiweb.util.GeraSenhaRandomica;

/**
 * Implementação Rest dos serviços de login
 * 
 * @author mmaia
 * 
 */
@Path("login")
@Name("loginRest")
public class LoginRest {

	@Logger
	Log log;

	@In(create = true)
	UserPesquisa userPesquisa;
	
	@In(create = true)
	UserHome userHome;
	

	/**
	 * Método para autenticar o device. Este método é responsável por processar o login de um device. Quando o usuário e
	 * senha passados são válidos ele checa se o usuário já é cadastrado na base central e retorna a
	 * resposta depedendo do resultado.
	 * 
	 * A url de chamada para este serviço REST é: http://url:porta/$CONTEXTO_WEB/seam/resource/rest/login/autenticar , deve-se 
	 * substituir url:porta por um enedereço válido de servidor e o CONTEXTO_WEB por um contexto válido de aplicação.
	 * 
	 * O método recupera os dados login(e-mail) e senha do usuário de form parameters com os respectivos nomes login e senha.
	 * 
	 * @param email
	 *            O email de cadastro do usuário.
	 * @param senha
	 *            A senha de autenticação escolhida pelo usuário ao fazer o cadastro.
	 * @return O retorno depende do resultado da autenticação. <br\>
	 *         1) Status code http 200 OK se autenticar com sucesso. Neste caso retorna também a senha gerada para
	 *         autenticação de sincronização vinda daquele device posteriormente.<br/>
	 *         2) Status code http 401 UNAUTHORIZED se o usuário e senha não forem válidos. <quote>401 Unauthorized
	 *         Similar to 403 Forbidden, but specifically for use when authentication is possible but has failed or not
	 *         yet been provided. </quote> <br/>
	 *         3) Status code http 204 NO CONTENT se o login passado não estiver cadastrado na Base de Dados principal do software.
	 */
	@POST
	@Path("/autenticar")
	@Produces(MediaType.TEXT_PLAIN)
	public Response autenticar(@FormParam("login") String login, @FormParam("senha") String senha) {
		log.info("Executando LoginRest.autenticar recebeu login e senha ==>> " + login + " | " + senha);
		
		Usuario usuario = userPesquisa.pesquisaUsuarioPorEmail(login);

		// se usuario nao existir na base informa que não existe. 204 NO CONTENT
		if (usuario == null) {
			log.info("Usuario com email informado nao existe, retornando 204 NO CONTENT");
			return Response.noContent().build();
		} else if (!usuario.getSenha().equals(senha)) {
			log.info("Usuario com email informado existe mas senha não confere, retornando 401 UNAUTHORIZED ");
			// se usuario existir mais senha não confere retorna 401 unauthorized.
			return Response.status(401).build();
		} else {
			log.info("Usuario encontrado e autenticado, gerando senha randomica para enviar para o device");
			userHome.setId(usuario.getId());
			String senhaDevice = GeraSenhaRandomica.randomstring();
			userHome.getInstance().setSenhaDevice(senhaDevice);
			userHome.update();
			log.info("Senha gerada e gravada para o device! Device autenticado com sucesso senha device ==>> " + senhaDevice);
			return Response.ok(senhaDevice).build();
		}
	}
	
	@GET
	@Path("/teste/{stringTeste}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response teste(@PathParam("stringTeste") String testeString)
	{
		log.info("Executando LoginRest.teste");
		return Response.ok("Executou teste REST ==>> " + testeString).build();
	}
	
}
