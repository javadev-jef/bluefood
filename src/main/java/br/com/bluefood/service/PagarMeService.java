package br.com.bluefood.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.bluefood.domain.pagamento.CartaoCredito;
import br.com.bluefood.domain.pedido.ItemPedido;
import br.com.bluefood.domain.pedido.Pedido;
import me.pagar.model.Address;
import me.pagar.model.Billing;
import me.pagar.model.Customer;
import me.pagar.model.Document;
import me.pagar.model.Item;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Shipping;
import me.pagar.model.Transaction;

/**
 * PAGARME SERVICE
 */
@Service
public class PagarMeService 
{
    @Value("${bluefood.pagarme.api_key}")
    private String api_key;

    public Transaction pedidoPay(Pedido pedido, CartaoCredito cartao) throws PagarMeException
    {
        PagarMe.init(api_key);

        final Transaction transaction = new Transaction();
        final Customer customer = new Customer();
        customer.setType(Customer.Type.INDIVIDUAL);
        customer.setExternalId(String.valueOf(pedido.getId()));
        customer.setName(pedido.getCliente().getNome());
        customer.setBirthday(null);
        customer.setEmail(pedido.getCliente().getEmail());
        customer.setCountry("br");

        final Collection<Document> documents = new ArrayList<>();
        final Document document = new Document();
        document.setType(Document.Type.CPF);
        document.setNumber(cartao.getCpfCnpj());
        documents.add(document);
        customer.setDocuments(documents);

        final Collection<String> phones = new ArrayList<>();
        phones.add("+55"+pedido.getCliente().getTelefone());
        customer.setPhoneNumbers(phones);

        final Billing billing = new Billing();
        billing.setName(pedido.getCliente().getNome());
        final Address address = new Address();
        address.setCity(pedido.getCliente().getEndereco().getCidade());
        address.setCountry("br");
        address.setState(pedido.getCliente().getEndereco().getUf().getSigla());
        address.setNeighborhood(pedido.getCliente().getEndereco().getBairro());
        address.setStreet(pedido.getCliente().getEndereco().getLogradouro());
        address.setZipcode(pedido.getCliente().getEndereco().getCep());
        address.setStreetNumber(pedido.getCliente().getEndereco().getNumero());
        billing.setAddress(address);

        final Shipping shipping = new Shipping();
        shipping.setAddress(address);
        shipping.setName(pedido.getCliente().getNome());
        int taxaCents = pedido.getTaxaEntrega().multiply(BigDecimal.valueOf(100)).intValue();
        shipping.setFee(taxaCents);

        final Collection<Item> items = new ArrayList<>();
        for(ItemPedido itemPedido : pedido.getItens())
        {
            final Item item = new Item();
            item.setId(String.valueOf(itemPedido.getId()));
            item.setQuantity(itemPedido.getQuantidade());
            item.setTangible(Boolean.TRUE);
            item.setTitle(itemPedido.getItemCardapio().getNome());
            int precoCents = itemPedido.getPreco().multiply(BigDecimal.valueOf(100)).intValue();
            item.setUnitPrice(precoCents);

            items.add(item);
        }

        transaction.setShipping(shipping);
        transaction.setBilling(billing);
        transaction.setItems(items);
        transaction.setPaymentMethod(Transaction.PaymentMethod.CREDIT_CARD);
        transaction.setAmount(pedido.getTotal().multiply(BigDecimal.valueOf(100)).intValue());
        transaction.setCardHolderName(cartao.getNomeCartao());
        transaction.setCardNumber(cartao.getNumero());
        transaction.setCardCvv(String.valueOf(cartao.getCodigoSeguranca()));
        transaction.setCardExpirationDate(cartao.getMes()+cartao.getAno());
        transaction.setCustomer(customer);
        transaction.save();

        return transaction;
    }
}