package br.com.bluefood.domain.pedido;

import java.time.LocalDate;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * ORDER REPORT FILTER
 */
@Data
public class RelatorioPedidoFilter 
{
    @AllArgsConstructor @Getter
    public enum OptionsFilter
    {
        IDPEDIDO("Número"),
        PERIODO("Periodo");

        private String legenda;
    }

    @Min(value = 1)
    private Integer pedidoId;

    private OptionsFilter optionFilter = OptionsFilter.IDPEDIDO;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;  
}