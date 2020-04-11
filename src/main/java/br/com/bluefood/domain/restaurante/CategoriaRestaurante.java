package br.com.bluefood.domain.restaurante;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * RESTAURANT CATEGORY
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_CATEGORIA")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaRestaurante implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "Descrição não informada")
    @Size(max = 20, message = "A descrição é muito grande")
    @Column(length = 20, nullable = false)
    private String descricao;

    @NotBlank(message = "Imagem não anexada")
    @Size(max = 50, message = "O caminho da imagem é muito grande")
    @Column(length = 50, nullable = false)
    private String urlImagem;

    @NotBlank(message = "Capa não anexada")
    @Size(max = 50, message = "O caminho da imagem é muito grande")
    @Column(length = 50, nullable = true)
    private String urlCapa;

    @ManyToMany(mappedBy = "categorias")
    private Set<Restaurante> restaurantes = new HashSet<>(0);

    @OneToMany(mappedBy = "categoria")
    private Set<ItemCardapio> itemCardapio = new HashSet<>(0);
}