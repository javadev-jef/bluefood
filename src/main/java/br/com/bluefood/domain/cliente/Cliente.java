package br.com.bluefood.domain.cliente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.usuario.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * CLIENT MODEL
 */
@SuppressWarnings("serial")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "TBL_CLIENTE")
public class Cliente extends Usuario
{
    @NotBlank(message = "O Nome não pode ser vazio")
    @Size(max = 80, message = "O Nome é muito grande")
    @Column(length = 80, nullable = false)
    private String nome;

    @NotBlank(message = "O CPF não pode ser vazio")
    @Pattern(regexp = "[0-9]{10,11}", message = "O CPF possui o formato inválido")
    @Column(length = 11, nullable = false)
    private String cpf;
}