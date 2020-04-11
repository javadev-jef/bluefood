package br.com.bluefood.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CLIENT REPOSITORY
 */
public interface ClienteRepository extends JpaRepository<Cliente, Integer>
{
    public Cliente findByEmail(String email);
}