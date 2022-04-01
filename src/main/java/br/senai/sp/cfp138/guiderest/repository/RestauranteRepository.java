package br.senai.sp.cfp138.guiderest.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.senai.sp.cfp138.guiderest.model.TipoRestaurante;


public interface RestauranteRepository extends PagingAndSortingRepository<TipoRestaurante, Long>{
	
	@Query("SELECT t FROM TipoRestaurante t WHERE t.palavrasChave LIKE %:p%")
	public List<TipoRestaurante> buscaPorPChave(@Param("p") String palavrasChave);
	
	@Query("SELECT t FROM TipoRestaurante t WHERE t.nome LIKE %:p%")
	public List<TipoRestaurante> buscaPorNome(@Param("p") String nome);
	
	@Query("SELECT t FROM TipoRestaurante t WHERE t.descricao LIKE %:p%")
	public List<TipoRestaurante> buscaPorDescricao(@Param("p") String descricao);
	
}
