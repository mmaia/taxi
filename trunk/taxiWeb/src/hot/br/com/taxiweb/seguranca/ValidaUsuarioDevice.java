package br.com.taxiweb.seguranca;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Taxista;
import br.com.taxiweb.model.Usuario;

@Scope(ScopeType.EVENT)
@Name("validaUsuarioDevice")
public class ValidaUsuarioDevice implements Serializable{
	
	@In(create=true)
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	/**
	 * Método que pesquisa na base por usuário com email e senha device especificados, se existir na base de dados retorna
	 * objeto Usuario com todos os dados se não existir na base, retorna null.
	 * @param email o email do usuario
	 * @param senhaDevice a senha do device já autenticada no sistema
	 * @return o Usuario ou null se não existir o usuario na base de dados.
	 */
	public Usuario valida(String email, String senhaDevice)
	{
		log.info("Executando ValidaUsuarioDevice.valida");
		Query query = entityManager.createNamedQuery("recuperaUsuarioPorEmailSenhaDevice");
		query.setParameter("email", email);
		query.setParameter("senhaDevice", senhaDevice);
		List<Usuario> lUsuarios = query.getResultList();
		
		if(lUsuarios.size() < 1){
			log.info("Usuario nao encontrado na base, retornando null");
			return null;
		}
			
		//pega so primeiro elemento da lista que é o usuário recuperado pela named query
		Usuario user = lUsuarios.get(0);
		log.info("Usuario encontrado retornando com nome ==>> " + user.getNome());
		return user;
	}
	
	public boolean ehTaxista(Usuario usuario)
	{
		log.info("Checando se usuario eh taxista.");
		boolean result = false;
		Taxista taxista = usuario.getTaxista(); 
		if(taxista == null || taxista.getCnh() == null ||taxista.getCnh().equals(""))
		{
			log.debug("Usuario nao eh taxista");
			return result;
		}
		else
		{
			log.debug("usuario eh taxista");
			result = true;
			return result;
		}
	}
}
