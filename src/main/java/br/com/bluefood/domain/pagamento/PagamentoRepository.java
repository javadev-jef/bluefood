package br.com.bluefood.domain.pagamento;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PAYMENT REPOSITORY
 */
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>
{

}