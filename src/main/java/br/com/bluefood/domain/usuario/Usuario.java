package br.com.bluefood.domain.usuario;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.endereco.Endereco;
import br.com.bluefood.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * USER MODEL
 */
@SuppressWarnings("serial")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class Usuario implements Serializable
{
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O E-mail não pode ser vazio")
    @Size(max = 60, message = "O Nome é muito grande")
    @Email(message = "O E-mail é inválido")
    @Column(length = 60, nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "[0-9]{10,11}", message = "Telefone inválido")
    @NotBlank(message = "O Telefone não poder ser vazio")
    @Column(length = 11, nullable = false)
    private String telefone;

    @NotBlank(message = "A senha não pode ser vazia")
    @Size(max = 80, message = "A senha é muito grande")
    @Column(length = 80, nullable = false)
    private String senha;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar dataCadastro = Calendar.getInstance();

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "endereco_id", nullable = false)
    @Valid
    private Endereco endereco = new Endereco();

    private transient boolean alterarSenha;

    public void encryptPassword()
    {
        this.senha = StringUtils.encrypt(this.senha);
    }
}