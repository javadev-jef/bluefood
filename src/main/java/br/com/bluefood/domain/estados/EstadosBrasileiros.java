package br.com.bluefood.domain.estados;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.endereco.Endereco;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * STATE MODEL
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_ESTADOS_BRASILEIROS")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EstadosBrasileiros implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank(message = "A sigla não pode ser nulla")
    @Size(max = 2, min = 2, message = "Sigla inválida")
    @Column(length = 2)
    private String sigla;

    @NotBlank(message = "A sigla não pode ser nulla")
    @Size(max = 60, message = "A descrição é muito grande")
    @Column(length = 60)
    private String descricao;

    @OneToMany(mappedBy = "uf")
    private List<Endereco> enderecos;
}