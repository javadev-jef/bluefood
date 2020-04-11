package br.com.bluefood.domain.pagamento;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.bluefood.domain.pedido.Pedido;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * PAYMENT MODEL
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "TBL_PAGAMENTO")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento implements Serializable
{
    @Id
    private Integer id;

    @OneToOne @NotNull
    @MapsId
    private Pedido pedido;

    @NotNull
    private LocalDateTime data;

    @NotNull @Size(min = 4, max = 4)
    private String numCartaoFinal;

    @Column(nullable = false, length = 20)
    private String bandeiraCartao;

    private String status;

    private Integer idTransacao;

    public void definirNumeroFinal(String numCartao)
    {
        this.numCartaoFinal = numCartao.substring(12);
    }
}