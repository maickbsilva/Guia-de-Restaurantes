package br.senai.sp.cfp138.guiderest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp138.guiderest.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

}
