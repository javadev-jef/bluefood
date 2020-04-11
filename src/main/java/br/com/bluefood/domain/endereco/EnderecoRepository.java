package br.com.bluefood.domain.endereco;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ADDRESS REPOSITORY
 */
public interface EnderecoRepository extends JpaRepository<Endereco, Integer>
{
    
}