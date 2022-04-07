package br.senai.sp.cfp138.guiderest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.cfp138.guiderest.repository.TipoRestauranteRepository;

@Controller
public class RestauranteController {
	@Autowired
	private TipoRestauranteRepository repTipo;
	
	
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "restaurante/form";
	}
	
	@RequestMapping("cadastroRestaurante")
	public String RestPage() {
		return "cadastroRestaurante";
	}
}
