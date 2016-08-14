package br.com.taxiweb.action;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManagementException;
import org.jboss.seam.security.management.IdentityManager;

import br.com.taxiweb.model.Endereco;
import br.com.taxiweb.model.Taxista;
import br.com.taxiweb.model.Usuario;


/**
 * Classe que implementa funcionalidade de cadastro de usuário do software
 * 
 * @author mmaia
 * 
 */
@Name("registroBean")
public class UserRegistroBean implements Serializable{
	

	private static final long serialVersionUID = 4949473945569083164L;

	@In
	private Usuario usuario;

	@In
	private IdentityManager identityManager;

	@In
	private StatusMessages statusMessages;

	@Logger
	Log log;

	private String verificar;

	private boolean registrado = false;
	
	boolean senhaAlteradaComSucesso = false;
	
	private String mensagemTrocaSenha;
	
	@In
	EntityManager entityManager;
	
	public static final String EVENT_USER_CREATED = "org.jboss.seam.security.management.userCreated";
	public static final String EVENT_PRE_PERSIST_USER = "org.jboss.seam.security.management.prePersistUser";

	public void atualizarUsuario() {
		log.debug("Entrou atualizarUsuario.registrarUsuario");
		log.debug("Vai atualizar dados do usuario ==>> " + usuario.getEmail() +" id na base => " + usuario.getId());
		entityManager.merge(usuario);
		//createQuery("update Usuario set nome = \'" + usuario.getNome() +"\' and sobrenome='"+usuario.getSobreNome()+"'" +
		//		" and  where id = " + usuario.getId());
//		em.getTransaction().commit();
		statusMessages.add("Alterado  dados do usuário #{usuario.getNome()}.");
	}
	
	public void registrarUsuario() {
		log.debug("Entrou UserRegistroBean.registrarUsuario");
		if (usuario.getSenha().equals(verificar)) {
			try {
				new RunAsOperation() {
					public void execute() {
						registrado = identityManager.createUser(usuario.getEmail(),
								usuario.getSenha());
						identityManager.grantRole(usuario.getEmail(),
								"CLIENTE");
					}
				}.addRole("admin").run();
				
				statusMessages
						.add("Cadastrado com sucesso! Bem Vindo(a) #{usuario.getNome()}, você já pode efetuar seu login.");
				log.info("Novo usuario cadastrado com sucesso email: " + usuario.getEmail());

			} catch (IdentityManagementException e) {
				e.printStackTrace();
				statusMessages.add(e.getMessage());
			}
		} else {
			statusMessages.addToControl("verificar",
					"Senha não confere. Digite novamente!");
			verificar = null;
		}
	}

	/**
	 * Muda senha do usuário.
	 */
	public void modificaSenha()
	{
		log.debug("Modificando senha, nova senha ==>> " + usuario.getSenha() + " para o usuário ==>> " + usuario.getEmail());
		if(usuario.getSenha().length() <= 6 )
		{
			mensagemTrocaSenha = "Senha deve ter mais de 6 caracteres!";
			return;
		}
		if (usuario.getSenha().equals(verificar))
		{
			log.debug("Vai atualizar senha Login ==>> " + usuario.getEmail() + " Nova senha => " + usuario.getSenha() + " id na base => " + usuario.getId());
			Query query = entityManager.createQuery("update Usuario set senha = \'" + usuario.getSenha() +"\' where id = " + usuario.getId());
			int registrosAfetados = query.executeUpdate();
			if(registrosAfetados == 1)
				mensagemTrocaSenha = "Senha alterada com sucesso";
			else
				mensagemTrocaSenha = "Ocorreu um erro ao alterar senha";
		}
		else
		{
			mensagemTrocaSenha = "Senha não confere com confirmação de senha. Digite novamente!";
			verificar = null;
			return;
		}
	}
	
	@Observer(EVENT_PRE_PERSIST_USER)
	public void onPrePersist(Usuario usuario) {
		log.debug("Passando e-mail para ser persistido no cadastramento");
		usuario.setEmail(this.usuario.getEmail());
		usuario.setNome(this.usuario.getNome());
		usuario.setSobreNome(this.usuario.getSobreNome());
		usuario.setTelefone1(this.usuario.getTelefone1());
		usuario.setCelular(this.usuario.getCelular());
		usuario.setEnabled(this.usuario.isEnabled());
		usuario.setSexo(this.usuario.getSexo());
		Endereco end = new Endereco();
		end.setLogradouro(this.usuario.getEndereco().getLogradouro());
		end.setBairro(this.usuario.getEndereco().getBairro());
		end.setCidade(this.usuario.getEndereco().getCidade());
		end.setEstado(this.usuario.getEndereco().getEstado());
		end.setPais(this.usuario.getEndereco().getPais());
		end.setCep(this.usuario.getEndereco().getCep());
		usuario.setEndereco(end);
		usuario.setDataCadastro(new Date());
		usuario.setEhTaxista(this.usuario.isEhTaxista());
		if(this.usuario.isEhTaxista())
		{
			Taxista taxista = new Taxista();
			taxista.setCnh(this.usuario.getTaxista().getCnh());
			taxista.setCpf(this.usuario.getTaxista().getCpf());
			taxista.setMatricla(this.usuario.getTaxista().getMatricla());
			taxista.setPermissao(this.usuario.getTaxista().getPermissao());
			usuario.setTaxista(taxista);
		}
	}

	@Observer(EVENT_USER_CREATED)
        public void onUserCreated(Usuario usuario) {
		log.debug("Usuario cadastrado, redirecionando para tela home em UserRegistroBean.onUserCreated");
		//após cadastro redireciona para página inicial para usuário fazer login.
		Redirect redirect = Redirect.instance();
        redirect.setViewId("/home.xhtml");
        redirect.execute();
	}

	public boolean isRegistrado() {
		return registrado;
	}

	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}


	public String getVerificar() {
		return verificar;
	}

	public void setVerificar(String verificar) {
		this.verificar = verificar;
	}

	public String getMensagemTrocaSenha() {
		return mensagemTrocaSenha;
	}

	public void setMensagemTrocaSenha(String mensagemTrocaSenha) {
		this.mensagemTrocaSenha = mensagemTrocaSenha;
	}
}
