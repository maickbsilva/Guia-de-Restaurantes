package br.senai.sp.cfp138.guiderest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.senai.sp.cfp138.guiderest.model.Restaurante;

public interface RestauranteRepository extends PagingAndSortingRepository<Restaurante, Long>{

	@Query("SELECT t FROM Restaurante t WHERE t.tipo.id = :p")
	public List<Restaurante> buscaPorTipo(@Param("p") Long tipo);
	
	/**
	 * 
	 * @Query("SELECT t FROM TipoRestaurante t WHERE t.palavrasChave LIKE %:p%")
	public List<TipoRestaurante> buscaPorPChave(@Param("p") String palavrasChave);
	 */
}
