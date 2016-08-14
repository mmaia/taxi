package br.com.taxiweb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;

@NamedQueries({
@NamedQuery(name="recuperaUsuarioPorEmail", query="SELECT usuario FROM Usuario usuario WHERE usuario.email = :email"),
@NamedQuery(name="recuperaUsuarioPorEmailSenhaDevice", 
	query="SELECT usuario FROM Usuario usuario WHERE usuario.email = :email and usuario.senhaDevice = :senhaDevice")
})

@Entity
@Table(name="Usuario")
@Name("usuario")
public class Usuario implements Serializable{
	
	
	private static final long serialVersionUID = 3797344831919573636L;
	private Long id;
	private String nome;
	private String sobreNome;
	private String sexo;
	private Endereco endereco = new Endereco();
	private String email;
	private String telefone1;
	private String celular;
	private String senha;
	
	private Taxista taxista = new Taxista();
	private boolean ehTaxista = false;
	
	private List<Role> roles;
	private boolean enabled = true;//qdo usuário se cadastra já está automaticamente habilitado.
	
	private Date dataCadastro;
	
	private String senhaDevice;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	@UserPassword(hash="SHA")
	@Length(min=6, max = 100)
	@NotNull
	public String getSenha() {
		return senha;
	}
	
	@UserRoles
	@ManyToMany
	@JoinTable(joinColumns={@JoinColumn(name="usuario_id")}, inverseJoinColumns={@JoinColumn(name="role_id")})
	public List<Role> getRoles() {
		return roles;
	}

	@UserEnabled
	@Column
	public boolean isEnabled() {
		return enabled;
	}
	
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return nome;
	}
	
	@UserPrincipal
	@Length(max = 50)
	@Email
	@Column(unique=true)
	public String getEmail() {
		return email;
	}
	
	@Column
	public String getTelefone1() {
		return telefone1;
	}

	@Column
	public String getSobreNome() {
		return sobreNome;
	}

	@Column
	public String getSexo() {
		return sexo;
	}

	
	public Endereco getEndereco() {
		return endereco;
	}

	@Column
	public String getCelular() {
		return celular;
	}
	
	public Taxista getTaxista() {
		return taxista;
	}

	@Column
	public boolean isEhTaxista() {
		return ehTaxista;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	public Date getDataCadastro() {
		return dataCadastro;
	}
	
	@Column
	public String getSenhaDevice() {
		return senhaDevice;
	}

	public void setSenhaDevice(String senhaDevice) {
		this.senhaDevice = senhaDevice;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public void setTaxista(Taxista taxista) {
		this.taxista = taxista;
	}

	public void setEhTaxista(boolean ehTaxista) {
		this.ehTaxista = ehTaxista;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
}
