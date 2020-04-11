package br.com.bluefood.domain.restaurante;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * RESTAURANT REPOSITORY
 */
public interface RestauranteRepository extends JpaRepository<Restaurante, Integer>
{
    public Restaurante findByEmail(String email);

    public List<Restaurante> findByRazaoIgnoreCaseContaining(String descricao);

    public List<Restaurante> findByCategorias_Id(Integer idCategoria);
    
    public List<Restaurante> findByTaxaEntrega(BigDecimal valorTaxa);

    @Query("SELECT r.id, r.banner FROM Restaurante r WHERE r.banner != null")
    public Object[] getIdAndBanner();
}