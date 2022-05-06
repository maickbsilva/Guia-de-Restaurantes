package br.senai.sp.cfp138.guiderest.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Restaurante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String cep;
	private String endereco;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String atracoes;
	private String formaDePagamento;
	private String horario;
	private String site;
	private String telefone;
	private Boolean delivery;
	private Boolean acessibilidade;
	private Boolean driveThru;
	private Boolean estacionamento;
	private Boolean espacoPet;
	private Boolean playground;
	@ManyToOne
	private TipoRestaurante tipo;
	@Column(columnDefinition = "TEXT")
	private String fotos;
	private int preco;
	//igual esta escrito na model avaliacao
	@OneToMany(mappedBy = "restaurante")
	private List<Avaliacao> avaliacoes;
	
	//retorna as fotos na forma de vetor de String
	public String[] verFotos() {
		return fotos.split(";");
				
	}
}
