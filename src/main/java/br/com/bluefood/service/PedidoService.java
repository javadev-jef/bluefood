package br.com.bluefood.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluefood.domain.pagamento.CartaoCredito;
import br.com.bluefood.domain.pagamento.Pagamento;
import br.com.bluefood.domain.pagamento.PagamentoRepository;
import br.com.bluefood.domain.pedido.Carrinho;
import br.com.bluefood.domain.pedido.ItemPedido;
import br.com.bluefood.domain.pedido.ItemPedidoPK;
import br.com.bluefood.domain.pedido.ItemPedidoRepository;
import br.com.bluefood.domain.pedido.Pedido;
import br.com.bluefood.domain.pedido.Pedido.Status;
import br.com.bluefood.domain.pedido.PedidoRepository;
import br.com.bluefood.utils.PagarMeUtils;
import br.com.bluefood.utils.SecurityUtils;
import me.pagar.model.PagarMeException;
import me.pagar.model.Transaction;

/**
 * ORDER SERVICE
 */
@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PagarMeService pagarMeService;

    @Transactional(rollbackFor = PagarMeException.class)
    public Pedido criarEPagar(final Carrinho carrinho, final CartaoCredito cartaoCredito) throws PagarMeException 
    {
        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setCliente(SecurityUtils.loggedCliente());
        pedido.setRestaurante(carrinho.getRestaurante());
        pedido.setStatus(Status.Producao);
        pedido.setTaxaEntrega(carrinho.getRestaurante().getTaxaEntrega());
        pedido.setSubtotal(carrinho.getPrecoTotal(false));
        pedido.setTotal(carrinho.getPrecoTotal(true));

        pedido = pedidoRepository.save(pedido);

        int ordem = 1;

        for (final ItemPedido itemPedido : carrinho.getItens()) 
        {
            itemPedido.setId(new ItemPedidoPK(pedido, ordem++));
            itemPedidoRepository.save(itemPedido);

            pedido.getItens().add(itemPedido);
        }

        final Transaction transacao = pagarMeService.pedidoPay(pedido, cartaoCredito);

        if(transacao.getStatus().equals(Transaction.Status.REFUSED) || 
            transacao.getStatus().equals(Transaction.Status.REFUNDED) ||
            transacao.getStatus().equals(Transaction.Status.PENDING_REFUND) ||
            transacao.getStatus().equals(Transaction.Status.CHARGEDBACK))
        {
            final String statusPg = PagarMeUtils.verifyTransactionStatus(transacao);
            throw new PagarMeException(statusPg);
        }

        final Pagamento pagamento = new Pagamento();
        pagamento.setData(LocalDateTime.now());
        pagamento.setPedido(pedido);
        pagamento.setNumCartaoFinal(transacao.getCard().getLastDigits());
        pagamento.setBandeiraCartao(transacao.getCard().getBrand().name());
        pagamento.setStatus(transacao.getStatus().name());
        pagamento.setIdTransacao(transacao.getId());
        pagamentoRepository.save(pagamento);

        return pedido;
    }
}