package br.com.taxiweb.action;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Usuario;

@Name("userPesquisa")
public class UserPesquisa {
	
	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	/**
	 * Metodo que pesquisa usuario pelo email que tambem e o login.
	 * @param email o e-mail a ser pesquisado.
	 * @return Usuario representado pelo email ou null se não for encontrado na base de dados.
	 */
	@SuppressWarnings("unchecked")
	public Usuario pesquisaUsuarioPorEmail(String email)
	{
		log.debug("Executando UserPesquisa.pesquisaUsuarioPorEmail com email ==>> " + email);
		Query query = entityManager.createNamedQuery("recuperaUsuarioPorEmail");
		query.setParameter("email", email);
		List<Usuario> lUsuarios = query.getResultList();
		
		if(lUsuarios.size() < 1)
		{
			log.debug("Usuario nao encontrado, retornando null...");
			return null;
		}
		
		//pega so primeiro elemento da lista que é o usuário recuperado pela named query
		Usuario user = lUsuarios.get(0);
		log.debug("Usuario encontrado retornando com nome ==>> " + user.getNome());
		return user;
	}
}
