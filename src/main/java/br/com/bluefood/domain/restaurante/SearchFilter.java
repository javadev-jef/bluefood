package br.com.bluefood.domain.restaurante;

import br.com.bluefood.utils.StringUtils;
import lombok.Data;
import lombok.Getter;

/**
 * SEARCH FITLER
 */
@Data
public class SearchFilter 
{
    public enum SearchType
    {
        TEXTO, CATEGORIA, SELECTS;
    }

    public enum Order
    {
        TaxaEntrega, TempoEntrega;
    }

    @Getter
    public enum Options
    {
        EntregaGratis("Entrega Grátis"), 
        MenorTaxa("Menor Taxa"), 
        MaiorTaxa("Maior Taxa"),
        MenorTempo("Menor Tempo"),
        MaiorTempo("Maior Tempo");

        private final String legenda;
        Options(String legenda)
        {
            this.legenda = legenda;
        }
    }

    private String texto = "";
    private SearchType searchType;
    private Integer idCategoria;
    private String descCategoria;
    private boolean asc;
    private Order order = Order.TaxaEntrega;
    private boolean entregaGratis;
    private Options option = Options.MenorTaxa;

    public void processFilter()
    {
        if(!StringUtils.isEmpty(this.option.name()))
        {
            Options opt = Options.valueOf(this.option.name());

            if(opt == Options.MaiorTaxa)
            {
                order = Order.TaxaEntrega;
                asc = false;
            }
            else if(opt == Options.MenorTaxa)
            {
                order = Order.TaxaEntrega;
                asc = true;
            }
            else if(opt == Options.MaiorTempo)
            {
                order = Order.TempoEntrega;
                asc = false;
            }
            else if(opt == Options.MenorTempo)
            {
                order = Order.TempoEntrega;
                asc = true;
            }
        }

        if(searchType == SearchType.TEXTO)
        {
            this.idCategoria = null;
        }
        else if(searchType == SearchType.CATEGORIA)
        {
            this.texto = null;
        }
    }
}