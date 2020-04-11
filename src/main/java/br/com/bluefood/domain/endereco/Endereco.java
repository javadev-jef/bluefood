package br.com.bluefood.domain.endereco;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.estados.EstadosBrasileiros;
import br.com.bluefood.domain.restaurante.Restaurante;
import lombok.Getter;
import lombok.Setter;

/**
 * ADDRESS MODEL
 */
@SuppressWarnings("serial")
@Getter @Setter
@Entity
@Table(name = "TBL_ENDERECO")
public class Endereco implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O campo logradouro não pode ser vazio")
    @Size(max = 60, message = "A descrição informada é muito grande")
    @Column(length = 60, nullable = false)
    private String logradouro;

    @NotBlank(message = "O campo bairro não pode ser vazio")
    @Size(max = 50, message = "A descrição informada é muito grande")
    @Column(length = 50, nullable = false)
    private String bairro;

    @NotBlank(message = "O campo cidade não pode ser vazio")
    @Size(max = 60, message = "A descrição informada é muito grande")
    @Column(length = 60, nullable = false)
    private String cidade;

    @NotBlank(message = "O campo CEP não pode ser vazio")
    @Pattern(regexp = "[0-9]{8}", message = "O CEP possui o formato inválido")
    @Column(length = 8, nullable = false)
    private String cep;

    @NotBlank(message = "O número não pode ser vazio")
    @Pattern(regexp = "[0-9]+[A-Za-z]?", message = "O número possui o formato inválido")
    @Size(max = 8, message = "Número muito grande")
    @Column(length = 8, nullable = false)
    private String numero;

    @Size(max = 30, message = "A descrição informada é muito grande")
    private String complemento;

    @NotNull(message = "Nenhum estado foi selecionado")
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    @Valid
    private EstadosBrasileiros uf = new EstadosBrasileiros();

    @OneToOne(mappedBy = "endereco")
    private Cliente cliente;

    @OneToOne(mappedBy = "endereco")
    private Restaurante restaurante;
}