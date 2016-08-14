package br.com.taxiweb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.security.management.RoleName;

@Entity
@Name("role")
public class Role implements Serializable {
	
	private static final long serialVersionUID = 3905381619401193034L;
	private Long id;
	private String nome;
	private String descricao;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@RoleName
	@Length(max = 100)
	@NotNull
	@Column(unique=true)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Length(max = 250)
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}