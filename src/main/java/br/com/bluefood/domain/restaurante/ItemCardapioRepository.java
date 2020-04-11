package br.com.bluefood.domain.restaurante;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MENU ITEM REPOSITORY
 */
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer>
{
    public List<ItemCardapio> findByRestaurante_IdOrderByNome(Integer idRestaurante);

    public List<ItemCardapio> findByCategoria_IdAndRestaurante_Id(Integer categoriaId, Integer restauranteId);
}