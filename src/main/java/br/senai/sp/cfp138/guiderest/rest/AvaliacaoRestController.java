package br.senai.sp.cfp138.guiderest.rest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.cfp138.guiderest.annotation.Privado;
import br.senai.sp.cfp138.guiderest.annotation.Publico;
import br.senai.sp.cfp138.guiderest.model.Avaliacao;
import br.senai.sp.cfp138.guiderest.repository.AvaliacaoRepository;

@RestController
@RequestMapping("/api/avaliacao")
public class AvaliacaoRestController {
	@Autowired
	private AvaliacaoRepository repository;
	
	@Privado
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody Avaliacao avaliacao){
		repository.save(avaliacao);
		return ResponseEntity.created(URI.create("/api/avaliacao"+avaliacao.getId())).body(avaliacao);
	}
	
	@Publico
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Avaliacao getById(@PathVariable("id") Long idAvaliacao){
		return repository.findById(idAvaliacao).get();
	}
	
	@Publico
	@RequestMapping(value="/restaurante/{id}", method = RequestMethod.GET)
	public List<Avaliacao> getByRestaurante(@PathVariable("id") Long idRestaurante){
		return repository.findByRestauranteId(idRestaurante);
	}
}
