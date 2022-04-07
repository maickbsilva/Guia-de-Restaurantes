package br.senai.sp.cfp138.guiderest.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp138.guiderest.model.Restaurante;
import br.senai.sp.cfp138.guiderest.model.TipoRestaurante;

public interface RestauranteRepository extends PagingAndSortingRepository<Restaurante, Long>{

}
