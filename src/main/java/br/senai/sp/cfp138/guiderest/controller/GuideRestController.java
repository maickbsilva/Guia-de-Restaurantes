package br.senai.sp.cfp138.guiderest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.hash.Hashing;

import br.senai.sp.cfp138.guiderest.model.Administrador;
import br.senai.sp.cfp138.guiderest.repository.AdminRepository;
import br.senai.sp.cfp138.guiderest.util.HashUtil;
@Controller
public class GuideRestController {
	
	//variavel para persistencia dos dados
	@Autowired
	private AdminRepository repository;
	

	@RequestMapping("AdminPage")
	public String acessoAdm() {
		return "AdminPage";
	}
	
	@RequestMapping(value="salvarAdmin", method = RequestMethod.POST)
	public String salvarAdmin(@Valid Administrador admin, BindingResult result, RedirectAttributes attr) {
		
		//verifica se houveram erros na validacao
		
		if (result.hasErrors()) {
			attr.addFlashAttribute("mensagemErro", "Verifique os campos...");
			return "redirect:AdminPage";
		}
		
		//variavel para descobrir alteracao ou inserção
		boolean alteracao = admin.getId() != null ? true : false;

		//verifica se a senha esta vazia)
		if (admin.getSenha().equals(HashUtil.hash(""))) {
			
		if (!alteracao) {
			//retirar a parte antes do @ no email
			String parte = admin.getEmail().substring(0, admin.getEmail().indexOf("@"));
			//setar a parte na senha do admin
			admin.setSenha(parte);
		}else {
			//buscar a senha atual no banco de dados
			String hash = repository.findById(admin.getId()).get().getSenha();
			//setar o hash na senha
			admin.setSenhaComHash(hash);
		}
		
		}
		
		
		try {
			//salva no bd a entidade 
			repository.save(admin);	
			//adiciona uma mensagem de sucesso
			attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso! --- ID:"+admin.getId());
			
		} catch (Exception e) {

			//adiciona uma mensagem de erro
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar"+ e.getMessage());
		}
		
		//redireciona para o formulario
		return "redirect:AdminPage";
		
	}
	
	//request que leva para a lista
	@RequestMapping("listaAdmin/{page}")
	public String listaAdmin(Model model, @PathVariable("page") int page) {
		
		//cria um pageable informando os parametros da pagina
		PageRequest pageable = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC, "id"));
		
		//cria um page de adm atraves dos parametros passados ao repository
		Page<Administrador> pagina = repository.findAll(pageable);
		
		//adiciona à model a lista com os admins
		model.addAttribute("admins", pagina.getContent());
		
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
		
		//retorna para a lista
		return "lista";
		
	}
	
	@RequestMapping("alterar")
	public String alterar(Long id, Model model) {
		Administrador adm = repository.findById(id).get();
		model.addAttribute("adm", adm);
		return "forward:AdminPage";
	}
	
	@RequestMapping("excluir")
	public String excluir(Long id) {
		repository.deleteById(id);
		return "redirect:listaAdmin/1";
	}
	
	
	
}
