package br.com.bluefood.domain.pedido;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BILLING ITEMS REPORT 
 */
@Getter
@AllArgsConstructor
public class RelatorioItemFaturamento 
{
    private Integer id;
    private String nome;
    private Long quantidade;
    private BigDecimal valor;
}