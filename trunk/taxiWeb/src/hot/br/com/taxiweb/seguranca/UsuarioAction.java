package br.com.taxiweb.seguranca;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Usuario;
@Scope(ScopeType.CONVERSATION)
@Name("usuarioAction")
@AutoCreate
public class UsuarioAction implements Serializable{
	
	private static final long serialVersionUID = -1364928611732770548L;

	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	@DataModel
	List<Usuario> listaUsuarios;
	
	private String nome;
	
	@SuppressWarnings("unchecked")
	@Create
	public void recuperaTodosUsuarios()
	{
		Query query = entityManager.createQuery("select usuario from Usuario usuario" + montaQuery());
		if(nome != null)
		{
			query.setParameter("nome", "%" + getNome() + "%");
		}
		listaUsuarios = query.getResultList();
	}
	
	private String montaQuery()
	{
		StringBuffer result = new StringBuffer();
		if(nome != null)
		{
			result.append(" where usuario.nome like :nome");
		}
		result.append(" order by usuario.nome");
		return result.toString();
	}
	
	public void refresh()
	{
		listaUsuarios = null;
		nome = null;
		recuperaTodosUsuarios();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
