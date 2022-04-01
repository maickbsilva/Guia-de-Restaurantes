package br.senai.sp.cfp138.guiderest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.senai.sp.cfp138.guiderest.util.HashUtil;
import lombok.Data;

//cria os getters e setters
@Data

//mapeia a entidade para o JPA
@Entity
public class Administrador {
	//chave primaria e auto-incremento
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	
	//definir a coluna email como um indice unico
	@Column(unique = true)
	@Email
	private String email;
	@NotEmpty
	private String senha;
	
	//metodo set que aplica o hash na senha
	public void setSenha(String senha) {
		this.senha = HashUtil.hash(senha);
	}
	
	//seta o hash na senha
	public void setSenhaComHash(String hash) {
		this.senha = hash;
		
	}
}
