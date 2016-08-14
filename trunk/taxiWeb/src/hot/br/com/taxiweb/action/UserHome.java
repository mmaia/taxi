package br.com.taxiweb.action;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import br.com.taxiweb.model.Usuario;

@Name("userHome")
public class UserHome extends EntityHome<Usuario> {

	private static final long serialVersionUID = 6233948326778894191L;
	
	@Logger
	Log log;

	public void setUserId(Long id) {
		setId(id);
	}

	public Long getUserId() {
		return (Long) getId();
	}

	@Override
	protected Usuario createInstance() {
		log.debug("Criando nova instancia de usuario!");
		Usuario user = new Usuario();
		return user;
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

	public Usuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
