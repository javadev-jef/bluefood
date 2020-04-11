package br.com.bluefood.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.domain.pedido.RelatorioItemFaturamento;
import br.com.bluefood.domain.pedido.RelatorioItemFilter;
import br.com.bluefood.domain.pedido.RelatorioPedidoFilter;

/**
 * REPORT SERVICE
 */
@Service
public class RelatorioService
{
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listPedidos(Integer restauranteId, RelatorioPedidoFilter filter)  
    {
        switch(filter.getOptionFilter())
        {
            case IDPEDIDO:
            {
                if(filter.getPedidoId() != null)
                {
                    Pedido pedido = pedidoRepository.findByIdAndRestaurante_Id(filter.getPedidoId(), restauranteId);
                    return pedido != null ? List.of(pedido) : null;
                }
                break;
            }
            case PERIODO:
            {
                LocalDate dataInicial = filter.getDataInicial();
                LocalDate dataFinal = filter.getDataFinal();

                if(dataInicial == null)
                {
                    return List.of();
                }

                if(dataFinal == null)
                {
                    dataFinal = LocalDate.now();
                }

                return pedidoRepository.findByDateInterval(restauranteId, dataInicial.atStartOfDay(), dataFinal.atTime(23, 59, 59));
            }
        }

        return null;
    }

    public List<RelatorioItemFaturamento> calcularFaturamentoItens(Integer restauranteId, RelatorioItemFilter filter)  
    {
        List<Object[]> itensObj = null;
        LocalDateTime dataHoraInicial = null;
        LocalDateTime dataHoraFinal = null;

        if(filter.getOptionFilter() != RelatorioItemFilter.OptionsFilter.ITEMID)
        {
            LocalDate dataInicial = filter.getDataInicial();
            LocalDate dataFinal = filter.getDataFinal();

            if(dataInicial == null)
            {
                return List.of();
            }

            if(dataFinal == null)
            {
                dataFinal = LocalDate.now();
            }

            dataHoraInicial = dataInicial.atStartOfDay();
            dataHoraFinal = dataFinal.atTime(23, 59, 59);
        }

        switch(filter.getOptionFilter())
        {
            case ITEMID:
            {
                if(filter.getItemId() != null)
                {
                    itensObj = pedidoRepository.findItensForFaturamento(restauranteId, filter.getItemId());
                }
                break;
            }
            case PERIODO:
            {
                itensObj = pedidoRepository.findItensForFaturamento(restauranteId, dataHoraInicial, dataHoraFinal);
                break;
            }
            case PERIODO_AND_ITEMID:
            {
                itensObj = pedidoRepository.findItensForFaturamento(restauranteId, filter.getItemId(), dataHoraInicial, dataHoraFinal);
                break;
            }
        }

        if(itensObj != null)
        {
            List<RelatorioItemFaturamento> itens = new ArrayList<>();
            for(Object[] item : itensObj)
            {
                Integer id = (Integer) item[0];
                String nome = (String) item[1];
                Long quantidade = (Long) item[2];
                BigDecimal valor = (BigDecimal) item[3];
                itens.add(new RelatorioItemFaturamento(id, nome, quantidade, valor));
            }

            return itens;
        }

        return null;
    }
}