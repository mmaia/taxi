package br.com.taxiweb.action;

import javax.ejb.Local;

@Local
public interface UserRegistro {
	public void modificaSenha();

	public void registrarUsuario();

	public String getVerificar();

	public void setVerificar(String verificar);

	public void destroy();
	
	public void atualizarUsuario();
}
