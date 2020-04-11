package br.com.bluefood.domain.estados;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bluefood.domain.estados.EstadosBrasileiros;

/**
 * STATE REPOSITORY
 */
public interface EstadosRepository extends JpaRepository<EstadosBrasileiros, Integer>
{
    
}