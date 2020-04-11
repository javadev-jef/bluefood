package br.com.bluefood.domain.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CATEGORY REPOSITORY
 */
public interface CategoriaRepository extends JpaRepository<CategoriaRestaurante, Integer>
{
    
}