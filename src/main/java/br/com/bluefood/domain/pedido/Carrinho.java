package br.com.bluefood.domain.pedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.bluefood.domain.restaurante.ItemCardapio;
import br.com.bluefood.domain.restaurante.Restaurante;
import br.com.bluefood.utils.StringUtils;
import lombok.Getter;

/**
 * SHOPPING CART MODEL
 */
@Getter
@SuppressWarnings("serial")
public class Carrinho implements Serializable
{
    private final List<ItemPedido> itens = new ArrayList<>();
    private Restaurante restaurante;
    private String observacaoTemp;

    /**
     * 
     * @param itemCardapio item a ser adicionado
     * @param qtd quantidade do item
     * @param observacoes observações
     * @throws RestauranteDiferenteException
     */
    public void addItem(final ItemCardapio itemCardapio, final Integer qtd, final String observacoes) throws RestauranteDiferenteException 
    {
        if (itens.size() == 0) 
        {
            restaurante = itemCardapio.getRestaurante();
        } 
        else if (!itemCardapio.getRestaurante().getId().equals(restaurante.getId())) 
        {
            throw new RestauranteDiferenteException();
        }

        if (!exists(itemCardapio, qtd, observacoes))
        {  
            final ItemPedido itemPedido = new ItemPedido();
            itemPedido.setItemCardapio(itemCardapio);
            itemPedido.setQuantidade(qtd);
            itemPedido.setObservacoes(observacoes);
            itemPedido.setPreco(itemCardapio.getPreco());
            itens.add(itemPedido);
        }
    }

    public void removeItem(final ItemCardapio itemCardapio, final Integer indexItem)
    {
        final ItemPedido itemPedido = itens.get(indexItem);
        if(itemPedido.getItemCardapio().getId().equals(itemCardapio.getId()))
        {
            itens.remove(indexItem.intValue());
        }

        if(itens.size() == 0)
        {
            restaurante = null;
        }
    }

    public void updateQtdItem(final Integer indexItem, final Integer operator)
    {
        final ItemPedido itemPedido = this.getItens().get(indexItem);
        final Integer qtdCurrent = itemPedido.getQuantidade();

        if(qtdCurrent == 1 && operator < 1)
        {
            return;
        }

        itemPedido.setQuantidade(qtdCurrent + operator);
    }

    public void adicionarItem(final ItemPedido itemPedido) 
    {
        try 
        {
            addItem(itemPedido.getItemCardapio(), itemPedido.getQuantidade(), itemPedido.getObservacoes());
        } 
        catch (final RestauranteDiferenteException e) 
        {
        }
    }

    private boolean exists(ItemCardapio itemCardapioTemp, Integer qtd, String observacoes) throws RestauranteDiferenteException
    {
        ItemCardapio itemCardapioCarrinho;
        List<ItemPedido> listTemp = new ArrayList<>();

        for(ItemPedido itemPedido : itens)
        {
            itemCardapioCarrinho = itemPedido.getItemCardapio();
            if(itemCardapioCarrinho.getId().equals(itemCardapioTemp.getId()))
            {
                listTemp.add(itemPedido);
            }
        }

        for(ItemPedido itemPedido : listTemp)
        {
            if(StringUtils.trimAndEqualsIgnoreCase(itemPedido.getObservacoes(), observacoes))
            {
                int quantidadeCurrent = itemPedido.getQuantidade();
                itemPedido.setQuantidade(quantidadeCurrent + qtd);

                return true;
            }
        }

        return false;
    }

    public BigDecimal getPrecoTotal(final boolean adicionarEntrega)
    {
        BigDecimal soma = BigDecimal.ZERO;

        for(final ItemPedido item : itens)
        {
            soma = soma.add(item.getPrecoCalculado());
        }

        if(adicionarEntrega)
        {
            soma = soma.add(restaurante.getTaxaEntrega());
        }

        return soma;
    }

    public String getTooltip()
    {
        if(isVazio())
        {
            return "Carrinho vazio";
        }

        if(itens.size() == 1)
        {
            return "Você possui "+itens.size()+" item adicionado ao carrinho";
        }

        if(itens.size() > 1)
        {
            return "Você possui "+itens.size()+" itens adicionado ao carrinho";
        }

        return null;
    }

    public void limpar()
    {
        itens.clear();
        restaurante = null;
    }

    public boolean isVazio()
    {
        return itens.size() == 0;
    }
}