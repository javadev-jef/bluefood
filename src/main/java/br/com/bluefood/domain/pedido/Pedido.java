package br.com.bluefood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.bluefood.domain.cliente.Cliente;
import br.com.bluefood.domain.endereco.Endereco;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.utils.FormatUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * ORDER
 */
@Entity
@Getter @Setter
@Table(name = "TBL_PEDIDO")
@SuppressWarnings("serial")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido implements Serializable
{

    @Getter
    public enum Status
    {
        Producao(1, "Em produção", false),
        Entrega(2, "Saiu para entrega", false),
        Concluido(3, "Concluído", true);

        int ordem;
        String descricao;
        boolean ultimo;

        Status(int ordem, String descricao, boolean ultimo)
        {
            this.ordem = ordem;
            this.descricao = descricao;
            this.ultimo = ultimo;
        }

        public static Status fromOrdem(int ordem)
        {
            for(Status status : Status.values())
            {
                if(status.getOrdem() == ordem)
                {
                    return status;
                }
            }

            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDateTime data;

    @NotNull
    private Status status;

    @NotNull
    @ManyToOne
    private Cliente cliente;

    @NotNull
    @ManyToOne
    private Restaurante restaurante;

    @NotNull
    private BigDecimal subtotal;

    @NotNull
    private BigDecimal taxaEntrega;

    @NotNull
    private BigDecimal total;

    @OneToMany(mappedBy = "id.pedido", fetch = FetchType.EAGER)
    private Set<ItemPedido> itens = new LinkedHashSet<>(0);

    public String getShortMonthName()
    {
        return data.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public String getDayOfMonth()
    {
        return String.format("%02d", this.data.getDayOfMonth());
    }

    public String getFormattedId()
    {
        return String.format("#%04d", this.id);
    }

    public String getDateFormatted()
    {
        return FormatUtils.newDateFormatted(this.data);
    }

    public void definirProximoStatus()
    {
        int ordem = status.getOrdem();

        Status newStatus = Status.fromOrdem(ordem + 1);

        if(newStatus != null)
        {
            this.status = newStatus;
        }
    }

    public String getLegendaProximoStatus()
    {
        int ordem = status.getOrdem();

        return Status.fromOrdem(ordem + 1) == null ? null : Status.fromOrdem(ordem + 1).getDescricao();
    }

    public String getEnderecoCompletoCliente()
    {
        Endereco endereco = cliente.getEndereco();
        
        return endereco.getLogradouro() + ", N°" + endereco.getNumero() + " - " + endereco.getBairro();
    }
}