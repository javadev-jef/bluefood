package br.com.bluefood.domain.pedido;

import java.time.LocalDate;

import javax.validation.constraints.Min;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * ORDER ITEM REPORT FILTER
 */
@Data
public class RelatorioItemFilter 
{
    @AllArgsConstructor @Getter
    public enum OptionsFilter
    {
        ITEMID("Item"),
        PERIODO("Periodo"),
        PERIODO_AND_ITEMID("Periodo + Item");

        private String legenda;
    }

    @Min(value = 1)
    private Integer itemId;

    private OptionsFilter optionFilter = OptionsFilter.ITEMID;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;  
}