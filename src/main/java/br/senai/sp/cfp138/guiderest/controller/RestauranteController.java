package br.senai.sp.cfp138.guiderest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.senai.sp.cfp138.guiderest.model.Restaurante;
import br.senai.sp.cfp138.guiderest.model.TipoRestaurante;
import br.senai.sp.cfp138.guiderest.repository.RestauranteRepository;
import br.senai.sp.cfp138.guiderest.repository.TipoRestauranteRepository;
import br.senai.sp.cfp138.guiderest.util.FirebaseUtil;

@Controller
public class RestauranteController {
	@Autowired
	private TipoRestauranteRepository repTipo;
	@Autowired
	private RestauranteRepository repository;
	@Autowired
	private FirebaseUtil fireUtil;

	@RequestMapping("cadastroRestaurante")
	public String form(Model model) {
		model.addAttribute("tipos", repTipo.findAllByOrderByNomeAsc());
		return "cadastroRestaurante";
	}

	@RequestMapping("salvarRestaurante")
	public String salvar(Restaurante restaurante, @RequestParam("fileFotos") MultipartFile[] fileFotos) {

		// string para armazenar as URLs
		String fotos = restaurante.getFotos();

		// percorre cada arquivo no vetor
		for (MultipartFile arquivo : fileFotos) {

			// verifica se o arquivo existe
			if (arquivo.getOriginalFilename().isEmpty()) {
				// vai para o proximo arquivo
				continue;
			}

			try {
				// faz o upload e guarda a URL na String fotos
				fotos += fireUtil.upload(arquivo) + ";";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// guarda na variavel restaurante as fotos
		restaurante.setFotos(fotos);
		// salvar no bd
		repository.save(restaurante);

		return "redirect:cadastroRestaurante";

	}
	
	@RequestMapping("listaRestaurantes/{page}")
	public String lista(Model model, @PathVariable("page") int page) {

		// cria um pageable informando os parametros da pagina
		PageRequest pageable = PageRequest.of(page - 1, 6, Sort.by(Sort.Direction.ASC, "id"));

		// cria um page de adm atraves dos parametros passados ao repository
		Page<Restaurante> pagina = repository.findAll(pageable);

		model.addAttribute("restaurantes", pagina.getContent());

		// variavel para o total de paginas
		int totalPages = pagina.getTotalPages();

		// cria um List de inteiros para armazenar os numeros das paginas
		List<Integer> numPaginas = new ArrayList<Integer>();

		// preencher o List com as paginas
		for (int i = 1; i <= totalPages; i++) {
			// adidonar a pagina ao list
			numPaginas.add(i);
		}
		

		// adiciona os valores a model
		model.addAttribute("numPaginas", numPaginas);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pagAtual", page);

		return "listaRestaurantes";
	}
	
	@RequestMapping("alterarRestaurantes")
	public String alterarRest(Long id, Model model) {
		Restaurante Rest = repository.findById(id).get();
		model.addAttribute("restaurantes", Rest);
		return "forward:cadastroRestaurante";
	}
	
	@RequestMapping("excluirRestaurantes")
	public String excluirRest(Long id) {
		Restaurante rest = repository.findById(id).get();
		if (rest.getFotos().length() > 0) {
			for (String foto : rest.verFotos()) {
				fireUtil.deletar(foto);
			}
		}
		
		repository.delete(rest);
		
		return "redirect:listaRestaurantes/1";
	}
	
	@RequestMapping("excluirFotos")
	public String excluirFotos(Long idRest, int numFoto, Model model) {
		//busca o restaurante
		Restaurante rest = repository.findById(idRest).get();
		
		//busca a URL da foto
		String urlFoto = rest.verFotos()[numFoto];
		
		//deleta a foto
		fireUtil.deletar(urlFoto);
		
		//remove a url do atributo foto
		rest.setFotos(rest.getFotos().replace(urlFoto+";", ""));
		
		//salva no banco
		repository.save(rest);
		
		//coloca o rest na model
		model.addAttribute("restaurantes", rest);
		return "forward:cadastroRestaurante";
	}

}
