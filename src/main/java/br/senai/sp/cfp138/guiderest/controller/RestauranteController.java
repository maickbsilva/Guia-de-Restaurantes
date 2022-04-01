package br.senai.sp.cfp138.guiderest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.senai.sp.cfp138.guiderest.model.Administrador;
import br.senai.sp.cfp138.guiderest.model.TipoRestaurante;
import br.senai.sp.cfp138.guiderest.repository.RestauranteRepository;


@Controller
public class RestauranteController {
	
	//variavel para persistencia dos dados
	@Autowired
	private RestauranteRepository repository;

	@RequestMapping("formRest")
	public String RestPage() {
		return "formRest";
	}
	
	@RequestMapping(value="salvarRest", method = RequestMethod.POST)
	public String salvar(TipoRestaurante restaurante, RedirectAttributes attr) {
		repository.save(restaurante);
		attr.addFlashAttribute("mensagemSucesso", "Restaurante cadastrado com sucesso! --- ID:"+restaurante.getId());
		return "redirect:formRest";
	}
	
	@RequestMapping("listaRest/{page}")
	public String lista(Model model, @PathVariable("page") int page) {
		
		//cria um pageable informando os parametros da pagina
		PageRequest pageable = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC, "id"));
		
		//cria um page de adm atraves dos parametros passados ao repository
		Page<TipoRestaurante> pagina = repository.findAll(pageable);
		
		model.addAttribute("rests", pagina.getContent());
		
		//variavel para o total de paginas
		int totalPages = pagina.getTotalPages();
		
		//cria um List de inteiros para armazenar os numeros das paginas
		List<Integer> numPaginas = new ArrayList<Integer>();
		
		//preencher o List com as paginas
		for(int i = 1; i <= totalPages; i++) {
			//adidonar a pagina ao list
			numPaginas.add(i);
		}
		
		//adiciona os valores a model
		model.addAttribute("numPaginas", numPaginas);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pagAtual", page);
	
		return "listaRest";
	}
	
	@RequestMapping("alterarRest")
	public String alterarRest(Long id, Model model) {
		TipoRestaurante tipoRest = repository.findById(id).get();
		model.addAttribute("rest", tipoRest);
		return "forward:formRest";
	}
	
	@RequestMapping("excluirRest")
	public String excluirRest(Long id) {
		repository.deleteById(id);
		return "redirect:listaRest/1";
	}
	
	@RequestMapping("busca")
	public String busca(String escolha, String palavra, Model model) {
	
		if (escolha.equals("nome")) {
			model.addAttribute("rests", repository.buscaPorNome(palavra));
		}else if(escolha.equals("descricao")) {
			model.addAttribute("rests", repository.buscaPorDescricao(palavra));
		}else {		
			
		model.addAttribute("rests", repository.buscaPorPChave(palavra));
		}
		
		return "listaRest";
	}
	
	
	
	
}
