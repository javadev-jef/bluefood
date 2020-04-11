package br.com.bluefood.domain.pedido;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ORDER ITEM REPOSITORY
 */
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoPK>
{

}