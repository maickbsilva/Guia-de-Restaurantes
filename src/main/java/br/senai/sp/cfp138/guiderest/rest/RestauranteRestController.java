package br.senai.sp.cfp138.guiderest.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.cfp138.guiderest.annotation.Publico;
import br.senai.sp.cfp138.guiderest.model.Restaurante;
import br.senai.sp.cfp138.guiderest.repository.RestauranteRepository;

@RestController
@RequestMapping("/api/restaurante")
public class RestauranteRestController {

	@Autowired
	private RestauranteRepository repository;
	
	@Publico
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Restaurante> getRestaurantes(){
		return repository.findAll();
	}
	
	@Publico
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Restaurante> getRestaurante(@PathVariable("id") Long idRestaurante){
		//tenta buscar o restaurante no repository
		Optional<Restaurante> optional = repository.findById(idRestaurante);
		
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Publico
	@RequestMapping(value= "/tipo/{id}", method = RequestMethod.GET)
	public List<Restaurante> getTipoRestaurante(@PathVariable("id")Long id){
		return repository.buscaPorTipo(id);
	}
}
