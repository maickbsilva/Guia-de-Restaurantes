package br.senai.sp.cfp138.guiderest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.senai.sp.cfp138.guiderest.model.Restaurante;
import br.senai.sp.cfp138.guiderest.repository.RestauranteRepository;
import br.senai.sp.cfp138.guiderest.repository.TipoRestauranteRepository;

@Controller
public class RestauranteController {
	@Autowired
	private TipoRestauranteRepository repTipo;
	@Autowired
	private RestauranteRepository repository;
	
	@RequestMapping("cadastroRestaurante")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "cadastroRestaurante";
	}
	
	
	@RequestMapping("salvarRestaurante")
	public String salvar(Restaurante restaurante, @RequestParam("fileFotos") MultipartFile[] fileFotos) {
		repository.save(restaurante);
		return "redirect:cadastroRestaurante";
		
	}
	
	
}
